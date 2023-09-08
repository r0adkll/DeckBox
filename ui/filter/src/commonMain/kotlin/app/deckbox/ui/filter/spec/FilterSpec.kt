package app.deckbox.ui.filter.spec

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.deckbox.core.model.Expansion
import app.deckbox.core.model.SearchFilter
import app.deckbox.ui.filter.FilterUiEvent
import app.deckbox.ui.filter.FilterUiState

abstract class FilterSpec {

  /**
   * The section header
   */
  abstract val title: String

  /**
   * Emit the UI necessary for this filter spec based on the passed
   * [FilterUiState].
   */
  protected abstract fun LazyListScope.buildContent(
    uiState: FilterUiState,
    actionEmitter: (FilterUiEvent) -> Unit,
  )

  /**
   * Emit this [FilterSpec] content into a LazyList context that is sectioned
   * according to the defined [titleResId]
   */
  fun LazyListScope.content(
    uiState: FilterUiState,
    showDivider: Boolean = false,
    actionEmitter: (FilterUiEvent) -> Unit,
  ) {
    sectioned(title, showDivider = showDivider) {
      buildContent(uiState, actionEmitter)
    }
  }

  private fun LazyListScope.sectioned(
    header: String,
    showDivider: Boolean = false,
    block: LazyListScope.() -> Unit,
  ) {
    if (showDivider) item { Divider() }
    item {
      Text(
        text = header,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(16.dp),
      )
    }
    block()
  }
}

/**
 * Represents an action that a FilterSpec can apply to the Ui State
 */
interface FilterAction {

  /**
   * Apply this action to the current filter state, returning the new updated
   * filter as a result
   */
  fun applyToFilter(
    expansions: List<Expansion>,
    filter: SearchFilter,
  ): SearchFilter = filter
}
