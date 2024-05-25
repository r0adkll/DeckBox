package app.deckbox.sharing

import androidx.compose.ui.graphics.asSkiaBitmap
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.logging.bark
import app.deckbox.core.model.Deck
import app.deckbox.core.model.DeckShareAction
import app.deckbox.core.model.ShareAction
import app.deckbox.features.decks.api.export.DeckExporter
import app.deckbox.sharing.api.ShareManager
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import me.tatarka.inject.annotations.Inject
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIImage
import platform.UIKit.UIViewController

@ContributesBinding(MergeActivityScope::class)
@Inject
class PlatformShareManager(
  private val uiViewController: UIViewController,
  private val deckExporter: DeckExporter,
) : ShareManager {

  override suspend fun share(shareAction: ShareAction) {
    when (shareAction) {
      is DeckShareAction -> shareDeck(shareAction)
    }
  }

  private suspend fun shareDeck(deckShareAction: DeckShareAction) {
    when (deckShareAction.type) {
      DeckShareAction.ExportType.Text -> shareDeckText(deckShareAction.deck)
      DeckShareAction.ExportType.Image -> shareDeckImage(deckShareAction.deck)
    }
  }

  private suspend fun shareDeckText(deck: Deck) {
    val text = deckExporter.exportAsText(deck)

    val ac = UIActivityViewController(activityItems = listOf(text), applicationActivities = null)
    uiViewController.presentViewController(ac, animated = true, completion = null)
  }

  @OptIn(ExperimentalForeignApi::class)
  private suspend fun shareDeckImage(deck: Deck) {
    val image = deckExporter.exportAsImage(deck).asSkiaBitmap()
    val skiaImage = Image.makeFromBitmap(image)
    val uiImage = skiaImage.encodeToData()?.let {
      UIImage(data = it.bytes.toData())
    }

    if (uiImage != null) {
      val ac = UIActivityViewController(activityItems = listOf(uiImage), applicationActivities = null)
      uiViewController.presentViewController(ac, animated = true, completion = null)
    } else {
      bark { "Unable to convert export image to UIImage to share" }
    }
  }

  @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
  private fun ByteArray.toData(): NSData = memScoped {
    NSData.create(
      bytes = allocArrayOf(this@toData),
      length = this@toData.size.toULong(),
    )
  }
}
