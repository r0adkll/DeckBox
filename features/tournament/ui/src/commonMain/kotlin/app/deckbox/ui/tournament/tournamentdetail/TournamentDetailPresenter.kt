package app.deckbox.ui.tournament.tournamentdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.deckbox.common.screens.DeckListScreen
import app.deckbox.common.screens.TournamentScreen
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.tournament.api.TournamentRepository
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, TournamentScreen::class)
@Inject
class TournamentDetailPresenter(
  @Assisted private val navigator: Navigator,
  @Assisted private val screen: TournamentScreen,
  private val tournamentRepository: TournamentRepository,
) : Presenter<TournamentDetailUiState> {

  @Composable
  override fun present(): TournamentDetailUiState {
    val participantsLoadState by remember {
      flow {
        val result = tournamentRepository.getParticipants(screen.tournamentId)
        if (result.isSuccess) {
          emit(LoadState.Loaded(result.getOrThrow()))
        } else {
          emit(LoadState.Error)
        }
      }
    }.collectAsState(LoadState.Loading)

    return TournamentDetailUiState(
      tournamentName = screen.tournamentName,
      tournamentFormat = screen.tournamentFormat,
      participants = participantsLoadState,
    ) { event ->
      when (event) {
        TournamentDetailUiEvent.NavigateBack -> navigator.pop()
        is TournamentDetailUiEvent.ParticipantClick -> navigator.goTo(
          DeckListScreen(
            deckListId = event.participant.deckListId!!,
            archetypeName = event.participant.archetype.name,
          ),
        )
      }
    }
  }
}
