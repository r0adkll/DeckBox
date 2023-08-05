package app.deckbox.features.cards.ui

import androidx.compose.runtime.Stable
import app.deckbox.core.model.Card
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

sealed interface CardDetailUiState : CircuitUiState {
  object Loading : CardDetailUiState

  @Stable
  data class Error(val message: String) : CardDetailUiState

  @Stable
  data class Loaded(
    val card: Card,
    val evolvesFrom: List<Card> = emptyList(),
    val evolvesTo: List<Card> = emptyList(),
    val similar: List<Card> = emptyList(),
    val eventSink: (CardDetailUiEvent) -> Unit,
  ) : CardDetailUiState
}

sealed interface CardDetailUiEvent : CircuitUiEvent {
  object NavigateBack : CardDetailUiEvent
}
