package app.deckbox.features.cards.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.features.cards.public.CardRepository
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, CardDetailScreen::class)
@Inject
class CardDetailPresenter(
  @Assisted private val navigator: Navigator,
  @Assisted private val screen: CardDetailScreen,
  private val repository: CardRepository,
) : Presenter<CardDetailUiState> {

  @Composable
  override fun present(): CardDetailUiState {
    val cardLoadState by remember {
      flow {
        val card = repository.getCard(screen.cardId)
        emit(
          card?.let { LoadState.Loaded(card) }
            ?: LoadState.Error("Unable to load card for ${screen.cardId}"),
        )
      }
    }.collectAsState(LoadState.Loading)

    // TODO Load additional card information such as evolution info, similar cards, etc

    return when (val state = cardLoadState) {
      LoadState.Loading -> CardDetailUiState.Loading
      is LoadState.Error -> CardDetailUiState.Error(state.message)
      is LoadState.Loaded -> CardDetailUiState.Loaded(
        card = state.data,
      ) { event ->
        when (event) {
          CardDetailUiEvent.NavigateBack -> navigator.pop()
        }
      }
    }
  }
}
