package app.deckbox.ui.decks.picker

import androidx.compose.runtime.Immutable
import app.deckbox.core.model.Deck
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class DeckPickerUiState(
  val isLoading: Boolean,
  val decks: ImmutableList<Deck>,
  val eventSink: (DeckPickerUiEvent) -> Unit,
) : CircuitUiState

sealed interface DeckPickerUiEvent {
  data object Close : DeckPickerUiEvent
  data object NewDeck : DeckPickerUiEvent
  data class DeckPicked(val deck: Deck) : DeckPickerUiEvent
}
