package app.deckbox.features.boosterpacks.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.deckbox.common.screens.BoosterPackBuilderScreen
import app.deckbox.common.screens.BoosterPackScreen
import app.deckbox.common.screens.SettingsScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.features.boosterpacks.api.BoosterPackRepository
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, BoosterPackScreen::class)
@Inject
class BoosterPackPresenter(
  @Assisted private val navigator: Navigator,
  private val boosterPackRepository: BoosterPackRepository,
) : Presenter<BoosterPackUiState> {

  @Composable
  override fun present(): BoosterPackUiState {

    val boosterPackLoadState by remember {
      boosterPackRepository.observeBoosterPacks()
        .map { BoosterPackLoadState.Loaded(it) }
        .catch { BoosterPackLoadState.Error }
    }.collectAsState(BoosterPackLoadState.Loading)

    return BoosterPackUiState(
      packState = boosterPackLoadState,
    ) { event ->
      when (event) {
        BoosterPackUiEvent.OpenAppSettings -> navigator.goTo(SettingsScreen())
        BoosterPackUiEvent.CreateNew -> navigator.goTo(BoosterPackBuilderScreen(boosterPackRepository.createSession()))
        is BoosterPackUiEvent.BoosterPackClick -> navigator.goTo(BoosterPackBuilderScreen(event.pack.id))
        is BoosterPackUiEvent.Delete -> boosterPackRepository.delete(event.pack.id)
        is BoosterPackUiEvent.Duplicate -> boosterPackRepository.duplicate(event.pack.id)
      }
    }
  }
}
