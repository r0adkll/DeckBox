package app.deckbox.ui.decks.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.deckbox.common.compose.overlays.overlayResult
import app.deckbox.common.screens.DeckPickerScreen
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Deck
import app.deckbox.features.decks.api.DeckRepository
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, DeckPickerScreen::class)
@Inject
class DeckPickerPresenter(
  @Assisted private val navigator: Navigator,
  private val deckRepository: DeckRepository,
) : Presenter<DeckPickerUiState> {

  @Composable
  override fun present(): DeckPickerUiState {
    val decksLoadState by remember {
      deckRepository.observeDecks()
        .map<List<Deck>, LoadState<ImmutableList<Deck>>> { LoadState.Loaded(it.toImmutableList()) }
        .catch {
          @Suppress("UNCHECKED_CAST")
          emit(LoadState.Error as LoadState<ImmutableList<Deck>>)
        }
    }.collectAsState(LoadState.Loading)

    return DeckPickerUiState(
      isLoading = decksLoadState is LoadState.Loading,
      decks = decksLoadState.dataOrNull ?: persistentListOf(),
    ) { event ->
      when (event) {
        DeckPickerUiEvent.Close -> navigator.pop()
        is DeckPickerUiEvent.DeckPicked -> navigator.overlayResult(event.deck)
      }
    }
  }
}
