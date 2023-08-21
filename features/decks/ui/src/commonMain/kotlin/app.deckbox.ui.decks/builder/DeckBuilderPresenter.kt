package app.deckbox.ui.decks.builder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.deckbox.common.screens.DeckBuilderScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.features.decks.api.DeckBuilderRepository
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, DeckBuilderScreen::class)
@Inject
class DeckBuilderPresenter(
  @Assisted private val navigator: Navigator,
  @Assisted private val screen: DeckBuilderScreen,
  private val repository: DeckBuilderRepository,
) : Presenter<DeckBuilderUiState> {

  @Composable
  override fun present(): DeckBuilderUiState {
    var name by remember { mutableStateOf("") }

    return DeckBuilderUiState(
      name = name,
      cards = emptyList(),
    ) { event ->
      when (event) {
        is DeckBuilderUiEvent.EditName -> name = event.name
      }
    }
  }
}
