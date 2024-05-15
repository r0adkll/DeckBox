package app.deckbox.ui.decks.importer

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.input.TextFieldValue
import app.deckbox.common.compose.widgets.builder.model.CardUiModel
import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import app.deckbox.features.decks.api.import.DeckTextParser
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList

@Stable
data class DeckTextImporterUiState(
  val textField: TextFieldValue,
  val isLoading: Boolean,
  val isImportEnabled: Boolean,
  val previewModels: ImmutableList<CardUiModel>,
  val eventSink: (DeckTextImporterUiEvent) -> Unit,
) : CircuitUiState

sealed interface ParsedLoadState {
  data object Loading : ParsedLoadState
  data class Loaded(val data: List<Stacked<Card>>) : ParsedLoadState
  data class Error(val error: DeckTextParser.Errors) : ParsedLoadState
}

sealed interface DeckTextImporterUiEvent : CircuitUiEvent {
  data object NavigateBack : DeckTextImporterUiEvent
  data object Import : DeckTextImporterUiEvent
  data object ClearInput : DeckTextImporterUiEvent
  data class TextFieldUpdated(val field: TextFieldValue) : DeckTextImporterUiEvent
  data class OpenUrl(val url: String) : DeckTextImporterUiEvent
  data class CardClick(val card: Card) : DeckTextImporterUiEvent
}
