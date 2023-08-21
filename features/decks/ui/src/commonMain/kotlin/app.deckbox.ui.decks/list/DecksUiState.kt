package app.deckbox.ui.decks.list

import androidx.compose.runtime.Stable
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.DeckCardConfig
import app.deckbox.features.decks.public.ui.events.DeckCardEvent
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class DecksUiState(
  val isLoading: Boolean = false,
  val deckCardConfig: DeckCardConfig,
  val decks: List<Deck> = emptyList(),
  val eventSink: (DecksUiEvent) -> Unit,
) : CircuitUiState

sealed interface DecksUiEvent : CircuitUiEvent {
  object CreateNewDeck : DecksUiEvent
  object OpenAppSettings : DecksUiEvent
  data class CardEvent(val deck: Deck, val event: DeckCardEvent) : DecksUiEvent
}
