package app.deckbox.features.decks.public.ui.slices

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.DeckCardSlice
import app.deckbox.features.decks.public.ui.events.DeckCardEvent
import com.seiko.imageloader.rememberImagePainter

class FannedImageSlice : ComposeSlice {
  override val config: DeckCardSlice = DeckCardSlice.Images.Fanned

  @Composable
  override fun ColumnScope.Content(
    deck: Deck,
    eventSink: (DeckCardEvent) -> Unit,
  ) {
    Row(
      modifier = Modifier
        .padding(
          horizontal = 16.dp,
          vertical = 4.dp,
        )
        .clip(RoundedCornerShape(16.dp))
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      deck.cardImages.forEach { url ->
        val painter = key(url) { rememberImagePainter(url) }

        val width = 90.dp
        val height = 150.dp
        Image(
          painter = painter,
          contentDescription = null,
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(16.dp)),
        )
      }
    }
  }
}

class GridImageSlice : ComposeSlice {
  override val config: DeckCardSlice = DeckCardSlice.Images.Grid

  @Composable
  override fun ColumnScope.Content(
    deck: Deck,
    eventSink: (DeckCardEvent) -> Unit,
  ) {
    TODO("Not yet implemented")
  }
}
