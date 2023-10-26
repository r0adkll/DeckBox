package app.deckbox.ui.decks.builder.composables

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Subject
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.widgets.builder.composables.BuilderTextField
import app.deckbox.features.decks.api.validation.DeckValidation
import app.deckbox.features.decks.api.validation.Validation
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent
import app.deckbox.ui.decks.builder.DeckBuilderUiState
import app.deckbox.ui.decks.builder.DeckPriceState

@Composable
internal fun ColumnScope.DeckBuilderBottomSheet(
  state: DeckBuilderUiState,
) {
  val eventSink = state.eventSink
  val validation = state.validation.dataOrNull ?: DeckValidation()
  val price = state.price.dataOrNull ?: DeckPriceState()

  var descriptionValue by remember(state.session.deckOrNull() != null) {
    mutableStateOf(TextFieldValue(state.session.deckOrNull()?.description ?: ""))
  }
  BuilderTextField(
    icon = { Icon(Icons.Rounded.Subject, contentDescription = null) },
  ) {
    TextField(
      value = descriptionValue,
      onValueChange = { value ->
        descriptionValue = value
        eventSink(DeckBuilderUiEvent.EditDescription(value.text))
      },
      placeholder = {
        Text("Description")
      },
      minLines = 3,
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done,
      ),
      modifier = Modifier.fillMaxWidth(),
    )
  }

  Spacer(Modifier.height(16.dp))
  Divider()

  var collectionMode by remember { mutableStateOf(false) }
  ListItem(
    headlineContent = {
      Text("Only show cards in collection")
    },
    trailingContent = {
      Switch(
        checked = collectionMode,
        onCheckedChange = {
          collectionMode = it
        },
      )
    },
  )

  DeckPrices(
    prices = price,
  )

  Spacer(Modifier.height(16.dp))

  validation.ruleValidations
    .filterIsInstance<Validation.Invalid>()
    .forEach { invalid ->
      ListItem(
        headlineContent = { Text(invalid.reason) },
        leadingContent = {
          Icon(Icons.Rounded.ErrorOutline, contentDescription = null)
        },
        colors = ListItemDefaults.colors(
          headlineColor = MaterialTheme.colorScheme.error,
          leadingIconColor = MaterialTheme.colorScheme.error,
        ),
      )
    }
}
