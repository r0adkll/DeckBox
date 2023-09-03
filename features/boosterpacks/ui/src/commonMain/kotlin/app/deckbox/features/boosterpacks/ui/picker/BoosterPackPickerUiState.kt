package app.deckbox.features.boosterpacks.ui.picker

import androidx.compose.runtime.Immutable
import app.deckbox.core.model.BoosterPack
import app.deckbox.features.boosterpacks.ui.list.BoosterPackLoadState
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class BoosterPackPickerUiState(
  val packLoadState: BoosterPackLoadState,
  val eventSink: (BoosterPackPickerUiEvent) -> Unit,
) : CircuitUiState

sealed interface BoosterPackPickerUiEvent {
  data object Close : BoosterPackPickerUiEvent
  data class BoosterPackClick(val boosterPack: BoosterPack) : BoosterPackPickerUiEvent
}
