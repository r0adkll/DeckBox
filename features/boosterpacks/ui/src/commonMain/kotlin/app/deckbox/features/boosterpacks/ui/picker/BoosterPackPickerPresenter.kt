package app.deckbox.features.boosterpacks.ui.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.deckbox.common.compose.overlays.overlayResult
import app.deckbox.common.screens.BoosterPackPickerScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.features.boosterpacks.api.BoosterPackRepository
import app.deckbox.features.boosterpacks.ui.list.BoosterPackLoadState
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, BoosterPackPickerScreen::class)
@Inject
class BoosterPackPickerPresenter(
  @Assisted private val navigator: Navigator,
  @Assisted private val screen: BoosterPackPickerScreen,
  private val repository: BoosterPackRepository,
) : Presenter<BoosterPackPickerUiState> {

  @Composable
  override fun present(): BoosterPackPickerUiState {
    val packLoadState by remember(screen) {
      repository.observeBoosterPacks()
        .map { BoosterPackLoadState.Loaded(it) }
        .catch { BoosterPackLoadState.Error }
    }.collectAsState(BoosterPackLoadState.Loading)

    return BoosterPackPickerUiState(
      packLoadState = packLoadState,
    ) { event ->
      when (event) {
        BoosterPackPickerUiEvent.Close -> navigator.pop()
        is BoosterPackPickerUiEvent.BoosterPackClick -> navigator.overlayResult(event.boosterPack)
      }
    }
  }
}
