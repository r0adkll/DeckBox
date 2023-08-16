package app.deckbox.ui.expansions.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.common.screens.ExpansionDetailScreen
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.expansions.ExpansionsRepository
import app.deckbox.features.cards.public.model.MAX_PAGE_SIZE
import app.deckbox.features.cards.public.paging.ExpansionCardPagingSourceFactory
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, ExpansionDetailScreen::class)
@Inject
class ExpansionDetailPresenter(
  @Assisted private val navigator: Navigator,
  @Assisted private val screen: ExpansionDetailScreen,
  private val expansionRepository: ExpansionsRepository,
  private val expansionCardPagingSourceFactory: ExpansionCardPagingSourceFactory,
) : Presenter<ExpansionDetailUiState> {

  @Composable
  override fun present(): ExpansionDetailUiState {
    val expansionLoadState by remember {
      flow {
        val expansion = expansionRepository.getExpansion(screen.expansionId)
        emit(LoadState.Loaded(expansion))
      }
    }.collectAsState(LoadState.Loading)

    return when (val state = expansionLoadState) {
      is LoadState.Loaded -> {
        val pager = remember {
          Pager(
            config = PagingConfig(
              pageSize = MAX_PAGE_SIZE,
            ),
            pagingSourceFactory = { expansionCardPagingSourceFactory.create(state.data) },
          )
        }
        ExpansionDetailUiState.Loaded(
          expansion = state.data,
          cardsPager = pager,
        ) { event ->
          when (event) {
            ExpansionDetailUiEvent.NavigateBack -> navigator.pop()
            is ExpansionDetailUiEvent.CardSelected -> navigator.goTo(CardDetailScreen(event.card))
          }
        }
      }
      else -> ExpansionDetailUiState.Loading
    }
  }
}
