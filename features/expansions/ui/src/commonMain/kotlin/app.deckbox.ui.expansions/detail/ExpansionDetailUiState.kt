package app.deckbox.ui.expansions.detail

import androidx.compose.runtime.Stable
import app.cash.paging.Pager
import app.deckbox.core.model.Card
import app.deckbox.core.model.Expansion
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
sealed interface ExpansionDetailUiState : CircuitUiState {
  object Loading : ExpansionDetailUiState
  data class Loaded(
    val expansion: Expansion,
    val cardsPager: Pager<Int, Card>,
    val eventSink: (ExpansionDetailUiEvent) -> Unit,
  ) : ExpansionDetailUiState
}

sealed interface ExpansionDetailUiEvent : CircuitUiEvent {
  object NavigateBack : ExpansionDetailUiEvent
}
