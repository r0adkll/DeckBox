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
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.SearchFilter
import app.deckbox.features.cards.public.CardRepository
import app.deckbox.features.cards.public.model.CardQuery
import app.deckbox.features.cards.public.paging.CardPagingSourceFactory
import app.deckbox.features.decks.api.builder.DeckBuilderRepository
import app.deckbox.ui.filter.BrowseFilterPresenter
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.FlowPreview
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
  private val deckBuilderRepository: DeckBuilderRepository,
  private val filterPresenter: BrowseFilterPresenter,
) : Presenter<BrowseUiState> {

  private val queryPipeline = MutableSharedFlow<String?>()

  @OptIn(FlowPreview::class)
  @Composable
  override fun present(): BrowseUiState {
    var searchQuery by rememberSaveable { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val searchQueryPipelineValue by remember {
      queryPipeline.debounce(1000L)
    }.collectAsState(null)

    LaunchedEffect(searchQueryPipelineValue) {
      searchQuery = searchQueryPipelineValue
    }

    val initialFilter = remember {
      screen.superType?.let {
        SearchFilter(superTypes = setOf(it))
      } ?: SearchFilter()
    }
    val filterUiState = filterPresenter.present(initialFilter)

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
      createPager { cardPagingSourceFactory.create(query) }
    }

    val deckState by remember {
      screen.deckId?.let { deckId ->
        cardRepository.observeCardsForDeck(deckId)
          .map { cards -> cards.associateBy { it.card.id } }
      } ?: flowOf(null)
    }.collectAsState(null)

    return BrowseUiState(
      isEditing = screen.deckId != null,
      query = searchQuery,
      filterUiState = filterUiState,
      cardsPager = pager,
      deckState = deckState,
    ) { event ->
      when (event) {
        BrowseUiEvent.NavigateBack -> navigator.pop()
        BrowseUiEvent.SearchCleared -> searchQuery = null
        is BrowseUiEvent.SearchUpdated -> {
          coroutineScope.launch { queryPipeline.emit(event.query) }
        }
        is BrowseUiEvent.CardClicked -> {
          if (screen.deckId != null) {
            deckBuilderRepository.incrementCard(
              deckId = screen.deckId!!,
              cardId = event.card.id,
            )
          } else {
            navigator.goTo(CardDetailScreen(event.card, deckId = screen.deckId))
          }
        }
        is BrowseUiEvent.CardLongClicked -> {
          navigator.goTo(CardDetailScreen(event.card, deckId = screen.deckId))
        }
      }
    }
  }
}
