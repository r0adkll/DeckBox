package app.deckbox.features.decks.public.ui.slices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.DeckCardSlice
import app.deckbox.features.decks.public.ui.events.DeckCardEvent
import cafe.adriel.lyricist.LocalStrings

class ActionSlice : ComposeSlice {
  override val config: DeckCardSlice = DeckCardSlice.Actions.Full

  @Composable
  override fun ColumnScope.Content(deck: Deck, eventSink: (DeckCardEvent) -> Unit) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(
          horizontal = SlicePaddingHorizontal,
          vertical = 16.dp,
        ),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {

      OutlinedButton(
        onClick = { eventSink(DeckCardEvent.Test) },
      ) {
        Text(LocalStrings.current.deckActionTestButton)
      }

      OutlinedButton(
        onClick = { eventSink(DeckCardEvent.Duplicate) },
      ) {
        Text(LocalStrings.current.deckActionDuplicateButton)
      }

      FilledTonalButton(
        onClick = { eventSink(DeckCardEvent.Delete) },
      ) {
        Icon(Icons.Rounded.Delete, contentDescription = null)
        Text(LocalStrings.current.deckActionDeleteButton)
      }
    }
  }
}
