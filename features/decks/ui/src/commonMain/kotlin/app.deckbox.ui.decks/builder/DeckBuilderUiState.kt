package app.deckbox.ui.decks.builder

import androidx.compose.runtime.Stable
import app.deckbox.core.model.Card
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class DeckBuilderUiState(
  val name: String,
  val cards: List<Card>,
  val eventSink: (DeckBuilderUiEvent) -> Unit,
) : CircuitUiState

sealed interface DeckBuilderUiEvent : CircuitUiEvent {
  data class EditName(val name: String) : DeckBuilderUiEvent
}
