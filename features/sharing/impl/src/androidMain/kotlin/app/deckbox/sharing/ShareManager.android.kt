package app.deckbox.sharing

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Deck
import app.deckbox.core.model.DeckShareAction
import app.deckbox.core.model.ShareAction
import app.deckbox.features.decks.api.export.DeckExporter
import app.deckbox.sharing.api.ShareManager
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@ContributesBinding(MergeActivityScope::class)
@Inject
class AndroidShareManager(
  private val activity: Activity,
  private val deckExporter: DeckExporter,
  private val dispatcherProvider: DispatcherProvider,
) : ShareManager {
  override suspend fun share(shareAction: ShareAction) {
    when (shareAction) {
      is DeckShareAction -> shareDeck(shareAction)
    }
  }

  private suspend fun shareDeck(action: DeckShareAction) {
    when (action.type) {
      DeckShareAction.ExportType.Text -> shareDeckText(action.deck)
      DeckShareAction.ExportType.Image -> shareDeckImage(action.deck)
    }
  }

  private suspend fun shareDeckText(deck: Deck) {
    val text = deckExporter.exportAsText(deck)

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
      putExtra(Intent.EXTRA_TEXT, text)
      type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, "Share ${deck.name}")
    activity.startActivity(shareIntent)
  }

  private suspend fun shareDeckImage(deck: Deck) {
    val image = deckExporter.exportAsImage(deck)
    val imageFileUri = cacheImageToMediaStore(activity, deck, image) ?: cacheImageToDisk(image, deck)

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
      putExtra(Intent.EXTRA_STREAM, imageFileUri)
      addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
      type = "image/jpg"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share ${deck.name}")
    activity.startActivity(shareIntent)
  }

  private suspend fun cacheImageToDisk(image: ImageBitmap, deck: Deck): Uri {
    val bitmap = image.asAndroidBitmap()
    val cacheDir = File(activity.cacheDir, "deck_exports").apply { mkdir() }
    return withContext(dispatcherProvider.io) {
      val outputFile = File.createTempFile(deck.name.fileSafeName(), ".jpg", cacheDir)
      val outputStream = FileOutputStream(outputFile)
      try {
        bitmap.compress(CompressFormat.JPEG, 75, outputStream)
      } finally {
        outputStream.flush()
        outputStream.close()
      }

      FileProvider.getUriForFile(activity, "app.deckbox.android.fileprovider", outputFile)
    }
  }

  private suspend fun cacheImageToMediaStore(
    context: Context,
    deck: Deck,
    image: ImageBitmap,
  ): Uri? = withContext(dispatcherProvider.io) {
    val filename = "${deck.name.fileSafeName()}_${System.currentTimeMillis()}.jpg"

    val contentValues = ContentValues().apply {
      put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
      put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
      put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
      put(MediaStore.Video.Media.IS_PENDING, 1)
    }

    val contentResolver = context.contentResolver

    val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
      ?: return@withContext null
    val fos = contentResolver.openOutputStream(imageUri) ?: return@withContext null

    fos.use { image.asAndroidBitmap().compress(CompressFormat.JPEG, 75, it) }

    contentValues.clear()
    contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
    contentResolver.update(imageUri, contentValues, null, null)

    return@withContext imageUri
  }

  private fun String.fileSafeName(): String = lowercase().replace(Regex("[^a-zA-Z0-9]"), "_")
}
