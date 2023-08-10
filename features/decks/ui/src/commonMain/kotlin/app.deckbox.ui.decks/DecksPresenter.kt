package app.deckbox.ui.decks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.deckbox.common.screens.DecksScreen
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.logging.bark
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.DeckCardConfig
import app.deckbox.features.decks.public.DeckCardConfigurator
import app.deckbox.features.decks.public.DeckRepository
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Suppress("UNCHECKED_CAST")
@CircuitInject(MergeActivityScope::class, DecksScreen::class)
@Inject
class DecksPresenter(
  @Assisted private val navigator: Navigator,
  private val deckRepository: DeckRepository,
  private val deckCardConfigurator: DeckCardConfigurator,
) : Presenter<DecksUiState> {

  @Composable
  override fun present(): DecksUiState {
    val decksLoadState by remember {
      deckRepository.observeDecks()
        .map<List<Deck>, LoadState<List<Deck>>> { LoadState.Loaded(it) }
        .catch { emit(LoadState.Error(it.message ?: "Error fetching decks") as LoadState<List<Deck>>) }
    }.collectAsState(LoadState.Loading)

    val deckCardConfig by remember {
      deckCardConfigurator.observeConfig()
    }.collectAsState(DeckCardConfig.DEFAULT)

    return DecksUiState(
      isLoading = decksLoadState is LoadState.Loading,
      deckCardConfig = deckCardConfig,
      decks = (decksLoadState as? LoadState.Loaded<List<Deck>>)?.data ?: emptyList(),
    ) { event ->
      bark { "$event" }
    }
  }
}
