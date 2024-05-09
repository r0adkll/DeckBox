package app.deckbox.ui.tournament.tournamentdetail

import androidx.compose.runtime.Stable
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.model.Format
import app.deckbox.tournament.api.model.Participant
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class TournamentDetailUiState(
  val tournamentName: String,
  val tournamentFormat: Format,
  val participants: LoadState<out List<Participant>>,
  val eventSink: (TournamentDetailUiEvent) -> Unit,
) : CircuitUiState

sealed interface TournamentDetailUiEvent : CircuitUiEvent {
  data object NavigateBack : TournamentDetailUiEvent
  data class ParticipantClick(val participant: Participant) : TournamentDetailUiEvent
}
