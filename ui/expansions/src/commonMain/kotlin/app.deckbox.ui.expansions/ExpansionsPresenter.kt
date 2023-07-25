package app.deckbox.ui.expansions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.deckbox.common.screens.DecksScreen
import app.deckbox.common.screens.ExpansionsScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen
import com.slack.circuit.runtime.presenter.Presenter

class ExpansionsPresenterFactory : Presenter.Factory {
  override fun create(
    screen: Screen,
    navigator: Navigator,
    context: CircuitContext,
  ): Presenter<*>? = when (screen) {
    is ExpansionsScreen -> ExpansionsPresenter(navigator)
    else -> null
  }
}

class ExpansionsPresenter(
  private val navigator: Navigator,
) : Presenter<ExpansionsUiState> {

  @Composable
  override fun present(): ExpansionsUiState {
    var isLoading by remember { mutableStateOf(false) }

    // TODO: All the meat and potatoes here

    return ExpansionsUiState(
      isLoading = isLoading,
      expansions = emptyList(),
    ) { event ->
      println(event)
    }
  }
}
