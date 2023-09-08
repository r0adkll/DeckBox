package app.deckbox.ui.browse

import androidx.compose.runtime.Stable
import app.cash.paging.Pager
import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import app.deckbox.core.settings.PokemonGridStyle
import app.deckbox.ui.filter.FilterUiState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class BrowseUiState(
  val isEditing: Boolean,
  val query: String?,
  val filterUiState: FilterUiState,
  val cardsPager: Pager<Int, Card>,
  val countState: Map<String, Stacked<Card>>?,
  val cardGridStyle: PokemonGridStyle,
  val eventSink: (BrowseUiEvent) -> Unit,
) : CircuitUiState

sealed interface BrowseUiEvent : CircuitUiEvent {
  data class CardClicked(val card: Card) : BrowseUiEvent
  data class CardLongClicked(val card: Card) : BrowseUiEvent
  data class GridStyleChanged(val style: PokemonGridStyle) : BrowseUiEvent
  data class SearchUpdated(val query: String?) : BrowseUiEvent
  data object SearchCleared : BrowseUiEvent
  data object NavigateBack : BrowseUiEvent
}
