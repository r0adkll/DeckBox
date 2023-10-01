package app.deckbox.features.boosterpacks.ui.builder

import androidx.compose.runtime.Stable
import app.deckbox.core.model.BoosterPack
import app.deckbox.core.model.Card
import app.deckbox.core.model.Deck
import app.deckbox.core.model.SuperType
import app.deckbox.features.boosterpacks.ui.builder.model.CardUiModel
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class BoosterPackBuilderUiState(
  val session: BoosterPackSession,
  val cards: List<CardUiModel>,
  val price: PackPriceState,
  val eventSink: (BoosterPackBuilderUiEvent) -> Unit,
) : CircuitUiState

@Stable
data class PackPriceState(
  val tcgPlayer: PackPrice? = null,
  val cardMarket: PackPrice? = null,
)

@Stable
data class PackPrice(
  val lastUpdated: String,
  val market: Double,
)

@Stable
sealed interface BoosterPackSession {
  fun boosterPackOrNull(): BoosterPack?

  data object Loading : BoosterPackSession {
    override fun boosterPackOrNull(): BoosterPack? = null
  }

  data class Loaded(val boosterPack: BoosterPack) : BoosterPackSession {
    override fun boosterPackOrNull(): BoosterPack = boosterPack
  }

  data object Error : BoosterPackSession {
    override fun boosterPackOrNull(): BoosterPack? = null
  }
}

sealed interface BoosterPackBuilderUiEvent {
  data object NavigateBack : BoosterPackBuilderUiEvent
  data class AddCards(val superType: SuperType? = null) : BoosterPackBuilderUiEvent
  data class CardClick(val card: Card) : BoosterPackBuilderUiEvent

  data object NewDeck : BoosterPackBuilderUiEvent
  data class EditName(val name: String) : BoosterPackBuilderUiEvent
  data class IncrementCard(val cardId: String, val amount: Int = 1) : BoosterPackBuilderUiEvent
  data class DecrementCard(val cardId: String, val amount: Int = 1) : BoosterPackBuilderUiEvent
  data class AddToDeck(val deck: Deck) : BoosterPackBuilderUiEvent
}
