package app.deckbox.features.cards.ui

import androidx.compose.runtime.Stable
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.model.Card
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class CardDetailUiState(
  val cardName: String,
  val cardImageUrl: String,
  val card: LoadState<out Card>,
  val deckState: DeckState?,
  val evolvesFrom: List<Card> = emptyList(),
  val evolvesTo: List<Card> = emptyList(),
  val similar: List<Card> = emptyList(),
  val eventSink: (CardDetailUiEvent) -> Unit,
) : CircuitUiState

@Stable
data class DeckState(
  val count: Int,
)

sealed interface CardDetailUiEvent : CircuitUiEvent {
  data object NavigateBack : CardDetailUiEvent
  data class OpenUrl(val url: String) : CardDetailUiEvent

  data object IncrementCount : CardDetailUiEvent
  data object DecrementCount : CardDetailUiEvent
}

val CardDetailUiState.pokemonCard: Card?
  get() = (card as? LoadState.Loaded)?.data
