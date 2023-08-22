package app.deckbox.ui.decks.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.deckbox.common.screens.DeckBuilderScreen
import app.deckbox.common.screens.DecksScreen
import app.deckbox.common.screens.SettingsScreen
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.logging.bark
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.DeckCardConfig
import app.deckbox.features.decks.api.DeckCardConfigurator
import app.deckbox.features.decks.api.DeckRepository
import app.deckbox.features.decks.public.ui.events.DeckCardEvent
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
      when (event) {
        is DecksUiEvent.CardEvent -> when (event.event) {
          DeckCardEvent.Clicked -> navigator.goTo(DeckBuilderScreen(event.deck.id))
          DeckCardEvent.Delete -> deckRepository.deleteDeck(event.deck)
          DeckCardEvent.Duplicate -> deckRepository.duplicateDeck(event.deck)
          DeckCardEvent.Export -> bark { "Export (${event.deck.name})" }
          DeckCardEvent.Test -> bark { "Experiment (${event.deck.name})" }
        }
        DecksUiEvent.CreateNewDeck -> navigator.goTo(DeckBuilderScreen())
        DecksUiEvent.OpenAppSettings -> navigator.goTo(SettingsScreen())
      }
    }
  }
}
