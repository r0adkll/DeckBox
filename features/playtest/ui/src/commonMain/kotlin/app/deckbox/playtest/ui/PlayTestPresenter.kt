package app.deckbox.playtest.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.deckbox.common.screens.PlayTestScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Card
import app.deckbox.core.model.flatten
import app.deckbox.features.cards.public.CardRepository
import app.deckbox.features.decks.api.DeckRepository
import app.deckbox.features.decks.api.validation.DeckValidator
import app.deckbox.playtest.ui.model.Board
import app.deckbox.playtest.ui.model.newGame
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, PlayTestScreen::class)
@Inject
class PlayTestPresenter(
  @Assisted private val navigator: Navigator,
  @Assisted private val screen: PlayTestScreen,
  private val deckValidator: DeckValidator,
  private val cardRepository: CardRepository,
) : Presenter<PlayTestUiState> {

  @Composable
  override fun present(): PlayTestUiState {
    val deckState by fetchAndValidateDeckState()

    return when (val deck = deckState) {
      DeckState.Loading -> PlayTestUiState.Loading
      is DeckState.Error -> PlayTestUiState.Error(deck.validation)
      is DeckState.Loaded -> {
        var board by remember { mutableStateOf(newGame(deck.cards)) }

        PlayTestUiState.InGame(
          board = board,
        ) { event ->
          when (event) {
            is PlayTestUiEvent.BoardAction -> {
              board = event.action.apply(board)
            }
          }
        }
      }
    }
  }

  /**
   * Fetch the list of cards for the given deck and validate them
   */
  @Composable
  private fun fetchAndValidateDeckState(): State<DeckState> {
    val cardsState: MutableState<DeckState> = remember { mutableStateOf(DeckState.Loading) }

    LaunchedEffect(Unit) {
      val cards = cardRepository.observeCardsForDeck(screen.deckId)
        .first()

      val validation = deckValidator.validate(cards)

      if (validation.isValid && validation.isNotEmpty) {
        cardsState.value = DeckState.Loaded(
          cards = cards.flatten().toImmutableList()
        )
      } else {
        cardsState.value = DeckState.Error(
          validation = validation,
        )
      }
    }

    return cardsState
  }
}
