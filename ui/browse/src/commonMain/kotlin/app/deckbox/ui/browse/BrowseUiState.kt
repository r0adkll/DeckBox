package app.deckbox.ui.browse

import androidx.compose.runtime.Stable
import app.cash.paging.Pager
import app.deckbox.core.model.Card
import app.deckbox.core.model.SearchFilter
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class BrowseUiState(
  val query: String? = null,
  val filter: SearchFilter? = null,
  val cardsPager: Pager<Int, Card>,
  val eventSink: (BrowseUiEvent) -> Unit,
) : CircuitUiState

sealed interface BrowseUiEvent : CircuitUiEvent {
  class Filter(val filter: SearchFilter) : BrowseUiEvent
  data class SearchUpdated(val query: String?) : BrowseUiEvent
  object SearchCleared : BrowseUiEvent
}
