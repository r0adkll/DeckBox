package app.deckbox.ui.expansions.list

import androidx.compose.runtime.Stable
import app.deckbox.core.model.Expansion
import app.deckbox.core.settings.ExpansionCardStyle
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

typealias Series = String

@Stable
data class ExpansionsUiState(
  val expansionState: ExpansionState,
  val expansionCardStyle: ExpansionCardStyle,
  val query: String?,
  val eventSink: (ExpansionsUiEvent) -> Unit,
) : CircuitUiState

@Stable
sealed interface ExpansionState {
  object Loading : ExpansionState
  data class Loaded(
    val groupedExpansions: List<ExpansionSeries>,
  ) : ExpansionState
  data class Error(val message: String) : ExpansionState
}

@Stable
data class ExpansionSeries(
  val series: Series,
  val expansions: List<Expansion>,
)

sealed interface ExpansionsUiEvent : CircuitUiEvent {
  data class ChangeCardStyle(val style: ExpansionCardStyle) : ExpansionsUiEvent
  data class ExpansionClicked(val expansion: Expansion) : ExpansionsUiEvent
  data class SearchUpdated(val query: String?) : ExpansionsUiEvent
  object SearchCleared : ExpansionsUiEvent
}
