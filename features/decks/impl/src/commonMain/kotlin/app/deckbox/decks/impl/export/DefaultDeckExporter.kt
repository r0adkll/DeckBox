package app.deckbox.decks.impl.export

import androidx.compose.ui.graphics.ImageBitmap
import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.Deck
import app.deckbox.features.decks.api.export.DeckExporter
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@ContributesBinding(MergeAppScope::class)
@Inject
class DefaultDeckExporter(
  private val textConverter: TextConverter,
  private val imageConverter: ImageConverter,
  private val dispatcherProvider: DispatcherProvider,
) : DeckExporter {

  override suspend fun exportAsText(deck: Deck): String = withContext(dispatcherProvider.computation) {
    textConverter.convert(deck)
  }

  override suspend fun exportAsImage(deck: Deck): ImageBitmap = withContext(dispatcherProvider.computation) {
    imageConverter.convert(deck)
  }
}
