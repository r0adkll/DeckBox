package app.deckbox.ui.filter.spec

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.deckbox.core.model.Expansion
import app.deckbox.core.model.Format
import app.deckbox.core.model.Legality
import app.deckbox.core.model.SearchFilter
import app.deckbox.ui.filter.FilterUiEvent
import app.deckbox.ui.filter.FilterUiEvent.*
import app.deckbox.ui.filter.FilterUiState
import app.deckbox.ui.filter.spec.ExpansionsFilterAction.*
import app.deckbox.ui.filter.widgets.Chip
import com.seiko.imageloader.rememberImagePainter

sealed interface ExpansionsFilterAction : FilterAction {

  object AllStandard : ExpansionsFilterAction {
    override fun applyToFilter(
      expansions: List<Expansion>,
      filter: SearchFilter
    ): SearchFilter {
      return filter.copy(
        expansions = expansions
          .filter { it.legalities?.standard == Legality.LEGAL }
          .map { it.id }
          .toSet()
      )
    }
  }

  object AllExpanded : ExpansionsFilterAction {
    override fun applyToFilter(
      expansions: List<Expansion>,
      filter: SearchFilter
    ): SearchFilter {
      return filter.copy(
        expansions = expansions
          .filter { it.legalities?.expanded == Legality.LEGAL }
          .map { it.id }
          .toSet()
      )
    }
  }

  data class DeselectFormat(val format: Format) : ExpansionsFilterAction {
    override fun applyToFilter(
      expansions: List<Expansion>,
      filter: SearchFilter
    ): SearchFilter {
      return filter.copy(
        expansions = filter.expansions.filter { expansionId ->
          val expansion = expansions.find { it.id == expansionId }
          when (format) {
            Format.STANDARD -> expansion?.legalities?.standard != Legality.LEGAL
            Format.EXPANDED -> expansion?.legalities?.expanded != Legality.LEGAL
            else -> true
          }
        }.toSet()
      )
    }
  }

  data class AddExpansion(val expansion: Expansion) : ExpansionsFilterAction {
    override fun applyToFilter(
      expansions: List<Expansion>,
      filter: SearchFilter
    ): SearchFilter {
      return filter.copy(
        expansions = filter.expansions.plus(expansion.id)
      )
    }
  }

  data class RemoveExpansion(val expansion: Expansion) : ExpansionsFilterAction {
    override fun applyToFilter(
      expansions: List<Expansion>,
      filter: SearchFilter
    ): SearchFilter {
      return filter.copy(
        expansions = filter.expansions.minus(expansion.id)
      )
    }
  }
}

object ExpansionsFilterSpec : FilterSpec() {
  override val title: String = "Expansions"

  @OptIn(ExperimentalMaterial3Api::class)
  override fun LazyListScope.buildContent(
    uiState: FilterUiState,
    actionEmitter: (FilterUiEvent) -> Unit
  ) {
    // Filter
    item {
      val allStandardSelected = uiState.expansions
        .filter { it.legalities?.standard == Legality.LEGAL }
        .all { uiState.filter.expansions.contains(it.id) }

      val allExpandedSelected = uiState.expansions
        .filter { it.legalities?.expanded == Legality.LEGAL }
        .all { uiState.filter.expansions.contains(it.id) }

      Row {
        Chip(
          modifier = Modifier.padding(start = 16.dp, end = 8.dp),
          isSelected = allStandardSelected,
          content = {
            Text("Standard")
          },
          onClick = {
            if (allStandardSelected) {
              actionEmitter(FilterChange(DeselectFormat(Format.STANDARD)))
            } else {
              actionEmitter(FilterChange(AllStandard))
              actionEmitter(ChangeVisibleExpansions(Format.STANDARD))
            }
          }
        )
        Chip(
          modifier = Modifier.padding(start = 8.dp, end = 16.dp),
          isSelected = allExpandedSelected,
          content = {
            Text("Expanded")
          },
          onClick = {
            if (allExpandedSelected) {
              actionEmitter(FilterChange(DeselectFormat(Format.EXPANDED)))
            } else {
              actionEmitter(FilterChange(AllExpanded))
              actionEmitter(ChangeVisibleExpansions(Format.EXPANDED))
            }
          }
        )
      }
    }

    // Expansion items
    val visibleExpansions = uiState.expansions
      .filter {
        when (uiState.visibleExpansionFormat) {
          Format.STANDARD -> it.legalities?.standard == Legality.LEGAL
          Format.EXPANDED -> it.legalities?.expanded == Legality.LEGAL
          else -> true
        }
      }
      .sortedByDescending { it.releaseDate }

    items(
      items = visibleExpansions,
      key = { it.id },
    ) { expansion ->
      val isSelected = uiState.filter.expansions.contains(expansion.id)
      ExpansionFilterItem(
        expansion = expansion,
        isSelected = isSelected,
        onCheckedChange = { checked ->
          if (checked) {
            actionEmitter(FilterChange(AddExpansion(expansion)))
          } else {
            actionEmitter(FilterChange(RemoveExpansion(expansion)))
          }
        },
        onClick = {
          if (isSelected) {
            actionEmitter(FilterChange(RemoveExpansion(expansion)))
          } else {
            actionEmitter(FilterChange(AddExpansion(expansion)))
          }
        }
      )
    }

    if (uiState.visibleExpansionFormat == Format.STANDARD ||
      uiState.visibleExpansionFormat == Format.EXPANDED) {
      item {
        ListItem(
          colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
          ),
          modifier = Modifier.clickable {
            actionEmitter(
              ChangeVisibleExpansions(
                when (uiState.visibleExpansionFormat) {
                  Format.STANDARD -> Format.EXPANDED
                  else -> Format.UNLIMITED
                }
              )
            )
          },
          headlineText = {
            Text(
              text = when (uiState.visibleExpansionFormat) {
                Format.STANDARD -> "View Expanded"
                else -> "View all"
              },
              style = MaterialTheme.typography.labelLarge,
            )
          }
        )
      }
    }
  }

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  private fun ExpansionFilterItem(
    expansion: Expansion,
    isSelected: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
  ) {
    ListItem(
      modifier = modifier.clickable(onClick = onClick),
      colors = ListItemDefaults.colors(
        containerColor = Color.Transparent,
      ),
      leadingContent = {
        val painter = key(expansion.images.symbol) {
          rememberImagePainter(expansion.images.symbol)
        }
        Image(
          painter = painter,
          contentDescription = null,
          modifier = Modifier.size(24.dp),
        )
      },
      headlineText = {
        Text(expansion.name)
      },
      trailingContent = {
        Switch(
          checked = isSelected,
          onCheckedChange = {
            onCheckedChange(it)
          },
        )
      },
    )
  }
}
