package app.deckbox.ui.browse

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.deckbox.common.screens.BrowseScreen
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.common.settings.DeckBoxSettings
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Card
import app.deckbox.core.model.SearchFilter
import app.deckbox.core.model.Stacked
import app.deckbox.core.settings.PokemonGridStyle
import app.deckbox.features.boosterpacks.api.BoosterPackRepository
import app.deckbox.features.cards.public.CardRepository
import app.deckbox.features.cards.public.model.CardQuery
import app.deckbox.features.cards.public.paging.CardPagingSourceFactory
import app.deckbox.features.cards.public.paging.CardRemoteMediatorFactory
import app.deckbox.features.decks.api.builder.DeckBuilderRepository
import app.deckbox.ui.filter.BrowseFilterPresenter
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, BrowseScreen::class)
@Inject
class BrowsePresenter(
  @Assisted private val navigator: Navigator,
  @Assisted private val screen: BrowseScreen,
  private val cardRepository: CardRepository,
  private val cardPagingSourceFactory: CardPagingSourceFactory,
  private val cardRemoteMediatorFactory: CardRemoteMediatorFactory,
  private val deckBuilderRepository: DeckBuilderRepository,
  private val boosterPackRepository: BoosterPackRepository,
  private val filterPresenter: BrowseFilterPresenter,
  private val settings: DeckBoxSettings,
) : Presenter<BrowseUiState> {

  private val queryPipeline = MutableSharedFlow<String?>()

  @OptIn(FlowPreview::class)
  @Composable
  override fun present(): BrowseUiState {
    var searchQuery by rememberRetained { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val searchQueryPipelineValue by remember {
      queryPipeline.debounce(1000L)
    }.collectAsRetainedState(null)

    LaunchedEffect(searchQueryPipelineValue) {
      searchQuery = searchQueryPipelineValue
    }

    val initialFilter = remember {
      screen.superType?.let {
        SearchFilter(superTypes = setOf(it))
      } ?: SearchFilter()
    }
    val filterUiState = filterPresenter.present(
      key = screen.hashCode().toString(),
      initialFilter = initialFilter,
    )

    val query by remember(filterUiState.filter) {
      derivedStateOf {
        if (searchQuery.isNullOrBlank() && filterUiState.filter.isEmpty) {
          CardQuery(
            orderBy = "-set.releaseDate",
            pageSize = 50, // TODO: Is this the best place for this? Should we set this globally?
          )
        } else {
          CardQuery(
            query = searchQuery?.let { "$it*" },
            orderBy = "-set.releaseDate",
            filter = filterUiState.filter,
            pageSize = 50, // TODO: Is this the best place for this? Should we set this globally?
          )
        }
      }
    }

    val pager = remember(query) {
      createPager(
        remoteMediatorFactory = { cardRemoteMediatorFactory.create(query) },
        pagingSourceFactory = { cardPagingSourceFactory.create(query) },
      )
    }

    val countState by remember {
      observeCountState()
    }.collectAsRetainedState(null)

    val gridStyle by remember {
      settings.observeBrowseCardGridStyle()
    }.collectAsRetainedState(PokemonGridStyle.Small)

    return BrowseUiState(
      isEditing = screen.deckId != null || screen.packId != null,
      query = searchQuery,
      filterUiState = filterUiState,
      cardsPager = pager,
      countState = countState,
      cardGridStyle = gridStyle,
    ) { event ->
      when (event) {
        BrowseUiEvent.NavigateBack -> navigator.pop()
        BrowseUiEvent.SearchCleared -> searchQuery = null
        is BrowseUiEvent.SearchUpdated -> {
          coroutineScope.launch { queryPipeline.emit(event.query) }
        }

        is BrowseUiEvent.CardClicked -> {
          when {
            screen.deckId != null -> {
              deckBuilderRepository.incrementCard(
                deckId = screen.deckId!!,
                cardId = event.card.id,
              )
            }

            screen.packId != null -> {
              boosterPackRepository.incrementCard(
                id = screen.packId!!,
                cardId = event.card.id,
              )
            }

            else -> {
              navigator.goTo(
                CardDetailScreen(
                  event.card,
                  deckId = screen.deckId,
                  packId = screen.packId,
                ),
              )
            }
          }
        }

        is BrowseUiEvent.CardLongClicked -> {
          navigator.goTo(
            CardDetailScreen(
              card = event.card,
              deckId = screen.deckId,
              packId = screen.packId,
            ),
          )
        }

        is BrowseUiEvent.GridStyleChanged -> settings.browseCardGridStyle = event.style
      }
    }
  }

  private fun observeCountState(): Flow<Map<String, Stacked<Card>>?> {
    return when {
      screen.deckId != null -> cardRepository.observeCardsForDeck(screen.deckId!!)
        .map { cards -> cards.associateBy { it.card.id } }
      screen.packId != null -> cardRepository.observeCardsForBoosterPack(screen.packId!!)
        .map { cards -> cards.associateBy { it.card.id } }
      else -> flowOf(null)
    }
  }
}
