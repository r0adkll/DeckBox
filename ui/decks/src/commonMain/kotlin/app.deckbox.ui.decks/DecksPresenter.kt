package app.deckbox.ui.decks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.deckbox.common.screens.DecksScreen
import app.deckbox.core.logging.bark
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen
import com.slack.circuit.runtime.presenter.Presenter
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class DecksPresenterFactory(
  private val presenterFactory: (Navigator) -> DecksPresenter
) : Presenter.Factory {
  override fun create(
    screen: Screen,
    navigator: Navigator,
    context: CircuitContext,
  ): Presenter<*>? = when (screen) {
    is DecksScreen -> presenterFactory(navigator)
    else -> null
  }
}

@Inject
class DecksPresenter(
  @Assisted private val navigator: Navigator,
) : Presenter<DecksUiState> {

  @Composable
  override fun present(): DecksUiState {
    var isLoading by remember { mutableStateOf(false) }

    // TODO: All the meat and potatoes here

    return DecksUiState(
      isLoading = isLoading,
      decks = emptyList(),
    ) { event ->
      bark { "$event" }
    }
  }
}
