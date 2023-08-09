package app.deckbox.features.decks.public.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import app.deckbox.features.decks.public.ui.slices.ComposeSlice

private val DeckCardCornerRadius = 16.dp

@Composable
fun DeckCard(
  slices: List<ComposeSlice>,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val shape = RoundedCornerShape(DeckCardCornerRadius)
  Card(
    shape = shape,
    modifier = modifier
      .clickable(
        role = Role.Button,
        onClick = onClick,
      )
      .clip(shape),
  ) {
    slices.forEach { slice ->
      with (slice) {
        Content()
      }
    }
  }
}
