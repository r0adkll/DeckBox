package app.deckbox.features.boosterpacks.ui.list

import androidx.compose.runtime.Stable
import app.deckbox.core.model.BoosterPack
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.SortOption
import com.slack.circuit.runtime.CircuitUiState

@Stable
data class BoosterPackUiState(
  val packState: BoosterPackLoadState,
  val sortOption: SortOption,
  val eventSink: (BoosterPackUiEvent) -> Unit,
) : CircuitUiState

sealed interface BoosterPackLoadState {
  val packs: List<BoosterPack>? get() = null

  data object Loading : BoosterPackLoadState
  data class Loaded(override val packs: List<BoosterPack>) : BoosterPackLoadState
  data object Error : BoosterPackLoadState

  fun map(mapper: (List<BoosterPack>) -> List<BoosterPack>): BoosterPackLoadState {
    return when (this) {
      is Loaded -> Loaded(mapper(packs))
      else -> this
    }
  }
}

sealed interface BoosterPackUiEvent {
  data object CreateNew : BoosterPackUiEvent
  data object OpenAppSettings : BoosterPackUiEvent

  data class BoosterPackClick(val pack: BoosterPack) : BoosterPackUiEvent
  data class Delete(val pack: BoosterPack) : BoosterPackUiEvent
  data class Duplicate(val pack: BoosterPack) : BoosterPackUiEvent
  data class AddToDeck(
    val deck: Deck,
    val pack: BoosterPack,
  ): BoosterPackUiEvent
  data class ChangeSortOption(val sortOption: SortOption) : BoosterPackUiEvent
}
