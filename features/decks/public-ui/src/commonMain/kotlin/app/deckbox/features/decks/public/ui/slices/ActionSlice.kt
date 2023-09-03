package app.deckbox.features.decks.public.ui.slices

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import app.deckbox.common.compose.icons.rounded.Duplicate
import app.deckbox.common.compose.icons.rounded.Experiment
import app.deckbox.common.compose.widgets.DeleteConfirmation
import app.deckbox.common.compose.widgets.OutlinedIconButton
import app.deckbox.common.compose.widgets.SizedIcon
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.DeckCardSlice
import app.deckbox.features.decks.public.ui.LocalDeckInteractionSource
import app.deckbox.features.decks.public.ui.events.DeckCardEvent
import cafe.adriel.lyricist.LocalStrings

class ActionSlice : ComposeSlice {
  override val config: DeckCardSlice = DeckCardSlice.Actions.Full

  @Composable
  override fun ColumnScope.Content(deck: Deck, eventSink: (DeckCardEvent) -> Unit) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    val interactionSource = LocalDeckInteractionSource.current

    val isDragged by interactionSource.collectIsDraggedAsState()
    LaunchedEffect(isDragged, showDeleteConfirmation) {
      if (showDeleteConfirmation && isDragged) {
        showDeleteConfirmation = false
      }
    }

    Box {
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
        OutlinedIconButton(
          onClick = { eventSink(DeckCardEvent.Test) },
          colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
          ),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
          icon = {
            SizedIcon(
              Icons.Rounded.Experiment,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
            )
          },
          label = { Text(LocalStrings.current.deckActionTestButton) },
        )

        Spacer(Modifier.weight(1f))

        IconButton(
          onClick = { eventSink(DeckCardEvent.Duplicate) },
        ) {
          Icon(
            Icons.Rounded.Duplicate,
            contentDescription = LocalStrings.current.deckActionDuplicateButtonContentDescription,
          )
        }

        IconButton(
          onClick = { showDeleteConfirmation = true },
        ) {
          Icon(Icons.Rounded.Delete, contentDescription = LocalStrings.current.deckActionDeleteButtonContentDescription)
        }
      }

      DeleteConfirmation(
        visible = showDeleteConfirmation,
        onDelete = { eventSink(DeckCardEvent.Delete) },
        onCancel = { showDeleteConfirmation = false },
        modifier = Modifier.zIndex(1f),
      )
    }
  }
}
