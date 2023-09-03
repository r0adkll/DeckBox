package app.deckbox.features.boosterpacks.ui.builder.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import app.deckbox.core.model.Legalities
import app.deckbox.core.model.Legality
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiEvent.EditName
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiState
import com.moriatsushi.insetsx.navigationBars

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ColumnScope.BoosterPackBottomSheet(
  state: BoosterPackBuilderUiState,
  focusRequester: FocusRequester,
  onHeaderClick: () -> Unit,
) {
  SheetHeader(
    totalCount = state.cards.sumOf { it.size },
    legalities = state.session.boosterPackOrNull()?.legalities
      ?: Legalities(standard = Legality.LEGAL),
    onHeaderClick = onHeaderClick,
  )
  Column(
    modifier = Modifier
      .padding(vertical = 16.dp)
      .focusGroup()
      .verticalScroll(rememberScrollState()),
  ) {
    var nameValue by remember(state.session.boosterPackOrNull() != null) {
      mutableStateOf(TextFieldValue(state.session.boosterPackOrNull()?.name ?: ""))
    }
    IconTextField(
      icon = { Icon(Icons.Rounded.ShortText, contentDescription = null) },
    ) {
      TextField(
        value = nameValue,
        onValueChange = { value ->
          nameValue = value
          state.eventSink(EditName(value.text))
        },
        placeholder = {
          Text("Name")
        },
        colors = TextFieldDefaults.colors(
          cursorColor = MaterialTheme.colorScheme.secondary,
          focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
          focusedLabelColor = MaterialTheme.colorScheme.secondary,
        ),
        modifier = Modifier
          .fillMaxWidth()
          .focusRequester(focusRequester),
      )
    }

    Spacer(Modifier.height(16.dp))
    Divider()

    PackPrices(
      prices = state.price,
    )

    Spacer(Modifier.height(16.dp))
  }
  Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
}

@Composable
private fun IconTextField(
  modifier: Modifier = Modifier,
  icon: @Composable () -> Unit,
  textField: @Composable () -> Unit,
) {
  Row(
    modifier = modifier
      .padding(end = 16.dp),
  ) {
    Box(
      modifier = Modifier.padding(16.dp),
    ) {
      icon()
    }
    textField()
  }
}
