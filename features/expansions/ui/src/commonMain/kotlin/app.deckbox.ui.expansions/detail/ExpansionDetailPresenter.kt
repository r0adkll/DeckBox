package app.deckbox.ui.expansions.detail

import androidx.compose.runtime.Composable
import app.deckbox.common.screens.ExpansionDetailScreen
import app.deckbox.core.di.MergeActivityScope
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, ExpansionDetailScreen::class)
@Inject
class ExpansionDetailPresenter(
  @Assisted private val navigator: Navigator,
  @Assisted private val screen: ExpansionDetailScreen,
) : Presenter<ExpansionDetailUiState> {

  @Composable
  override fun present(): ExpansionDetailUiState {
    TODO("Not yet implemented")
  }
}
