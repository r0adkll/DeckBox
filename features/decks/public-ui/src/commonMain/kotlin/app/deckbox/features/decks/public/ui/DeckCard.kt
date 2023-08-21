package app.deckbox.features.decks.public.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.DeckCardConfig
import app.deckbox.features.decks.public.ui.events.DeckCardEvent
import app.deckbox.features.decks.public.ui.slices.SliceRegistry

private val DeckCardCornerRadius = 16.dp

internal val LocalDeckInteractionSource = staticCompositionLocalOf<InteractionSource> {
  MutableInteractionSource()
}

@Composable
fun DeckCard(
  deck: Deck,
  config: DeckCardConfig,
  onEvent: (DeckCardEvent) -> Unit,
  modifier: Modifier = Modifier,
  interactionSource: InteractionSource = remember { MutableInteractionSource() },
) {
  CompositionLocalProvider(
    LocalDeckInteractionSource provides interactionSource,
  ) {
    DeckCard(
      onClick = { onEvent(DeckCardEvent.Clicked) },
      modifier = modifier,
    ) {
      config.slices.forEach { sliceConfig ->
        val composeSlice = SliceRegistry.slice(sliceConfig)
        with(composeSlice) {
          Content(deck, onEvent)
        }
      }
    }
  }
}

@Composable
private fun DeckCard(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable ColumnScope.() -> Unit,
) {
  val shape = RoundedCornerShape(DeckCardCornerRadius)
  Card(
    shape = shape,
    modifier = modifier
      .clip(shape)
      .clickable(
        role = Role.Button,
        onClick = onClick,
      ),
  ) {
    content()
  }
}
