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
import cafe.adriel.lyricist.LocalStrings

class ActionSlice(
  private val onTest: () -> Unit,
  private val onDuplicate: () -> Unit,
  private val onDelete: () -> Unit,
) : ComposeSlice {
  override val name: String = "ActionSlice"

  @Composable
  override fun ColumnScope.Content() {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {

      OutlinedButton(
        onClick = onTest,
      ) {
        Text(LocalStrings.current.deckActionTestButton)
      }

      OutlinedButton(
        onClick = onDuplicate,
      ) {
        Text(LocalStrings.current.deckActionDuplicateButton)
      }

      FilledTonalButton(
        onClick = onDelete,
      ) {
        Icon(Icons.Rounded.Delete, contentDescription = null)
        Text(LocalStrings.current.deckActionDeleteButton)
      }
    }
  }
}
