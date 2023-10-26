package app.deckbox.common.compose.widgets.builder.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShortText
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
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
  name: TextFieldValue,
  isValid: Boolean,
  legalities: Legalities,
  focusRequester: FocusRequester,
  cardsState: LoadState<out ImmutableList<CardUiModel>>,
  onHeaderClick: () -> Unit,
  onNameChange: (TextFieldValue) -> Unit,
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

    BuilderTextField(
      icon = { Icon(Icons.Rounded.ShortText, contentDescription = null) },
    ) {
      TextField(
        value = name,
        onValueChange = { value ->
          onNameChange(value)
        },
        placeholder = {
          Text("Name")
        },
        label = {
          Text("Name")
        },
        modifier = Modifier
          .fillMaxWidth()
          .focusRequester(focusRequester),
      )
    }
    Spacer(Modifier.height(16.dp))
    content()
  }

  Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
}
