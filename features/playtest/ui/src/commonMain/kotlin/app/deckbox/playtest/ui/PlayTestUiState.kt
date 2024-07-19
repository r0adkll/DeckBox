package app.deckbox.playtest.ui

import androidx.compose.runtime.Immutable
import app.deckbox.core.model.Card
import app.deckbox.features.decks.api.validation.DeckValidation
import app.deckbox.playtest.api.model.Action
import app.deckbox.playtest.api.model.Board
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed class PlayTestUiState : CircuitUiState {
  data object Loading : PlayTestUiState()

  data class InGame(
    val board: Board,
    val eventSink: (PlayTestUiEvent) -> Unit,
  ) : PlayTestUiState()

  data class Error(
    val validation: DeckValidation,
  ) : PlayTestUiState()
}

@Immutable
sealed interface DeckState {
  data object Loading : DeckState
  data class Loaded(val cards: ImmutableList<Card>) : DeckState
  data class Error(val validation: DeckValidation) : DeckState
}

sealed interface PlayTestUiEvent {
  data class BoardAction(val action: Action) : PlayTestUiEvent
}
