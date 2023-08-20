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
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.deckbox.common.screens.BrowseScreen
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.logging.bark
import app.deckbox.core.model.SearchFilter
import app.deckbox.features.cards.public.model.CardQuery
import app.deckbox.features.cards.public.model.MAX_PAGE_SIZE
import app.deckbox.features.cards.public.paging.CardPagingSourceFactory
import app.deckbox.ui.filter.BrowseFilterPresenter
import app.deckbox.ui.filter.FilterPresenter
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, BrowseScreen::class)
@Inject
class BrowsePresenter(
  @Assisted private val navigator: Navigator,
  private val cardPagingSourceFactory: CardPagingSourceFactory,
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

    val filterUiState = filterPresenter.present()

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
      Pager(
        config = PagingConfig(
          pageSize = MAX_PAGE_SIZE,
        ),
        initialKey = 1,
        pagingSourceFactory = { cardPagingSourceFactory.create(query) },
      )
    }

    return BrowseUiState(
      query = searchQuery,
      filterUiState = filterUiState,
      cardsPager = pager,
    ) { event ->
      when (event) {
        is BrowseUiEvent.SearchUpdated -> {
          coroutineScope.launch { queryPipeline.emit(event.query) }
        }
        BrowseUiEvent.SearchCleared -> searchQuery = null
        is BrowseUiEvent.CardClicked -> navigator.goTo(CardDetailScreen(event.card))
      }
    }
  }
}
