package app.deckbox.ui.decks

import androidx.compose.runtime.Stable
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.DeckCardConfig
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class DecksUiState(
  val isLoading: Boolean = false,
  val deckCardConfig: DeckCardConfig,
  val decks: List<Deck> = emptyList(),
  val eventSink: (DecksUiEvent) -> Unit,
) : CircuitUiState

sealed interface DecksUiEvent : CircuitUiEvent
