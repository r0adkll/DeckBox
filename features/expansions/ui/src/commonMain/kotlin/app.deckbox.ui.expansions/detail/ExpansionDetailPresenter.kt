package app.deckbox.ui.expansions.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import app.deckbox.common.screens.CardDetailPagerScreen
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.common.screens.ExpansionDetailScreen
import app.deckbox.common.settings.DeckBoxSettings
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Card
import app.deckbox.core.model.Collection
import app.deckbox.core.model.Expansion
import app.deckbox.core.model.SearchFilter
import app.deckbox.core.settings.PokemonGridStyle
import app.deckbox.expansions.ExpansionsRepository
import app.deckbox.features.cards.public.CardRepository
import app.deckbox.features.cards.public.ExpansionCardRepository
import app.deckbox.features.collection.api.CollectionRepository
import app.deckbox.ui.expansions.detail.ExpansionDetailUiEvent.CardSelected
import app.deckbox.ui.expansions.detail.ExpansionDetailUiEvent.ChangeGridStyle
import app.deckbox.ui.expansions.detail.ExpansionDetailUiEvent.DecrememntCollectionCount
import app.deckbox.ui.expansions.detail.ExpansionDetailUiEvent.IncrememntCollectionCount
import app.deckbox.ui.expansions.detail.ExpansionDetailUiEvent.NavigateBack
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

private const val DefaultIncrementAmount = 1
private const val DefaultDecrementAmount = -1

@CircuitInject(MergeActivityScope::class, ExpansionDetailScreen::class)
@Inject
class ExpansionDetailPresenter(
  @Assisted private val navigator: Navigator,
  @Assisted private val screen: ExpansionDetailScreen,
  private val expansionRepository: ExpansionsRepository,
  private val expansionCardRepository: ExpansionCardRepository,
  private val cardRepository: CardRepository,
  private val collectionRepository: CollectionRepository,
  private val settings: DeckBoxSettings,
) : Presenter<ExpansionDetailUiState> {

  @Composable
  override fun present(): ExpansionDetailUiState {
    val coroutineScope = rememberCoroutineScope()

    val expansionLoadState by remember {
      flow {
        if (screen.expansionId == Expansion.FAVORITES) {
          emit(LoadState.Loaded(Expansion.Favorites))
        } else {
          val expansion = expansionRepository.getExpansion(screen.expansionId)
          emit(LoadState.Loaded(expansion))
        }
      }
    }.collectAsState(LoadState.Loading)

    // Fetch the collection for this expansion
    val collection by remember {
      collectionRepository.observeCollectionForExpansion(screen.expansionId)
    }.collectAsState(Collection.empty())

    return when (val state = expansionLoadState) {
      is LoadState.Loaded -> {
        val expansionCards by remember {
          cardsFlow(state.data)
        }.collectAsState(LoadState.Loading)

        val filterPresenter = remember(expansionCards) {
          ExpansionFilterPresenter((expansionCards as? LoadState.Loaded)?.data ?: emptyList())
        }

        /**
         * Due to a compose multiplatform bug of calling composable functions from a subclass of an interface/abstract
         * that contains the function we can't rely on default parameters as they will crash on iOS. For now, just
         * pass the default at the top-level call-site
         *
         * https://github.com/JetBrains/compose-multiplatform/issues/3318
         *
         * TODO: Not 100% happy with the overall filter implementation and should re-visit it at some point to
         *  improve its implementation
         */
        val filterState = filterPresenter.present(screen.expansionId, SearchFilter())

        val cardGridStyle by remember {
          settings.observeExpansionCardGridStyle()
        }.collectAsState(PokemonGridStyle.Small)

        ExpansionDetailUiState.Loaded(
          expansion = state.data,
          filterState = filterState,
          cards = expansionCards.filterBy(filterState.filter),
          collection = collection,
          cardGridStyle = cardGridStyle,
        ) { event ->
          when (event) {
            NavigateBack -> navigator.pop()
            is CardSelected -> {
              navigator.goTo(
                CardDetailPagerScreen(
                  pagedCards = CardDetailPagerScreen.PagedCards.AsExpansion(
                    initialCard = CardDetailScreen(event.card),
                    expansionId = screen.expansionId,
                  ),
                ),
              )
            }
            is ChangeGridStyle -> settings.expansionCardGridStyle = event.style
            is IncrememntCollectionCount -> coroutineScope.launch {
              incrementCollectionCount(event.card.id, event.variant, DefaultIncrementAmount)
            }
            is DecrememntCollectionCount -> coroutineScope.launch {
              incrementCollectionCount(event.card.id, event.variant, DefaultDecrementAmount)
            }
          }
        }
      }

      else -> ExpansionDetailUiState.Loading
    }
  }

  @Suppress("UNUSED_PARAMETER")
  private suspend fun incrementCollectionCount(
    cardId: String,
    variant: Card.Variant,
    amount: Int,
  ) {
    collectionRepository.incrementCounts(
      cardId = cardId,
    )
  }

  private fun cardsFlow(expansion: Expansion): Flow<LoadState<out List<Card>>> {
    return if (expansion.id == Expansion.FAVORITES) {
      cardRepository.observeCardsForFavorites()
        .map { LoadState.Loaded(it) }
    } else {
      flow {
        val cards = expansionCardRepository.getCards(expansion)
          .sortedBy { it.number.toIntOrNull() ?: 1 }
        emit(LoadState.Loaded(cards))
      }
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
