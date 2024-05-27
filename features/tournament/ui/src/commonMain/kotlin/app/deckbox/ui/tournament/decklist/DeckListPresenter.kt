package app.deckbox.ui.tournament.decklist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import app.deckbox.common.compose.widgets.builder.model.CardUiModel
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.common.screens.DeckBuilderScreen
import app.deckbox.common.screens.DeckListScreen
import app.deckbox.common.screens.ImportScreen
import app.deckbox.common.screens.UrlScreen
import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.coroutines.map
import app.deckbox.core.coroutines.mapResult
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.extensions.prependIfNotEmpty
import app.deckbox.core.logging.bark
import app.deckbox.core.model.Card
import app.deckbox.core.model.Deck
import app.deckbox.core.model.Evolution
import app.deckbox.core.model.Stacked
import app.deckbox.core.model.SuperType
import app.deckbox.features.decks.api.DeckRepository
import app.deckbox.tournament.api.TournamentRepository
import app.deckbox.tournament.api.usecase.FetchDeckListCardsUseCase
import cafe.adriel.lyricist.LocalStrings
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.popUntil
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, DeckListScreen::class)
@Inject
class DeckListPresenter(
  @Assisted private val navigator: Navigator,
  @Assisted private val screen: DeckListScreen,
  private val tournamentRepository: TournamentRepository,
  private val deckRepository: DeckRepository,
  private val fetchDeckListCardsUseCase: FetchDeckListCardsUseCase,
  private val dispatcherProvider: DispatcherProvider,
) : Presenter<DeckListUiState> {

  private var importState by mutableStateOf<LoadState<out Deck>?>(null)

  @Composable
  override fun present(): DeckListUiState {
    val coroutineScope = rememberCoroutineScope()

    val deckListState by remember {
      flow {
        val result = tournamentRepository.getDeckList(screen.deckListId)
        if (result.isSuccess) {
          bark { "DeckList Loaded! Prices: ${result.getOrThrow().price}" }
          emit(LoadState.Loaded(result.getOrThrow()))
        } else {
          emit(LoadState.Error)
        }
      }
    }.collectAsState(LoadState.Loading)

    val cards = remember(deckListState) {
      flow {
        val cardsLoadState = deckListState.mapResult { deckList ->
          fetchDeckListCardsUseCase.execute(deckList).also {
            bark { "Fetch DeckList Cards Result: $it" }
          }
        }
        emit(cardsLoadState)
      }
    }.collectAsState(LoadState.Loading)

    // Convert these into UI models we can use to render the UI
    val uiModels by convertToUiModels(cards)

    return DeckListUiState(
      archetypeName = screen.archetypeName,
      deckListState = deckListState,
      cardsLoadState = uiModels,
      importEnabled = cards.value is LoadState.Loaded,
      importState = importState,
    ) { event ->
      when (event) {
        DeckListUiEvent.NavigateBack -> navigator.pop()
        DeckListUiEvent.Import -> coroutineScope.launch {
          val stackedCards = cards.value.dataOrNull ?: return@launch
          importDeck(stackedCards)
        }
        is DeckListUiEvent.CardClick -> navigator.goTo(CardDetailScreen(event.card))
        is DeckListUiEvent.PurchaseDeck -> navigator.goTo(UrlScreen(event.bulkPurchaseUrl))
      }
    }
  }

  @Composable
  private fun convertToUiModels(
    cardsLoadState: State<LoadState<out List<Stacked<Card>>>>,
  ): State<LoadState<out ImmutableList<CardUiModel>>> {
    return remember {
      snapshotFlow {
        cardsLoadState.value.map { cards ->
          val pokemon = mutableListOf<CardUiModel>()
          val trainers = mutableListOf<CardUiModel>()
          val energy = mutableListOf<CardUiModel>()

          val evolutions = Evolution.create(cards)
          evolutions.forEach { evolution ->
            if (evolution.size == 1) {
              evolution.firstNodeCards().forEach { card ->
                when (card.card.supertype) {
                  SuperType.ENERGY -> energy += CardUiModel.Single(card)
                  SuperType.TRAINER -> trainers += CardUiModel.Single(card)
                  else -> pokemon += CardUiModel.Single(card)
                }
              }
            } else {
              pokemon += CardUiModel.EvolutionLine(evolution)
            }
          }

          pokemon.apply {
            sortBy {
              when (it) {
                is CardUiModel.EvolutionLine -> it.evolution.firstNodeCards().first().card.name
                is CardUiModel.Single -> "zzzz${it.card.card.name}"
                else -> BottomSortName
              }
            }
          }

          trainers.apply {
            sortBy {
              when (it) {
                is CardUiModel.Single -> it.card.card.name
                else -> BottomSortName
              }
            }
          }

          energy.apply {
            sortBy {
              when (it) {
                is CardUiModel.Single -> it.card.card.name
                else -> BottomSortName
              }
            }
          }

          // Concatenate the models
          pokemon.prependIfNotEmpty(
            CardUiModel.SectionHeader(
              superType = SuperType.POKEMON,
              count = pokemon.sumOf { it.size },
              title = { LocalStrings.current.deckListHeaderPokemon },
            ),
          ) + trainers.prependIfNotEmpty(
            CardUiModel.SectionHeader(
              superType = SuperType.TRAINER,
              count = trainers.sumOf { it.size },
              title = { LocalStrings.current.deckListHeaderTrainer },
            ),
          ) + energy.prependIfNotEmpty(
            CardUiModel.SectionHeader(
              superType = SuperType.ENERGY,
              count = energy.sumOf { it.size },
              title = { LocalStrings.current.deckListHeaderEnergy },
            ),
          )
        }.map { it.toImmutableList() }
      }.flowOn(dispatcherProvider.computation)
    }.collectAsState(LoadState.Loading)
  }

  private suspend fun importDeck(cards: List<Stacked<Card>>) {
    // TODO: We should add source meta data to decks so we can indicate/track where a deck was imported
    //  from and respect the source affiliate Urls
    val newDeckId = deckRepository.importDeck(
      name = screen.archetypeName,
      cards = cards,
    )

    navigator.popUntil { it !is ImportScreen }
    navigator.goTo(DeckBuilderScreen(newDeckId))
  }
}

private const val BottomSortName = "zzzzzzzzzzzz"
