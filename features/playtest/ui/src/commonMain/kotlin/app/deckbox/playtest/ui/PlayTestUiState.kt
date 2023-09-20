package app.deckbox.playtest.ui

import androidx.compose.runtime.Immutable
import app.deckbox.core.model.Card
import app.deckbox.features.decks.api.validation.DeckValidation
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class PlayTestUiState(
  val deckState: DeckState,
  val eventSink: (PlayTestUiEvent) -> Unit,
) : CircuitUiState

@Immutable
sealed interface DeckState {
  data object Loading : DeckState
  data class Loaded(val cards: ImmutableList<Card>) : DeckState
  data class Error(val validation: DeckValidation) : DeckState
}

sealed interface PlayTestUiEvent
