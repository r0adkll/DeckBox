package app.deckbox.features.cards.ui.pager

import androidx.compose.runtime.Stable
import app.deckbox.common.screens.CardDetailScreen
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator

@Stable
class CardDetailPagerUiState(
  val navigator: Navigator,
  val detailPage: CardDetailPage,
  val eventSink: (CardDetailPagerUiEvent) -> Unit,
) : CircuitUiState

data class CardDetailPage(
  val initialScreen: CardDetailScreen,
  val screens: List<CardDetailScreen>,
) {
  val initialIndex: Int get() = screens.indexOfFirst { it.cardId == initialScreen.cardId }
  val size: Int get() = screens.size
}

sealed interface CardDetailPagerUiEvent : CircuitUiEvent {
  data object NavigateBack : CardDetailPagerUiEvent
}
