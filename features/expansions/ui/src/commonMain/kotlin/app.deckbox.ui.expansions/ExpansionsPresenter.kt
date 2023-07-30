package app.deckbox.ui.expansions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.deckbox.common.screens.ExpansionsScreen
import app.deckbox.core.logging.bark
import app.deckbox.core.model.Expansion
import app.deckbox.expansions.ExpansionsRepository
import app.deckbox.expansions.ui.ExpansionCardStyle
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ExpansionsPresenterFactory(
  private val presenterFactory: (Navigator) -> ExpansionsPresenter,
) : Presenter.Factory {
  override fun create(
    screen: Screen,
    navigator: Navigator,
    context: CircuitContext,
  ): Presenter<*>? = when (screen) {
    is ExpansionsScreen -> presenterFactory(navigator)
    else -> null
  }
}

@Inject
class ExpansionsPresenter(
  @Assisted private val navigator: Navigator,
  private val expansionsRepository: ExpansionsRepository,
) : Presenter<ExpansionsUiState> {

  @Composable
  override fun present(): ExpansionsUiState {
    val expansionsLoadState by remember {
      expansionsRepository.observeExpansions()
        .map { expansions ->
          val sorted = expansions.sortedByDescending { it.releaseDate }
          ExpansionsLoadState.Loaded(sorted)
        }
        .catch {
          ExpansionsLoadState.Error("Unable to load expansions")
        }
    }.collectAsState(ExpansionsLoadState.Loading)

    // TODO Hook up to preferences + UI
    var expansionCardStyle by remember { mutableStateOf(ExpansionCardStyle.Large) }

    var searchQuery by rememberSaveable { mutableStateOf<String?>(null) }
    val filteredLoadState by remember {
      derivedStateOf {
        if (searchQuery.isNullOrBlank()) return@derivedStateOf expansionsLoadState
        when (val state = expansionsLoadState) {
          is ExpansionsLoadState.Loaded -> {
            state.copy(
              expansions = state.expansions.filter {
                it.name.contains(searchQuery!!, ignoreCase = true) ||
                  it.series.contains(searchQuery!!, ignoreCase = true) ||
                  it.ptcgoCode?.contains(searchQuery!!, ignoreCase = true) == true
              },
            )
          }
          else -> expansionsLoadState
        }
      }
    }

    return ExpansionsUiState(
      loadState = filteredLoadState,
      expansionCardStyle = expansionCardStyle,
      query = searchQuery,
    ) { event ->
      when (event) {
        is ExpansionsUiEvent.ChangeCardStyle -> expansionCardStyle = event.style
        is ExpansionsUiEvent.ExpansionClicked -> {
          bark { "Expansion Clicked: ${event.expansion.name}" }
        }
        is ExpansionsUiEvent.SearchUpdated -> searchQuery = event.query
        ExpansionsUiEvent.SearchCleared -> searchQuery = null
      }
    }
  }
}

sealed interface ExpansionsLoadState {
  object Loading : ExpansionsLoadState

  @Stable
  data class Loaded(val expansions: List<Expansion>) : ExpansionsLoadState

  @Stable
  data class Error(val message: String) : ExpansionsLoadState
}
