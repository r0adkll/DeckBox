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
import app.deckbox.common.compose.widgets.CardAspectRatio
import app.deckbox.core.model.Deck
import com.seiko.imageloader.rememberImagePainter

class FannedImageSlice(
  val deck: Deck,
) : ComposeSlice {
  override val name: String = "FannedImageSlice"

  @Composable
  override fun ColumnScope.Content() {
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

class GridImageSlice(
  val deck: Deck,
) : ComposeSlice {
  override val name: String = "GridImageSlice"

  @Composable
  override fun ColumnScope.Content() {
    TODO("Not yet implemented")
  }
}
