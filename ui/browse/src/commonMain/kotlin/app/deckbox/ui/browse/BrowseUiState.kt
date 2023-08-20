package app.deckbox.ui.browse

import androidx.compose.runtime.Stable
import app.cash.paging.Pager
import app.deckbox.core.model.Card
import app.deckbox.ui.filter.FilterUiState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class BrowseUiState(
  val query: String? = null,
  val filterUiState: FilterUiState,
  val cardsPager: Pager<Int, Card>,
  val eventSink: (BrowseUiEvent) -> Unit,
) : CircuitUiState

sealed interface BrowseUiEvent : CircuitUiEvent {
  class CardClicked(val card: Card) : BrowseUiEvent
  data class SearchUpdated(val query: String?) : BrowseUiEvent
  object SearchCleared : BrowseUiEvent
}
