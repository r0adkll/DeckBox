package app.deckbox.ui.tournament.tournaments

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.deckbox.common.screens.TournamentScreen
import app.deckbox.common.screens.TournamentsScreen
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.tournament.api.TournamentRepository
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, TournamentsScreen::class)
@Inject
class TournamentsPresenter(
  @Assisted private val navigator: Navigator,
  @Assisted private val screen: TournamentsScreen,
  private val tournamentsRepository: TournamentRepository,
) : Presenter<TournamentsUiState> {

  @Composable
  override fun present(): TournamentsUiState {
    var refreshCounter by remember { mutableIntStateOf(0) }
    var forceRefresh by remember { mutableStateOf(false) }

    // Load the tournament information
    val tournamentsLoadState by remember(refreshCounter) {
      flow {
        val result = tournamentsRepository.getTournaments(forceRefresh)
        if (result.isSuccess) {
          emit(LoadState.Loaded(result.getOrThrow()))
        } else {
          emit(LoadState.Error)
        }
      }
    }.collectAsState(LoadState.Loading)

    return TournamentsUiState(
      tournaments = tournamentsLoadState,
    ) { event ->
      when (event) {
        TournamentsUiEvent.NavigateBack -> navigator.pop()
        is TournamentsUiEvent.TournamentClick -> navigator.goTo(
          TournamentScreen(
            tournamentId = event.tournament.id,
            tournamentName = event.tournament.name,
            tournamentFormat = event.tournament.format,
          ),
        )

        TournamentsUiEvent.Refresh -> {
          forceRefresh = true
          refreshCounter++
        }
      }
    }
  }
}
