package app.deckbox.common.compose.widgets.builder.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.widgets.builder.model.CardUiModel
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.model.Legalities
import app.deckbox.core.model.SuperType
import com.moriatsushi.insetsx.navigationBars
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ColumnScope.BuilderBottomSheet(
  isValid: Boolean,
  legalities: Legalities,
  focusRequester: FocusRequester,
  cardsState: LoadState<out ImmutableList<CardUiModel>>,
  onHeaderClick: () -> Unit,
  content: @Composable ColumnScope.(FocusRequester) -> Unit,
) {
  val cards = cardsState.dataOrNull ?: persistentListOf()

  SheetHeader(
    isValid = isValid,
    totalCount = cards.sumOf { it.size },
    pokemonCount = cards.sumOf { it.sizeOf(SuperType.POKEMON) },
    trainerCount = cards.sumOf { it.sizeOf(SuperType.TRAINER) },
    energyCount = cards.sumOf { it.sizeOf(SuperType.ENERGY) },
    legalities = legalities,
    onHeaderClick = onHeaderClick,
  )
  Column(
    modifier = Modifier
      .padding(vertical = 16.dp)
      .focusGroup()
      .verticalScroll(rememberScrollState()),
  ) {
    content(focusRequester)
  }

  Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
}
