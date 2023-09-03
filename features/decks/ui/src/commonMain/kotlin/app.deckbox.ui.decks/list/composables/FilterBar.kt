package app.deckbox.ui.decks.list.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.deckbox.common.compose.widgets.SortChip
import app.deckbox.core.settings.SortOption

@Composable
internal fun FilterBar(
  option: SortOption,
  onOptionClick: (SortOption) -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier.fillMaxWidth(),
  ) {
    Spacer(Modifier.weight(1f))

    SortChip(
      value = option,
      sortOptions = SortOption.values().toList(),
      onValueChanged = onOptionClick,
    )
  }
}
