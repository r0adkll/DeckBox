package app.deckbox.ui.decks.builder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import app.deckbox.common.screens.BrowseScreen
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.common.screens.DeckBuilderScreen
import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.extensions.prependFilterIfNotEmpty
import app.deckbox.core.extensions.prependIfNotEmpty
import app.deckbox.core.extensions.readableFormat
import app.deckbox.core.model.Card
import app.deckbox.core.model.Evolution
import app.deckbox.core.model.SuperType
import app.deckbox.features.cards.public.CardRepository
import app.deckbox.features.decks.api.builder.DeckBuilderRepository
import app.deckbox.features.decks.api.validation.DeckValidation
import app.deckbox.features.decks.api.validation.DeckValidator
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.AddCards
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.AddTag
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.CardClick
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.DecrementCard
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.EditDescription
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.EditName
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.IncrementCard
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.NavigateBack
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.RemoveCard
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.RemoveTag
import app.deckbox.ui.decks.builder.model.CardUiModel
import app.deckbox.ui.decks.builder.model.CardUiModel.Tip
import cafe.adriel.lyricist.LocalStrings
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
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
  private val dispatcherProvider: DispatcherProvider,
) : Presenter<DeckBuilderUiState> {

  @OptIn(ExperimentalCoroutinesApi::class)
  @Composable
  override fun present(): DeckBuilderUiState {
    val sessionId = rememberSaveable { screen.id ?: repository.createSession() }

    val session by remember {
      repository.observeSession(sessionId)
        .map { DeckSession.Loaded(it) }
        .catch { DeckSession.Error }
    }.collectAsState(DeckSession.Loading)

    val sessionCards by remember(sessionId) {
      cardRepository.observeCardsForDeck(sessionId)
    }.collectAsState(emptyList())

    // Split the cards by supertype and build the pokemon into evolution lines
    val uiModels by remember {
      snapshotFlow {
        val split = sessionCards.groupBy { it.card.supertype }

        val pokemon = split[SuperType.POKEMON]
          ?.let { Evolution.create(it) }
          ?.flatMap { evolution ->
            if (evolution.size == 1) {
              evolution.nodes.first().cards.map { CardUiModel.Single(it) }
            } else {
              listOf(CardUiModel.EvolutionLine(evolution))
            }
          }
          ?.sortedBy {
            when (it) {
              is CardUiModel.EvolutionLine -> 0
              else -> 1
            }
          }
          ?: listOf(Tip.Pokemon)

        val trainers = split[SuperType.TRAINER]
          ?.map { CardUiModel.Single(it) }
          ?: listOf(Tip.Trainer)

        val energy = split[SuperType.ENERGY]
          ?.map { CardUiModel.Single(it) }
          ?: listOf(Tip.Energy)

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
      }.flowOn(dispatcherProvider.computation)
    }.collectAsState(emptyList())

    val validation by remember {
      snapshotFlow { sessionCards }
        .mapLatest { deckValidator.validate(it) }
        .flowOn(dispatcherProvider.computation)
    }.collectAsState(DeckValidation())

    val deckPrice by remember {
      snapshotFlow {
        var oldestTcgPlayerUpdatedAt = LocalDate(3000, 1, 1)
        var tcgPlayerMarketLow = 0.0
        sessionCards.forEach { stack ->
          val cardPrice = stack.card.tcgPlayer?.prices?.lowestMarketPrice() ?: 0.0
          tcgPlayerMarketLow += stack.count * cardPrice
          stack.card.tcgPlayer?.updatedAt?.let { date ->
            if (date < oldestTcgPlayerUpdatedAt) oldestTcgPlayerUpdatedAt = date
          }
        }

        var oldestCardMarketUpdatedAt = LocalDate(3000, 1, 1)
        var cardMarketLow = 0.0
        sessionCards.forEach { stack ->
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
      }.flowOn(dispatcherProvider.computation)
    }.collectAsState(DeckPriceState(null, null))

    return DeckBuilderUiState(
      session = session,
      cards = uiModels,
      price = deckPrice,
      validation = validation,
      eventSink = { event ->
        onEvent(sessionId, event)
      },
    )
  }

  private fun onEvent(deckId: String, event: DeckBuilderUiEvent) {
    when (event) {
      NavigateBack -> navigator.pop()
      is AddCards -> navigator.goTo(BrowseScreen(deckId = deckId, superType = event.superType))
      is CardClick -> navigator.goTo(CardDetailScreen(event.card, deckId))

      is EditName -> repository.editName(deckId, event.name)
      is EditDescription -> repository.editDescription(deckId, event.description)
      is AddTag -> repository.addTag(deckId, event.tag)
      is RemoveTag -> repository.removeTag(deckId, event.tag)
      is IncrementCard -> repository.incrementCard(deckId, event.cardId, event.amount)
      is DecrementCard -> repository.decrementCard(deckId, event.cardId, event.amount)
      is RemoveCard -> repository.removeCard(deckId, event.cardId)
    }
  }

  private fun Card.TcgPlayer.Prices.lowestMarketPrice(): Double? {
    var lowestPrice = Double.MAX_VALUE
    fun Card.TcgPlayer.Price.checkLowest() {
      market?.let {
        if (it < lowestPrice) lowestPrice = it
      }
    }

    normal?.checkLowest()
    holofoil?.checkLowest()
    reverseHolofoil?.checkLowest()
    firstEditionNormal?.checkLowest()
    firstEditionHolofoil?.checkLowest()

    return lowestPrice.takeIf { it != Double.MAX_VALUE }
  }
}
