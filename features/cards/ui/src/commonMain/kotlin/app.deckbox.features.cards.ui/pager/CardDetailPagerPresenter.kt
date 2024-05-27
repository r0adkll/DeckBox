package app.deckbox.features.cards.ui.pager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.deckbox.common.screens.CardDetailPagerScreen
import app.deckbox.common.screens.CardDetailPagerScreen.PagedCards
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.logging.bark
import app.deckbox.core.model.Expansion
import app.deckbox.core.util.sortCards
import app.deckbox.expansions.ExpansionsRepository
import app.deckbox.features.cards.public.CardRepository
import app.deckbox.features.cards.public.ExpansionCardRepository
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, CardDetailPagerScreen::class)
@Inject
class CardDetailPagerPresenter(
  @Assisted private val navigator: Navigator,
  @Assisted private val screen: CardDetailPagerScreen,
  private val cardRepository: CardRepository,
  private val expansionRepository: ExpansionsRepository,
  private val expansionCardRepository: ExpansionCardRepository,
) : Presenter<CardDetailPagerUiState> {

  @Composable
  override fun present(): CardDetailPagerUiState {
    val detailPage = cardDetailPageForScreen(screen.pagedCards)

    return CardDetailPagerUiState(
      navigator = navigator,
      detailPage = detailPage,
    ) { event ->
      when (event) {
        CardDetailPagerUiEvent.NavigateBack -> navigator.pop()
      }
    }
  }

  @Composable
  private fun cardDetailPageForScreen(
    pagedCards: PagedCards,
  ): CardDetailPage {
    return when (pagedCards) {
      is PagedCards.AsList -> detailPageFromList(pagedCards)
      is PagedCards.AsDeck -> detailPageFromDeck(pagedCards)
      is PagedCards.AsBoosterPack -> detailPageFromBoosterPack(pagedCards)
      is PagedCards.AsExpansion -> detailPageFromExpansion(pagedCards)
    }
  }

  @Composable
  private fun detailPageFromList(asList: PagedCards.AsList): CardDetailPage {
    return CardDetailPage(
      initialScreen = asList.initialCard,
      screens = asList.cards,
    )
  }

  @Composable
  private fun detailPageFromExpansion(asExpansion: PagedCards.AsExpansion): CardDetailPage {
    val cardScreens by remember {
      flow {
        val cards = if (asExpansion.expansionId == Expansion.FAVORITES) {
          cardRepository.observeCardsForFavorites()
            .firstOrNull()
            ?.map { CardDetailScreen(it) }
            ?: listOf(asExpansion.initialCard)
        } else {
          val expansion = expansionRepository.getExpansion(asExpansion.expansionId)
          expansionCardRepository.getCards(expansion)
            .sortedBy { it.number.toIntOrNull() ?: 1 }
            .map { CardDetailScreen(it) }
        }
        emit(cards)
      }
    }.collectAsState(listOf(asExpansion.initialCard))

    return CardDetailPage(
      initialScreen = asExpansion.initialCard,
      screens = cardScreens,
    ).apply {
      bark { "CardDetailPage(initial=${asExpansion.initialCard.cardId}, index=${this.initialIndex})" }
    }
  }

  @Composable
  private fun detailPageFromDeck(asDeck: PagedCards.AsDeck): CardDetailPage {
    val cardScreens by remember {
      flow {
        val cards = cardRepository.observeCardsForDeck(asDeck.deckId)
          .firstOrNull()
          ?.sortCards()
          ?.map {
            CardDetailScreen(
              card = it.card,
              deckId = asDeck.deckId,
            )
          }
          ?: listOf(asDeck.initialCard)

        emit(cards)
      }
    }.collectAsState(listOf(asDeck.initialCard))

    return CardDetailPage(
      initialScreen = asDeck.initialCard,
      screens = cardScreens,
    )
  }

  @Composable
  private fun detailPageFromBoosterPack(asBoosterPack: PagedCards.AsBoosterPack): CardDetailPage {
    val cardScreens by remember {
      flow {
        val cards = cardRepository.observeCardsForBoosterPack(asBoosterPack.packId)
          .firstOrNull()
          ?.sortCards()
          ?.map {
            CardDetailScreen(
              card = it.card,
              packId = asBoosterPack.packId,
            )
          }
          ?: listOf(asBoosterPack.initialCard)

        emit(cards)
      }
    }.collectAsState(listOf(asBoosterPack.initialCard))

    return CardDetailPage(
      initialScreen = asBoosterPack.initialCard,
      screens = cardScreens,
    )
  }

//  @Composable
//  private fun detailPageFromRemoteKey(asRemoteKey: PagedCards.AsRemoteKey): CardDetailPage {
//    val cardScreens by remember {
//      flow {
//
//
//        emit(listOf(asRemoteKey.initialCard))
//      }
//    }.collectAsState(listOf(asRemoteKey.initialCard))
//
//    return CardDetailPage(
//      initialScreen = asRemoteKey.initialCard,
//      screens = emptyList(),
//    )
//  }
}
