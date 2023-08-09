package app.deckbox.features.decks.public.ui.slices

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.widgets.Tag
import app.deckbox.common.compose.widgets.TagGroup
import app.deckbox.common.compose.widgets.Tags

class TagSlice(
  private val tags: List<Tag>,
) : ComposeSlice {
  override val name: String = "TagSlice"

  @Composable
  override fun ColumnScope.Content() {
    TagGroup(
      tags = tags,
      modifier = Modifier
        .fillMaxWidth()
        .padding(
          horizontal = 16.dp,
          vertical = 8.dp,
        )
    )
  }
}
