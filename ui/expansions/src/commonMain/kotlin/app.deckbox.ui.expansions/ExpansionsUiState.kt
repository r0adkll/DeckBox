package app.deckbox.ui.expansions

import androidx.compose.runtime.Stable
import app.deckbox.core.model.Deck
import app.deckbox.core.model.Expansion
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class ExpansionsUiState(
  val isLoading: Boolean = false,
  val expansions: List<Expansion> = emptyList(),
  val eventSink: (ExpansionsUiEvent) -> Unit
) : CircuitUiState

sealed interface ExpansionsUiEvent : CircuitUiEvent {
}
