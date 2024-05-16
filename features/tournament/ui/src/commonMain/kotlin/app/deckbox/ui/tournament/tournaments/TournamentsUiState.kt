package app.deckbox.ui.tournament.tournaments

import androidx.compose.runtime.Stable
import app.deckbox.core.coroutines.LoadState
import app.deckbox.tournament.api.model.Tournament
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class TournamentsUiState(
  val tournaments: LoadState<out List<Tournament>>,
  val eventSink: (TournamentsUiEvent) -> Unit,
) : CircuitUiState

sealed interface TournamentsUiEvent : CircuitUiEvent {
  data object NavigateBack : TournamentsUiEvent
  data object Refresh : TournamentsUiEvent
  data class TournamentClick(val tournament: Tournament) : TournamentsUiEvent
}
