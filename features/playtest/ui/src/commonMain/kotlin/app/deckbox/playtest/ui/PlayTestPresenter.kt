package app.deckbox.playtest.ui

import androidx.compose.runtime.Composable
import app.deckbox.common.screens.PlayTestScreen
import app.deckbox.core.di.MergeActivityScope
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, PlayTestScreen::class)
@Inject
class PlayTestPresenter(
  @Assisted private val navigator: Navigator,
  @Assisted private val screen: PlayTestScreen,
) : Presenter<PlayTestUiState> {

  @Composable
  override fun present(): PlayTestUiState {
    return PlayTestUiState { event ->
      TODO("Handle events")
    }
  }
}
