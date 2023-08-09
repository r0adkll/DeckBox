package app.deckbox.features.decks.public.ui.slices

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImportExport
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import app.deckbox.common.compose.widgets.CardHeader
import app.deckbox.core.model.Deck
import cafe.adriel.lyricist.LocalStrings

class HeaderSlice(
  private val deck: Deck,
  private val onExport: () -> Unit,
) : ComposeSlice {
  override val name: String = "HeaderSlice(${deck.id})"

  @Composable
  override fun ColumnScope.Content() {
    CardHeader(
      title = { Text(deck.name.ifBlank { LocalStrings.current.deckDefaultNoName }) },
      subtitle = {
        Text(LocalStrings.current.deckLastUpdated(deck.updatedAt.toString()))
      },
      trailing = {
        IconButton(
          onClick = onExport,
        ) {
          Icon(Icons.Rounded.Share, contentDescription = null)
        }
      }
    )
  }
}
