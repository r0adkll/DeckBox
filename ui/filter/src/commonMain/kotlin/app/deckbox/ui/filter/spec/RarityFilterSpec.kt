package app.deckbox.ui.filter.spec

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import app.deckbox.core.model.Expansion
import app.deckbox.core.model.SearchFilter
import app.deckbox.ui.filter.FilterUiEvent
import app.deckbox.ui.filter.FilterUiEvent.FilterChange
import app.deckbox.ui.filter.FilterUiState
import app.deckbox.ui.filter.spec.RarityFilterAction.AddRarity
import app.deckbox.ui.filter.spec.RarityFilterAction.RemoveRarity

sealed interface RarityFilterAction : FilterAction {

  data class AddRarity(val rarity: String) : RarityFilterAction {
    override fun applyToFilter(
      expansions: List<Expansion>,
      filter: SearchFilter,
    ): SearchFilter {
      return filter.copy(
        rarity = filter.rarity.plus(rarity),
      )
    }
  }

  data class RemoveRarity(val rarity: String) : RarityFilterAction {
    override fun applyToFilter(
      expansions: List<Expansion>,
      filter: SearchFilter,
    ): SearchFilter {
      return filter.copy(
        rarity = filter.rarity.minus(rarity),
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
object RarityFilterSpec : FilterSpec() {

  override val title: String = "Rarity"

  override fun LazyListScope.buildContent(
    uiState: FilterUiState,
    actionEmitter: (FilterUiEvent) -> Unit,
  ) {
    items(uiState.rarities) { rarity ->
      val isSelected = uiState.filter.rarity.contains(rarity)

      val clickListener = { isChecked: Boolean ->
        if (isChecked) {
          actionEmitter(FilterChange(AddRarity(rarity)))
        } else {
          actionEmitter(FilterChange(RemoveRarity(rarity)))
        }
      }

      ListItem(
        modifier = Modifier.clickable {
          clickListener(!isSelected)
        },
        colors = ListItemDefaults.colors(
          containerColor = Color.Transparent,
        ),
        headlineContent = { Text(rarity) },
        trailingContent = {
          Switch(
            checked = isSelected,
            onCheckedChange = { isChecked ->
              clickListener(isChecked)
            },
          )
        },
      )
    }
  }
}
