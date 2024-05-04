package app.deckbox.ui.expansions.detail

import androidx.compose.runtime.Stable
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.model.Card
import app.deckbox.core.model.CardId
import app.deckbox.core.model.Collection
import app.deckbox.core.model.Expansion
import app.deckbox.core.settings.PokemonGridStyle
import app.deckbox.ui.filter.FilterUiState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
sealed interface ExpansionDetailUiState : CircuitUiState {
  data object Loading : ExpansionDetailUiState
  data class Loaded(
    val expansion: Expansion,
    val collection: Collection<CardId>,
    val filterState: FilterUiState,
    val cardGridStyle: PokemonGridStyle,
    val cards: LoadState<out List<Card>>,
    val eventSink: (ExpansionDetailUiEvent) -> Unit,
  ) : ExpansionDetailUiState
}

sealed interface ExpansionDetailUiEvent : CircuitUiEvent {
  data object NavigateBack : ExpansionDetailUiEvent
  data class CardSelected(val card: Card) : ExpansionDetailUiEvent
  data class ChangeGridStyle(val style: PokemonGridStyle) : ExpansionDetailUiEvent
  data class IncrememntCollectionCount(val card: Card, val variant: Card.Variant) : ExpansionDetailUiEvent
  data class DecrememntCollectionCount(val card: Card, val variant: Card.Variant) : ExpansionDetailUiEvent
}
