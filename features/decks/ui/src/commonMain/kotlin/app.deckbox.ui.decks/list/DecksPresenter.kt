package app.deckbox.ui.decks.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import app.deckbox.common.screens.DeckBuilderScreen
import app.deckbox.common.screens.DeckTextImporterScreen
import app.deckbox.common.screens.DecksScreen
import app.deckbox.common.screens.PlayTestScreen
import app.deckbox.common.screens.SettingsScreen
import app.deckbox.common.screens.TournamentsScreen
import app.deckbox.common.settings.DeckBoxSettings
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.logging.bark
import app.deckbox.core.model.Deck
import app.deckbox.core.model.DeckShareAction
import app.deckbox.core.settings.DeckCardConfig
import app.deckbox.core.settings.SortOption.UpdatedAt
import app.deckbox.core.settings.orderDecksBy
import app.deckbox.features.decks.api.DeckCardConfigurator
import app.deckbox.features.decks.api.DeckRepository
import app.deckbox.features.decks.api.builder.DeckBuilderRepository
import app.deckbox.features.decks.api.export.DeckExporter
import app.deckbox.features.decks.public.ui.DeckExportOption
import app.deckbox.features.decks.public.ui.events.DeckCardEvent
import app.deckbox.sharing.api.ShareManager
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, DecksScreen::class)
@Inject
class DecksPresenter(
  @Assisted private val navigator: Navigator,
  private val deckRepository: DeckRepository,
  private val deckBuilderRepository: DeckBuilderRepository,
  private val deckCardConfigurator: DeckCardConfigurator,
  private val shareManager: ShareManager,
  private val deckBoxSettings: DeckBoxSettings,
) : Presenter<DecksUiState> {

  @Suppress("UNCHECKED_CAST")
  @Composable
  override fun present(): DecksUiState {
    val coroutineScope = rememberCoroutineScope()

    val decksLoadState by remember {
      deckRepository.observeDecks()
        .map<List<Deck>, LoadState<List<Deck>>> { decks ->
          val sorted = decks.sortedBy { it.updatedAt }
          LoadState.Loaded(sorted)
        }
        .catch { emit(LoadState.Error as LoadState<List<Deck>>) }
    }.collectAsState(LoadState.Loading)

    val deckCardConfig by remember {
      deckCardConfigurator.observeConfig()
    }.collectAsState(DeckCardConfig.DEFAULT)

    val deckSortOrder by remember {
      deckBoxSettings.observeDeckSortOrder()
    }.collectAsState(UpdatedAt)

    val sortedDecks by remember {
      snapshotFlow {
        decksLoadState.dataOrNull?.orderDecksBy(deckSortOrder)
          ?.toImmutableList()
          ?: persistentListOf()
      }
    }.collectAsState(persistentListOf())

    return DecksUiState(
      isLoading = decksLoadState is LoadState.Loading,
      deckCardConfig = deckCardConfig,
      deckSortOrder = deckSortOrder,
      decks = sortedDecks,
    ) { event ->
      when (event) {
        is DecksUiEvent.CardEvent -> when (event.event) {
          DeckCardEvent.Clicked -> navigator.goTo(DeckBuilderScreen(event.deck.id))
          DeckCardEvent.Delete -> coroutineScope.launch {
            deckRepository.deleteDeck(event.deck.id)
          }

          DeckCardEvent.Duplicate -> coroutineScope.launch {
            deckRepository.duplicateDeck(event.deck.id)
          }

          is DeckCardEvent.Export -> coroutineScope.launch {
            shareDeck(event.deck, event.event.option)
          }

          DeckCardEvent.Test -> {
            navigator.goTo(PlayTestScreen(event.deck.id))
          }
        }

        DecksUiEvent.CreateNewDeck -> navigator.goTo(
          // TODO: Hate this, need to use the screen object as passed data persistent
          //  so the new session Id has to be generated here
          DeckBuilderScreen(deckBuilderRepository.createSession()),
        )
        DecksUiEvent.OpenAppSettings -> navigator.goTo(SettingsScreen())
        is DecksUiEvent.ChangeSortOrder -> deckBoxSettings.deckSortOrder = event.sortOrder

        DecksUiEvent.ImportDeck -> navigator.goTo(DeckTextImporterScreen())
        DecksUiEvent.ImportTournamentDeck -> navigator.goTo(TournamentsScreen())
      }
    }
  }

  private suspend fun shareDeck(deck: Deck, option: DeckExportOption) {
    shareManager.share(
      DeckShareAction(
        deck = deck,
        type = when (option) {
          DeckExportOption.Text -> DeckShareAction.ExportType.Text
          DeckExportOption.Image -> DeckShareAction.ExportType.Image
        }
      )
    )
  }
}
