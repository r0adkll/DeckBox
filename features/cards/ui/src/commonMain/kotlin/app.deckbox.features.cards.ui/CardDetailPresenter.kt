package app.deckbox.features.cards.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.common.screens.UrlScreen
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Card
import app.deckbox.features.boosterpacks.api.BoosterPackRepository
import app.deckbox.features.cards.public.CardRepository
import app.deckbox.features.decks.api.builder.DeckBuilderRepository
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, CardDetailScreen::class)
@Inject
class CardDetailPresenter(
  @Assisted private val navigator: Navigator,
  @Assisted private val screen: CardDetailScreen,
  private val repository: CardRepository,
  private val deckBuilderRepository: DeckBuilderRepository,
  private val boosterPackRepository: BoosterPackRepository,
) : Presenter<CardDetailUiState> {

  @Composable
  override fun present(): CardDetailUiState {
    val cardLoadState by loadCard(screen.cardId)

    // If we have passed a deck session id, then we should observe
    // this card in that deck to monitor its count
    val deckState by remember {
      observeCountForCard()
    }.collectAsState(null)

    // TODO Load additional card information such as evolution info, similar cards, etc

    return CardDetailUiState(
      cardName = screen.cardName,
      cardImageUrl = screen.cardImageLarge,
      card = cardLoadState,
      deckState = deckState,
    ) { event ->
      when (event) {
        CardDetailUiEvent.NavigateBack -> navigator.pop()
        is CardDetailUiEvent.OpenUrl -> navigator.goTo(UrlScreen(event.url))
        CardDetailUiEvent.DecrementCount -> {
          screen.deckId?.let { deckId ->
            deckBuilderRepository.decrementCard(deckId, screen.cardId)
          }
          screen.packId?.let { packId ->
            boosterPackRepository.decrementCard(packId, screen.cardId)
          }
        }
        CardDetailUiEvent.IncrementCount -> {
          screen.deckId?.let { deckId ->
            deckBuilderRepository.incrementCard(deckId, screen.cardId)
          }
          screen.packId?.let { packId ->
            boosterPackRepository.incrementCard(packId, screen.cardId)
          }
        }
      }
    }
  }

  @Composable
  private fun loadCard(id: String): State<LoadState<out Card>> {
    return remember {
      flow {
        val card = repository.getCard(id)
        emit(
          card?.let { LoadState.Loaded(card) }
            ?: LoadState.Error("Unable to load card for ${screen.cardId}"),
        )
      }
    }.collectAsState(LoadState.Loading)
  }

  private fun observeCountForCard(): Flow<DeckState?> {
    return when {
      screen.deckId != null -> repository.observeCardsForDeck(screen.deckId!!)
        .map {
          it.find { it.card.id == screen.cardId }
            ?.let { DeckState(it.count) }
            ?: DeckState(0)
        }
      screen.packId != null -> repository.observeCardsForBoosterPack(screen.packId!!)
        .map {
          it.find { it.card.id == screen.cardId }
            ?.let { DeckState(it.count) }
            ?: DeckState(0)
        }
      else -> flowOf(null)
    }
  }
}
