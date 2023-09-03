package app.deckbox.ui.expansions.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.common.screens.ExpansionDetailScreen
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Card
import app.deckbox.core.model.SearchFilter
import app.deckbox.expansions.ExpansionsRepository
import app.deckbox.features.cards.public.ExpansionCardRepository
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
  private val expansionCardRepository: ExpansionCardRepository,
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
        val expansionCards by remember {
          flow {
            val cards = expansionCardRepository.getCards(state.data)
              .sortedBy { it.number.toIntOrNull() ?: 1 }
            emit(LoadState.Loaded(cards))
          }
        }.collectAsState(LoadState.Loading)

        val filterPresenter = remember(expansionCards) {
          ExpansionFilterPresenter((expansionCards as? LoadState.Loaded)?.data ?: emptyList())
        }

        val filterState = filterPresenter.present(key = screen.expansionId)

        ExpansionDetailUiState.Loaded(
          expansion = state.data,
          filterState = filterState,
          cards = expansionCards.filterBy(filterState.filter),
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

fun LoadState<out List<Card>>.filterBy(searchFilter: SearchFilter): LoadState<out List<Card>> {
  return when (this) {
    is LoadState.Loaded -> LoadState.Loaded(
      data.filter { card ->
        searchFilter.matches(card)
      },
    )
    else -> this
  }
}
