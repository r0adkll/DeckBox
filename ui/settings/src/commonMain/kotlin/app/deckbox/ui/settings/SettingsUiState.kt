package app.deckbox.ui.settings

import com.slack.circuit.runtime.CircuitUiState

data class SettingsUiState(
  val options: String,
  val eventSink: (SettingsUiEvent) -> Unit,
) : CircuitUiState

sealed interface SettingsUiEvent
