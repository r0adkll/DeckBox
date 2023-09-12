package app.deckbox.playtest.ui

import com.slack.circuit.runtime.CircuitUiState

data class PlayTestUiState(
  val eventSink: (PlayTestUiEvent) -> Unit,
) : CircuitUiState

sealed interface GameState {
  data object Loading : GameState
  data class Error(val reason: GameErrors) : GameState
}

enum class GameErrors {
  INVALID_DECK,
  UNKNOWN,
}

sealed interface PlayTestUiEvent
