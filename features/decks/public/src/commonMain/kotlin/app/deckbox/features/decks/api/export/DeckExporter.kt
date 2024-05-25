package app.deckbox.features.decks.api.export

import androidx.compose.ui.graphics.ImageBitmap
import app.deckbox.core.model.Deck

interface DeckExporter {

  suspend fun exportAsText(deck: Deck): String
  suspend fun exportAsImage(deck: Deck): ImageBitmap
}
