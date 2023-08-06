package app.deckbox.ui.browse

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.deckbox.common.screens.BrowseScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.SearchFilter
import app.deckbox.features.cards.public.model.CardQuery
import app.deckbox.features.cards.public.model.MAX_PAGE_SIZE
import app.deckbox.features.cards.public.paging.CardPagingSourceFactory
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, BrowseScreen::class)
@Inject
class BrowsePresenter(
  @Assisted private val navigator: Navigator,
  private val cardPagingSourceFactory: CardPagingSourceFactory,
) : Presenter<BrowseUiState> {

  @Composable
  override fun present(): BrowseUiState {

    var searchQuery by rememberSaveable { mutableStateOf<String?>(null) }
    var filter by remember { mutableStateOf(SearchFilter()) }
    val query by remember {
      derivedStateOf {
        if (searchQuery.isNullOrBlank() && filter.isEmpty) {
          CardQuery(orderBy = "-set.releaseDate")
        } else {
          CardQuery(
            query = searchQuery,
            filter = filter,
          )
        }
      }
    }

    // TODO: All the meat and potatoes here
    val pager = remember(query) {
      Pager(
        config = PagingConfig(
          pageSize = MAX_PAGE_SIZE,
        ),
        pagingSourceFactory = { cardPagingSourceFactory.create(query) },
      )
    }

    return BrowseUiState(
      query = searchQuery,
      filter = filter,
      cardsPager = pager,
    ) { event ->
      when (event) {
        is BrowseUiEvent.Filter -> filter = event.filter
        is BrowseUiEvent.SearchUpdated -> searchQuery = event.query
        BrowseUiEvent.SearchCleared -> searchQuery = null
      }
    }
  }
}
