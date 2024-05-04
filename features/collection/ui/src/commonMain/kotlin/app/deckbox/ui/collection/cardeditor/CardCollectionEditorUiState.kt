package app.deckbox.ui.collection.cardeditor

import androidx.compose.runtime.Stable
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.model.Card
import app.deckbox.core.model.CollectionCount
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class CardCollectionEditorUiState(
  val cardName: String,
  val cardImageUrl: String,
  val variants: LoadState<out List<Card.Variant>>,
  val collectionCount: CollectionCount,
  val eventSink: (CardCollectionEditorUiEvent) -> Unit,
) : CircuitUiState

sealed interface CardCollectionEditorUiEvent : CircuitUiEvent {
  data object Close : CardCollectionEditorUiEvent
  data class Increment(val variant: Card.Variant) : CardCollectionEditorUiEvent
  data class Decrement(val variant: Card.Variant) : CardCollectionEditorUiEvent
}
