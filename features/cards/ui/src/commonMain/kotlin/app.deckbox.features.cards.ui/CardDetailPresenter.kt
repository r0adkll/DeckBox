package app.deckbox.features.cards.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import app.deckbox.common.compose.message.UiMessage
import app.deckbox.common.compose.message.UiMessageManager
import app.deckbox.common.compose.message.showUiMessage
import app.deckbox.common.screens.BoosterPackBuilderScreen
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.common.screens.DeckBuilderScreen
import app.deckbox.common.screens.UrlScreen
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Card
import app.deckbox.core.model.SearchFilter
import app.deckbox.core.model.SuperType
import app.deckbox.features.boosterpacks.api.BoosterPackRepository
import app.deckbox.features.cards.public.CardRepository
import app.deckbox.features.cards.public.model.CardQuery
import app.deckbox.features.cards.public.model.OrderByReleaseDate
import app.deckbox.features.decks.api.builder.DeckBuilderRepository
import cafe.adriel.lyricist.LocalStrings
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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
    val coroutineScope = rememberCoroutineScope()

    val uiMessageManager = remember { UiMessageManager() }
    val message by uiMessageManager.message.collectAsState(null)

    val cardLoadState by loadCard(screen.cardId)

    // If we have passed a deck session id, then we should observe
    // this card in that deck to monitor its count
    val deckState by rememberRetained {
      observeCountForCard()
    }.collectAsRetainedState(null)

    val similarCards by loadSimilarCards(cardLoadState)
    val evolvesFrom by loadEvolvesFrom(cardLoadState)
    val evolvesTo by loadEvolvesTo(cardLoadState)

    val isFavorited by rememberRetained {
      repository.observeFavorite(screen.cardId)
    }.collectAsRetainedState(false)

    return CardDetailUiState(
      cardName = screen.cardName,
      cardImageUrl = screen.cardImageLarge,
      card = cardLoadState,
      similar = similarCards,
      evolvesFrom = evolvesFrom,
      evolvesTo = evolvesTo,
      deckState = deckState,
      isFavorited = isFavorited,
      uiMessage = message,
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

        is CardDetailUiEvent.Favorite -> {
          coroutineScope.launch {
            repository.favorite(screen.cardId, event.value)
          }
        }

        is CardDetailUiEvent.CardClick -> {
          navigator.goTo(
            CardDetailScreen(
              card = event.card,
              deckId = screen.deckId,
              packId = screen.packId,
            ),
          )
        }

        is CardDetailUiEvent.AddToDeck -> {
          deckBuilderRepository.incrementCard(event.deck.id, screen.cardId)
          uiMessageManager.showUiMessage(coroutineScope) {
            LocalStrings.current.cardDetailAddedToDeck(event.deck.name)
          }
        }

        is CardDetailUiEvent.AddToBoosterPack -> {
          boosterPackRepository.incrementCard(event.boosterPack.id, screen.cardId)
          uiMessageManager.showUiMessage(coroutineScope) {
            LocalStrings.current.cardDetailAddedToBoosterPack(event.boosterPack.name)
          }
        }

        CardDetailUiEvent.NewDeck -> {
          val sessionId = deckBuilderRepository.createSession()
          deckBuilderRepository.incrementCard(sessionId, screen.cardId)
          navigator.goTo(DeckBuilderScreen(sessionId))
        }
        CardDetailUiEvent.NewBoosterPack -> {
          val sessionId = boosterPackRepository.createSession()
          boosterPackRepository.incrementCard(sessionId, screen.cardId)
          navigator.goTo(BoosterPackBuilderScreen(sessionId))
        }

        is CardDetailUiEvent.ClearUiMessage -> {
          coroutineScope.launch {
            uiMessageManager.clearMessage(event.id)
          }
        }

      }
    }
  }

  @Composable
  private fun loadCard(id: String): State<LoadState<out Card>> {
    return rememberRetained {
      flow {
        val card = repository.getCard(id)
        emit(
          card?.let { LoadState.Loaded(card) }
            ?: LoadState.Error,
        )
      }
    }.collectAsRetainedState(LoadState.Loading)
  }

  @Composable
  private fun loadSimilarCards(loadState: LoadState<out Card>): State<LoadState<out List<Card>>> {
    return loadQueryState(loadState) { card ->
      val cleanName = card.name.replace(CleanNameRegex, "")
      CardQuery(
        orderBy = OrderByReleaseDate,
        queryOverride = "name:\"$cleanName\" -id:${card.id}",
      )
    }
  }

  @Composable
  private fun loadEvolvesFrom(loadState: LoadState<out Card>): State<LoadState<out List<Card>>> {
    return loadQueryState(
      loadState = loadState,
      predicate = { it.evolvesFrom != null },
    ) { card ->
      CardQuery(
        query = "\"${card.evolvesFrom!!}\"",
        orderBy = OrderByReleaseDate,
      )
    }
  }

  @Composable
  private fun loadEvolvesTo(loadState: LoadState<out Card>): State<LoadState<out List<Card>>> {
    return loadQueryState(
      loadState = loadState,
      predicate = { it.supertype == SuperType.POKEMON },
    ) { card ->
      CardQuery(
        orderBy = OrderByReleaseDate,
        filter = SearchFilter(
          evolvesFrom = card.name,
        ),
      )
    }
  }

  @Composable
  private fun loadQueryState(
    loadState: LoadState<out Card>,
    predicate: (Card) -> Boolean = { true },
    queryBuilder: (Card) -> CardQuery,
  ): State<LoadState<out List<Card>>> {
    return rememberRetained(loadState) {
      loadState.dataOrNull?.let { card ->
        if (predicate(card)) {
          val query = queryBuilder(card)
          flow {
            val cards = repository.getCards(query)
              .sortedByDescending { it.expansion.releaseDate }
            emit(LoadState.Loaded(cards))
          }.catch {
            LoadState.Error
          }
        } else {
          flowOf(LoadState.Loaded(emptyList()))
        }
      } ?: emptyFlow()
    }.collectAsRetainedState(LoadState.Loading)
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

private val CleanNameRegex by lazy {
  "\\s*\\(.*\\)".toRegex()
}
