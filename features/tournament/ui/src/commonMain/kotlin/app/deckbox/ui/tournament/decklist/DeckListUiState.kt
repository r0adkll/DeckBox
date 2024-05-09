package app.deckbox.ui.tournament.decklist

import androidx.compose.runtime.Stable
import app.deckbox.common.compose.widgets.builder.model.CardUiModel
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.model.Card
import app.deckbox.core.model.Deck
import app.deckbox.tournament.api.model.DeckList
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList

@Stable
data class DeckListUiState(
  val archetypeName: String,
  val deckListState: LoadState<out DeckList>,
  val cardsLoadState: LoadState<out ImmutableList<CardUiModel>>,
  val importEnabled: Boolean,
  val importState: LoadState<out Deck>?,
  val eventSink: (DeckListUiEvent) -> Unit,
) : CircuitUiState

sealed interface DeckListUiEvent : CircuitUiEvent {
  data object NavigateBack : DeckListUiEvent
  data object Import : DeckListUiEvent
  data class CardClick(val card: Card) : DeckListUiEvent
  data class PurchaseDeck(val bulkPurchaseUrl: String) : DeckListUiEvent
}
