package app.deckbox.features.boosterpacks.ui.builder.composables

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShortText
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.widgets.builder.composables.BuilderTextField
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiEvent
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiState
import app.deckbox.features.boosterpacks.ui.builder.PackPriceState

@Composable
internal fun ColumnScope.BoosterPackBottomSheet(
  state: BoosterPackBuilderUiState,
  focusRequester: FocusRequester,
) {
  val eventSink = state.eventSink
  var nameValue by remember(state.session.boosterPackOrNull() != null) {
    mutableStateOf(TextFieldValue(state.session.boosterPackOrNull()?.name ?: ""))
  }
  BuilderTextField(
    icon = { Icon(Icons.Rounded.ShortText, contentDescription = null) },
  ) {
    TextField(
      value = nameValue,
      onValueChange = { value ->
        nameValue = value
        eventSink(BoosterPackBuilderUiEvent.EditName(value.text))
      },
      placeholder = {
        Text("Name")
      },
      label = {
        Text("Name")
      },
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done,
      ),
      singleLine = true,
      modifier = Modifier
        .fillMaxWidth()
        .focusRequester(focusRequester)
    )
  }
  Spacer(Modifier.height(16.dp))
  Divider()

  PackPrices(
    prices = state.price.dataOrNull ?: PackPriceState(),
  )

  Spacer(Modifier.height(16.dp))
}
