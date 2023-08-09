package app.deckbox.features.decks.public.ui.slices

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

class DescriptionSlice(
  private val description: String,
) : ComposeSlice {
  override val name: String = "DescriptionSlice"

  @Composable
  override fun ColumnScope.Content() {
    Text(
      text = description,
      style = MaterialTheme.typography.bodyMedium,
      maxLines = 2,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
    )
  }
}
