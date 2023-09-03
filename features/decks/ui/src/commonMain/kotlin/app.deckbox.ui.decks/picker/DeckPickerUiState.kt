package app.deckbox.ui.decks.picker

import androidx.compose.runtime.Immutable
import app.deckbox.core.model.Deck
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class DeckPickerUiState(
  val isLoading: Boolean,
  val decks: List<Deck> = emptyList(),
  val eventSink: (DeckPickerUiEvent) -> Unit,
) : CircuitUiState

sealed interface DeckPickerUiEvent {
  data object Close : DeckPickerUiEvent
  data class DeckPicked(val deck: Deck) : DeckPickerUiEvent
}
