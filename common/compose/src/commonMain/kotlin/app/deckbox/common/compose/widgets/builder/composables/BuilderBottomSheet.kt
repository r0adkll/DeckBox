package app.deckbox.common.compose.widgets.builder.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShortText
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.TextFieldValue
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
  cardsState: LoadState<out ImmutableList<CardUiModel>>,
  onHeaderClick: () -> Unit,
  content: @Composable ColumnScope.() -> Unit,
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
    content()
  }

  Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
}
