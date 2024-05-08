package app.deckbox.features.cards.ui

import androidx.compose.runtime.Stable
import app.deckbox.common.compose.message.UiMessage
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.model.BoosterPack
import app.deckbox.core.model.Card
import app.deckbox.core.model.CollectionCount
import app.deckbox.core.model.Deck
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class CardDetailUiState(
  val cardName: String,
  val cardImageUrl: String?,
  val card: LoadState<out Card>,
  val deckState: DeckState?,
  val collectionCount: CollectionCount,
  val isFavorited: Boolean,
  val evolvesFrom: LoadState<out List<Card>>,
  val evolvesTo: LoadState<out List<Card>>,
  val similar: LoadState<out List<Card>>,
  val uiMessage: UiMessage? = null,
  val eventSink: (CardDetailUiEvent) -> Unit,
) : CircuitUiState

@Stable
data class DeckState(
  val count: Int,
)

sealed interface CardDetailUiEvent : CircuitUiEvent {
  data object NavigateBack : CardDetailUiEvent
  data object NewBoosterPack : CardDetailUiEvent
  data object NewDeck : CardDetailUiEvent

  data class ClearUiMessage(val id: Long) : CardDetailUiEvent
  data class OpenUrl(val url: String) : CardDetailUiEvent
  data class Favorite(val value: Boolean) : CardDetailUiEvent
  data class CardClick(val card: Card) : CardDetailUiEvent
  data class AddToDeck(val deck: Deck) : CardDetailUiEvent
  data class AddToBoosterPack(val boosterPack: BoosterPack) : CardDetailUiEvent

  data object IncrementCount : CardDetailUiEvent
  data object DecrementCount : CardDetailUiEvent
  data class IncrementCollectionCount(
    val variant: Card.Variant,
  ) : CardDetailUiEvent

  data class DecrementCollectionCount(
    val variant: Card.Variant,
  ) : CardDetailUiEvent
}

val CardDetailUiState.pokemonCard: Card?
  get() = (card as? LoadState.Loaded)?.data
