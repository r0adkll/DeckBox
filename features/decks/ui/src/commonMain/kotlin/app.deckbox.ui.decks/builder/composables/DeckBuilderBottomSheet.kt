package app.deckbox.ui.decks.builder.composables

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShortText
import androidx.compose.material.icons.rounded.Subject
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
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
import app.deckbox.core.model.Legalities
import app.deckbox.core.model.Legality
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent
import app.deckbox.ui.decks.builder.DeckBuilderUiState
import com.moriatsushi.insetsx.navigationBars

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ColumnScope.DeckBuilderBottomSheet(
  state: DeckBuilderUiState,
  focusRequester: FocusRequester,
  onHeaderClick: () -> Unit,
) {
  SheetHeader(
    totalCount = state.cards.sumOf { it.size },
    legalities = state.session.deckOrNull()?.legalities
      ?: Legalities(standard = Legality.LEGAL),
    onHeaderClick = onHeaderClick,
  )
  Column(
    modifier = Modifier
      .padding(vertical = 16.dp)
      .focusGroup(),
  ) {
    var nameValue by remember(state.session.deckOrNull() != null) {
      mutableStateOf(TextFieldValue(state.session.deckOrNull()?.name ?: ""))
    }
    DeckTextField(
      icon = { Icon(Icons.Rounded.ShortText, contentDescription = null) },
    ) {
      TextField(
        value = nameValue,
        onValueChange = { value ->
          nameValue = value
          state.eventSink(DeckBuilderUiEvent.EditName(value.text))
        },
        placeholder = {
          Text("Name")
        },
        modifier = Modifier
          .fillMaxWidth()
          .focusRequester(focusRequester),
      )
    }

    Spacer(Modifier.height(16.dp))

    var descriptionValue by remember(state.session.deckOrNull() != null) {
      mutableStateOf(TextFieldValue(state.session.deckOrNull()?.description ?: ""))
    }
    DeckTextField(
      icon = { Icon(Icons.Rounded.Subject, contentDescription = null) },
    ) {
      TextField(
        value = descriptionValue,
        onValueChange = { value ->
          descriptionValue = value
          state.eventSink(DeckBuilderUiEvent.EditDescription(value.text))
        },
        placeholder = {
          Text("Description")
        },
        minLines = 3,
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
      prices = state.price,
    )
  }
  Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
}

@Composable
private fun DeckTextField(
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
