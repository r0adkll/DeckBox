package app.deckbox.ui.expansions

import androidx.compose.runtime.Stable
import app.deckbox.core.model.Expansion
import app.deckbox.expansions.ui.ExpansionCardStyle
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class ExpansionsUiState(
  val loadState: ExpansionsLoadState,
  val expansionCardStyle: ExpansionCardStyle,
  val query: String?,
  val eventSink: (ExpansionsUiEvent) -> Unit,
) : CircuitUiState

sealed interface ExpansionsUiEvent : CircuitUiEvent {
  data class ChangeCardStyle(val style: ExpansionCardStyle) : ExpansionsUiEvent
  data class ExpansionClicked(val expansion: Expansion) : ExpansionsUiEvent
  data class SearchUpdated(val query: String?) : ExpansionsUiEvent
  object SearchCleared : ExpansionsUiEvent
}
