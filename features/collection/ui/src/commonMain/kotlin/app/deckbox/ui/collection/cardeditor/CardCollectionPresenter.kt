package app.deckbox.ui.collection.cardeditor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import app.deckbox.common.screens.CardCollectionEditorScreen
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.coroutines.map
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Card
import app.deckbox.core.model.CollectionCount
import app.deckbox.features.cards.public.CardRepository
import app.deckbox.features.collection.api.CollectionRepository
import app.deckbox.ui.collection.cardeditor.CardCollectionEditorUiEvent.Close
import app.deckbox.ui.collection.cardeditor.CardCollectionEditorUiEvent.Decrement
import app.deckbox.ui.collection.cardeditor.CardCollectionEditorUiEvent.Increment
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

private const val DefaultIncrementAmount = 1
private const val DefaultDecrementAmount = -1

@CircuitInject(MergeActivityScope::class, CardCollectionEditorScreen::class)
@Inject
class CardCollectionPresenter(
  @Assisted private val navigator: Navigator,
  @Assisted private val screen: CardCollectionEditorScreen,
  private val cardRepository: CardRepository,
  private val collectionRepository: CollectionRepository,
) : Presenter<CardCollectionEditorUiState> {

  @Composable
  override fun present(): CardCollectionEditorUiState {
    val coroutineScope = rememberCoroutineScope()

    val cardLoadState by loadCard(screen.cardId)

    val collectionCount by remember {
      collectionRepository.observeCollectionForCard(screen.cardId)
    }.collectAsState(CollectionCount(screen.cardId))

    val variants by remember {
      derivedStateOf {
        val counts = collectionCount
        when (cardLoadState) {
          LoadState.Loading -> LoadState.Loading
          LoadState.Error -> LoadState.Error
          is LoadState.Loaded -> cardLoadState.map { card ->
            val potentialVariants = Card.Variant.entries.mapNotNull { variant ->
              val variantPrice = card.tcgPlayer?.forVariant(variant)
              val variantCount = counts.forVariant(variant)
              if (variantPrice != null || variantCount > 0) {
                variant
              } else {
                null
              }
            }

            if (potentialVariants.isEmpty() && card.tcgPlayer?.prices?.isEmpty == true) {
              listOf(Card.Variant.Normal)
            } else {
              potentialVariants
            }
          }
        }
      }
    }

    return CardCollectionEditorUiState(
      cardName = screen.cardName,
      cardImageUrl = screen.cardImageLarge,
      variants = variants,
      collectionCount = collectionCount,
    ) { event ->
      when (event) {
        Close -> navigator.pop()
        is Increment -> coroutineScope.launch {
          incrementCardCollection(event.variant, DefaultIncrementAmount)
        }
        is Decrement -> coroutineScope.launch {
          incrementCardCollection(event.variant, DefaultDecrementAmount)
        }
      }
    }
  }

  private suspend fun incrementCardCollection(
    variant: Card.Variant,
    amount: Int,
  ) {
    collectionRepository.incrementCounts(
      cardId = screen.cardId,
      normal = variant.amountIf(Card.Variant.Normal, amount),
      holofoil = variant.amountIf(Card.Variant.Holofoil, amount),
      reverseHolofoil = variant.amountIf(Card.Variant.ReverseHolofoil, amount),
      firstEditionNormal = variant.amountIf(Card.Variant.FirstEditionNormal, amount),
      firstEditionHolofoil = variant.amountIf(Card.Variant.FirstEditionHolofoil, amount),
    )
  }

  @Composable
  private fun loadCard(id: String): State<LoadState<out Card>> {
    return rememberRetained {
      flow {
        val card = cardRepository.getCard(id)
        emit(
          card?.let { LoadState.Loaded(card) }
            ?: LoadState.Error,
        )
      }
    }.collectAsRetainedState(LoadState.Loading)
  }
}
