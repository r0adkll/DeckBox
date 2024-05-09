package app.deckbox.ui.settings

import androidx.compose.runtime.Composable
import app.deckbox.common.screens.SettingsScreen
import app.deckbox.common.settings.DeckBoxSettings
import app.deckbox.core.di.MergeActivityScope
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, SettingsScreen::class)
@Inject
class SettingsPresenter(
  @Assisted private val navigator: Navigator,
  private val deckBoxSettings: DeckBoxSettings,
) : Presenter<SettingsUiState> {

  @Composable
  override fun present(): SettingsUiState {
    return SettingsUiState(
      options = "",
    ) { event ->
      when (event) {
        SettingsUiEvent.NavigateBack -> navigator.pop()
      }
    }
  }
}
