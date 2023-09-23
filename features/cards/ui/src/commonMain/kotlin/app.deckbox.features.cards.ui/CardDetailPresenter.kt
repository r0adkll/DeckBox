package app.deckbox.features.cards.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import app.deckbox.common.screens.CardDetailScreen
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
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
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

    // TODO: Create a common Message object that can be used to decorate and display
    //  snackbars, toasts, etc.
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    val cardLoadState by loadCard(screen.cardId)

    // If we have passed a deck session id, then we should observe
    // this card in that deck to monitor its count
    val deckState by remember {
      observeCountForCard()
    }.collectAsState(null)

    val similarCards by loadSimilarCards(cardLoadState)
    val evolvesFrom by loadEvolvesFrom(cardLoadState)
    val evolvesTo by loadEvolvesTo(cardLoadState)

    val isFavorited by remember {
      repository.observeFavorite(screen.cardId)
    }.collectAsState(false)

    return CardDetailUiState(
      cardName = screen.cardName,
      cardImageUrl = screen.cardImageLarge,
      card = cardLoadState,
      similar = similarCards,
      evolvesFrom = evolvesFrom,
      evolvesTo = evolvesTo,
      deckState = deckState,
      isFavorited = isFavorited,
      snackbarMessage = snackbarMessage,
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
          snackbarMessage = "Added to \"${event.deck.name}\""
        }
        CardDetailUiEvent.ClearSnackBar -> snackbarMessage = null
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
            ?: LoadState.Error,
        )
      }
    }.collectAsState(LoadState.Loading)
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
    return remember(loadState) {
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

private val CleanNameRegex by lazy {
  "\\s*\\(.*\\)".toRegex()
}
