package app.deckbox.ui.decks.builder

import androidx.compose.runtime.Stable
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.model.BoosterPack
import app.deckbox.core.model.Card
import app.deckbox.core.model.Deck
import app.deckbox.core.model.SuperType
import app.deckbox.features.decks.api.validation.DeckValidation
import app.deckbox.ui.decks.builder.model.CardUiModel
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList

@Stable
data class DeckBuilderUiState(
  val session: DeckSession,
  val cards: LoadState<out ImmutableList<CardUiModel>>,
  val validation: LoadState<out DeckValidation>,
  val price: LoadState<out DeckPriceState>,
  val eventSink: (DeckBuilderUiEvent) -> Unit,
) : CircuitUiState

@Stable
data class DeckPriceState(
  val tcgPlayer: DeckPrice? = null,
  val cardMarket: DeckPrice? = null,
)

@Stable
data class DeckPrice(
  val lastUpdated: String,
  val market: Double,
)

@Stable
sealed interface DeckSession {
  fun deckOrNull(): Deck?

  data object Loading : DeckSession {
    override fun deckOrNull(): Deck? = null
  }

  data class Loaded(val deck: Deck) : DeckSession {
    override fun deckOrNull(): Deck = deck
  }

  data object Error : DeckSession {
    override fun deckOrNull(): Deck? = null
  }
}

sealed interface DeckBuilderUiEvent : CircuitUiEvent {
  data object NavigateBack : DeckBuilderUiEvent
  data class AddCards(val superType: SuperType? = null) : DeckBuilderUiEvent
  data class CardClick(val card: Card) : DeckBuilderUiEvent

  data class EditName(val name: String) : DeckBuilderUiEvent
  data class EditDescription(val description: String) : DeckBuilderUiEvent
  data class AddTag(val tag: String) : DeckBuilderUiEvent
  data class RemoveTag(val tag: String) : DeckBuilderUiEvent

  data class IncrementCard(val cardId: String, val amount: Int = 1) : DeckBuilderUiEvent
  data class DecrementCard(val cardId: String, val amount: Int = 1) : DeckBuilderUiEvent
  data class RemoveCard(val cardId: String) : DeckBuilderUiEvent
  data class AddBoosterPack(val pack: BoosterPack) : DeckBuilderUiEvent
}
