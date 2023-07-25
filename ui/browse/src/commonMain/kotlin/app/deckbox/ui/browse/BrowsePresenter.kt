package app.deckbox.ui.browse

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.deckbox.common.screens.BrowseScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen
import com.slack.circuit.runtime.presenter.Presenter

class BrowsePresenterFactory : Presenter.Factory {
  override fun create(
    screen: Screen,
    navigator: Navigator,
    context: CircuitContext,
  ): Presenter<*>? = when (screen) {
    is BrowseScreen -> BrowsePresenter(navigator)
    else -> null
  }
}

class BrowsePresenter(
  private val navigator: Navigator,
) : Presenter<BrowseUiState> {

  @Composable
  override fun present(): BrowseUiState {
    var isLoading by remember { mutableStateOf(false) }

    // TODO: All the meat and potatoes here

    return BrowseUiState(
      isLoading = isLoading,
      results = emptyList(),
    ) { event ->
      when (event) {
        is BrowseUiEvent.Filter -> TODO()
        is BrowseUiEvent.Search -> TODO()
      }
    }
  }
}
