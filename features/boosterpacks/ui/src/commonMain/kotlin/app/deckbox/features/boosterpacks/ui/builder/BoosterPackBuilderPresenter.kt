package app.deckbox.features.boosterpacks.ui.builder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import app.deckbox.common.screens.BoosterPackBuilderScreen
import app.deckbox.common.screens.BrowseScreen
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.extensions.lowestMarketPrice
import app.deckbox.core.extensions.prependIfNotEmpty
import app.deckbox.core.extensions.readableFormat
import app.deckbox.core.model.SuperType
import app.deckbox.features.boosterpacks.api.BoosterPackRepository
import app.deckbox.features.boosterpacks.ui.builder.model.CardUiModel
import app.deckbox.features.cards.public.CardRepository
import cafe.adriel.lyricist.LocalStrings
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, BoosterPackBuilderScreen::class)
@Inject
class BoosterPackBuilderPresenter(
  @Assisted private val navigator: Navigator,
  @Assisted private val screen: BoosterPackBuilderScreen,
  private val repository: BoosterPackRepository,
  private val cardRepository: CardRepository,
  private val dispatcherProvider: DispatcherProvider,
) : Presenter<BoosterPackBuilderUiState> {

  @Composable
  override fun present(): BoosterPackBuilderUiState {
    val session by remember {
      repository.observeBoosterPack(screen.id)
        .map { BoosterPackSession.Loaded(it) }
        .catch { BoosterPackSession.Error }
    }.collectAsState(BoosterPackSession.Loading)

    val sessionCards by remember {
      cardRepository.observeCardsForBoosterPack(screen.id)
    }.collectAsState(emptyList())

    val uiModels by remember {
      snapshotFlow {
        val split = sessionCards.groupBy { it.card.supertype }

        val pokemon = split[SuperType.POKEMON]
          ?.map { CardUiModel.Single(it) }
          ?: emptyList()

        val trainers = split[SuperType.TRAINER]
          ?.map { CardUiModel.Single(it) }
          ?: emptyList()

        val energy = split[SuperType.ENERGY]
          ?.map { CardUiModel.Single(it) }
          ?: emptyList()

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

    val packPrice by remember {
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

        PackPriceState(
          tcgPlayer = tcgPlayerMarketLow
            .takeIf { it > 0.0 }
            ?.let { marketPrice ->
              PackPrice(
                lastUpdated = oldestTcgPlayerUpdatedAt.readableFormat,
                market = marketPrice,
              )
            },
          cardMarket = cardMarketLow
            .takeIf { it > 0.0 }
            ?.let { marketPrice ->
              PackPrice(
                lastUpdated = oldestCardMarketUpdatedAt.readableFormat,
                market = marketPrice,
              )
            },
        )
      }.flowOn(dispatcherProvider.computation)
    }.collectAsState(PackPriceState())

    return BoosterPackBuilderUiState(
      session = session,
      cards = uiModels,
      price = packPrice,
    ) { event ->
      when (event) {
        BoosterPackBuilderUiEvent.NavigateBack -> navigator.pop()
        is BoosterPackBuilderUiEvent.AddCards -> navigator.goTo(
          BrowseScreen(
            packId = screen.id,
            superType = event.superType,
          ),
        )

        is BoosterPackBuilderUiEvent.CardClick -> navigator.goTo(CardDetailScreen(event.card, packId = screen.id))
        is BoosterPackBuilderUiEvent.EditName -> repository.editName(screen.id, event.name)
        is BoosterPackBuilderUiEvent.IncrementCard -> repository.incrementCard(screen.id, event.cardId, event.amount)
        is BoosterPackBuilderUiEvent.DecrementCard -> repository.decrementCard(screen.id, event.cardId, event.amount)
      }
    }
  }
}
