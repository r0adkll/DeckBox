package app.deckbox.ui.decks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.deckbox.common.screens.BrowseScreen
import app.deckbox.common.screens.DecksScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen
import com.slack.circuit.runtime.presenter.Presenter

class DecksPresenterFactory : Presenter.Factory {
  override fun create(
    screen: Screen,
    navigator: Navigator,
    context: CircuitContext,
  ): Presenter<*>? = when (screen) {
    is DecksScreen -> DecksPresenter(navigator)
    else -> null
  }
}

class DecksPresenter(
  private val navigator: Navigator,
) : Presenter<DecksUiState> {

  @Composable
  override fun present(): DecksUiState {
    var isLoading by remember { mutableStateOf(false) }

    // TODO: All the meat and potatoes here

    return DecksUiState(
      isLoading = isLoading,
      decks = emptyList(),
    ) { event ->
      println(event)
    }
  }
}
