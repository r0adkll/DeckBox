package app.deckbox.ui.browse

import androidx.compose.runtime.Stable
import app.deckbox.core.model.Card
import app.deckbox.core.model.SearchFilter
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class BrowseUiState(
  val isLoading: Boolean = false,
  val results: List<Card> = emptyList(),
  val filter: SearchFilter? = null,
  val query: String? = null,
  val eventSink: (BrowseUiEvent) -> Unit,
) : CircuitUiState

sealed interface BrowseUiEvent : CircuitUiEvent {
  class Search(val query: String?) : BrowseUiEvent
  class Filter(val filter: SearchFilter) : BrowseUiEvent
}
