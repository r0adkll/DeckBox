package app.deckbox.ui.decks.builder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import app.deckbox.common.compose.widgets.builder.model.CardUiModel
import app.deckbox.common.compose.widgets.builder.model.CardUiModel.Tip
import app.deckbox.common.compose.widgets.builder.model.CardUiModel.Tip.Energy.Companion.DefaultEnergyAmount
import app.deckbox.common.screens.BoosterPackBuilderScreen
import app.deckbox.common.screens.BrowseScreen
import app.deckbox.common.screens.CardDetailPagerScreen
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.common.screens.DeckBuilderScreen
import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.coroutines.map
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.extensions.addIfEmpty
import app.deckbox.core.extensions.lowestMarketPrice
import app.deckbox.core.extensions.prependIfNotEmpty
import app.deckbox.core.extensions.readableFormat
import app.deckbox.core.model.Card
import app.deckbox.core.model.Evolution
import app.deckbox.core.model.Stacked
import app.deckbox.core.model.SuperType
import app.deckbox.core.model.Type
import app.deckbox.core.model.energyTypeFromCardName
import app.deckbox.core.model.map
import app.deckbox.core.model.stack
import app.deckbox.features.cards.public.CardRepository
import app.deckbox.features.cards.public.usecase.BasicEnergyUseCase
import app.deckbox.features.decks.api.builder.DeckBuilderRepository
import app.deckbox.features.decks.api.validation.DeckValidator
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.AddBoosterPack
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.AddCards
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.AddTag
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.CardClick
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.DecrementCard
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.EditDescription
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.EditName
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.IncrementCard
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.NavigateBack
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.NewBoosterPack
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.RemoveCard
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.RemoveTag
import cafe.adriel.lyricist.LocalStrings
import com.benasher44.uuid.uuid4
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.LocalDate
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, DeckBuilderScreen::class)
@Inject
class DeckBuilderPresenter(
  @Assisted private val navigator: Navigator,
  @Assisted private val screen: DeckBuilderScreen,
  private val repository: DeckBuilderRepository,
  private val cardRepository: CardRepository,
  private val deckValidator: DeckValidator,
  private val basicEnergyUseCase: BasicEnergyUseCase,
  private val dispatcherProvider: DispatcherProvider,
) : Presenter<DeckBuilderUiState> {

  @OptIn(ExperimentalCoroutinesApi::class)
  @Composable
  override fun present(): DeckBuilderUiState {
    val session by remember {
      repository.observeSession(screen.id)
        .map { DeckSession.Loaded(it) }
        .catch { DeckSession.Error }
    }.collectAsState(DeckSession.Loading)

    val sessionCards by remember(screen.id) {
      cardRepository.observeCardsForDeck(screen.id)
        .map { LoadState.Loaded(it.toImmutableList()) }
        .catch { LoadState.Error }
    }.collectAsState(LoadState.Loading)

    val basicEnergyCards by remember(screen.id) {
      flow {
        val result = basicEnergyUseCase.execute()
        emit(result.getOrNull() ?: emptyList())
      }
    }.collectAsState(emptyList())

    // Split the cards by supertype and build the pokemon into evolution lines
    val uiModels by remember {
      snapshotFlow {
        sessionCards.map { cards ->
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
          }.addIfEmpty(Tip.Pokemon)

          trainers.apply {
            sortBy {
              when (it) {
                is CardUiModel.Single -> it.card.card.name
                else -> BottomSortName
              }
            }
          }.addIfEmpty(Tip.Trainer)

          energy.apply {
            sortBy {
              when (it) {
                is CardUiModel.Single -> it.card.card.name
                else -> BottomSortName
              }
            }
          }.addIfEmpty(computeEnergyTip(cards, basicEnergyCards))

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

    val validation by remember {
      snapshotFlow { sessionCards }
        .mapLatest {
          it.map { cards ->
            deckValidator.validate(cards)
          }
        }
        .flowOn(dispatcherProvider.computation)
    }.collectAsState(LoadState.Loading)

    val deckPrice by remember {
      snapshotFlow {
        sessionCards.map { cards ->
          var oldestTcgPlayerUpdatedAt = LocalDate(3000, 1, 1)
          var tcgPlayerMarketLow = 0.0
          cards.forEach { stack ->
            val cardPrice = stack.card.tcgPlayer?.prices?.lowestMarketPrice() ?: 0.0
            tcgPlayerMarketLow += stack.count * cardPrice
            stack.card.tcgPlayer?.updatedAt?.let { date ->
              if (date < oldestTcgPlayerUpdatedAt) oldestTcgPlayerUpdatedAt = date
            }
          }

          var oldestCardMarketUpdatedAt = LocalDate(3000, 1, 1)
          var cardMarketLow = 0.0
          cards.forEach { stack ->
            val cardPrice = stack.card.cardMarket?.prices?.averageSellPrice ?: 0.0
            cardMarketLow += stack.count * cardPrice
            stack.card.cardMarket?.updatedAt?.let { date ->
              if (date < oldestCardMarketUpdatedAt) oldestCardMarketUpdatedAt = date
            }
          }

          DeckPriceState(
            tcgPlayer = tcgPlayerMarketLow
              .takeIf { it > 0.0 }
              ?.let { marketPrice ->
                DeckPrice(
                  lastUpdated = oldestTcgPlayerUpdatedAt.readableFormat,
                  market = marketPrice,
                )
              },
            cardMarket = cardMarketLow
              .takeIf { it > 0.0 }
              ?.let { marketPrice ->
                DeckPrice(
                  lastUpdated = oldestCardMarketUpdatedAt.readableFormat,
                  market = marketPrice,
                )
              },
          )
        }
      }.flowOn(dispatcherProvider.computation)
    }.collectAsState(LoadState.Loading)

    return DeckBuilderUiState(
      session = session,
      cards = uiModels,
      price = deckPrice,
      validation = validation,
      eventSink = { event ->
        onEvent(screen.id, event)
      },
    )
  }

  private fun onEvent(deckId: String, event: DeckBuilderUiEvent) {
    when (event) {
      NavigateBack -> navigator.pop()
      is AddCards -> navigator.goTo(BrowseScreen(deckId = deckId, superType = event.superType))
      is CardClick -> navigator.goTo(
        CardDetailPagerScreen(
          pagedCards = CardDetailPagerScreen.PagedCards.AsDeck(
            initialCard = CardDetailScreen(event.card, deckId),
            deckId = deckId,
          ),
        ),
      )

      is EditName -> repository.editName(deckId, event.name)
      is EditDescription -> repository.editDescription(deckId, event.description)
      is AddTag -> repository.addTag(deckId, event.tag)
      is RemoveTag -> repository.removeTag(deckId, event.tag)
      is IncrementCard -> repository.incrementCard(deckId, event.cardId, event.amount)
      is DecrementCard -> repository.decrementCard(deckId, event.cardId, event.amount)
      is AddBoosterPack -> repository.addBoosterPack(deckId, event.pack.id)
      is NewBoosterPack -> navigator.goTo(BoosterPackBuilderScreen(uuid4().toString()))
      is RemoveCard -> repository.removeCard(deckId, event.cardId)
    }
  }

  private fun computeEnergyTip(
    cards: ImmutableList<Stacked<Card>>,
    basicEnergyCards: List<Card>,
  ): Tip.Energy {
    val sum = cards.sumOf { it.count }
    val pokemon = cards.filter { it.card.supertype == SuperType.POKEMON }
    val energy = cards.filter { it.card.supertype == SuperType.ENERGY }

    if (pokemon.isEmpty()) return Tip.Energy.Default
    if (energy.isNotEmpty()) return Tip.Energy.Default

    // Compute the number of cards we can suggest
    val count = (60 - sum).coerceIn(0..DefaultEnergyAmount)
    if (count == 0) return Tip.Energy.Default

    // Compute the basic energy type we want to suggest
    val type = pokemon
      .mapNotNull {
        if (it.card.types.isNullOrEmpty()) {
          null
        } else {
          it.map { it.types!! }
        }
      }
      .flatMap { stack ->
        stack.card.map { type ->
          type.stack(stack.count)
        }
      }
      .groupingBy { it.card }
      .aggregate<Stacked<Type>, Type, Int> { _, accumulator, element, _ ->
        (accumulator ?: 0) + element.count
      }
      .maxByOrNull { it.value }
      ?.key

    // If we couldn't determine a type, then just exit to default
    if (type == null) return Tip.Energy.Default

    // Find Basic Energy Card for Type
    val basicEnergyCard = basicEnergyCards.find {
      energyTypeFromCardName(it.name) == type
    } ?: return Tip.Energy.Default

    return Tip.Energy.Suggested(basicEnergyCard.stack(count))
  }
}

private const val BottomSortName = "zzzzzzzzzzzz"
