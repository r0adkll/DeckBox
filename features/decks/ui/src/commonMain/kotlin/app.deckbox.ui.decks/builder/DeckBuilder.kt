package app.deckbox.ui.decks.builder

import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import app.deckbox.common.compose.icons.rounded.AddCard
import app.deckbox.common.compose.overlays.showBottomSheetScreen
import app.deckbox.common.compose.widgets.builder.CardBuilder
import app.deckbox.common.compose.widgets.builder.model.CardUiModel
import app.deckbox.common.screens.BoosterPackPickerScreen
import app.deckbox.common.screens.DeckBuilderScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Legalities
import app.deckbox.core.model.Legality
import app.deckbox.core.model.SuperType
import app.deckbox.features.decks.api.validation.DeckValidation
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.AddBoosterPack
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.AddCards
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.CardClick
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.DecrementCard
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.IncrementCard
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.NavigateBack
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.NewBoosterPack
import app.deckbox.ui.decks.builder.composables.DeckBuilderBottomSheet
import cafe.adriel.lyricist.LocalStrings
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.coroutines.launch

@CircuitInject(MergeActivityScope::class, DeckBuilderScreen::class)
@Composable
fun DeckBuilder(
  state: DeckBuilderUiState,
  modifier: Modifier = Modifier,
) {
  val coroutineScope = rememberCoroutineScope()
  val overlayHost = LocalOverlayHost.current
  val eventSink = state.eventSink
  val deck = state.session.deckOrNull()
  val validation = state.validation.dataOrNull ?: DeckValidation()
  var deckName by remember(state.session.deckOrNull() != null) {
    mutableStateOf(TextFieldValue(state.session.deckOrNull()?.name ?: ""))
  }
  CardBuilder(
    name = deckName,
    onNameChange = { value ->
      deckName = value
      eventSink(DeckBuilderUiEvent.EditName(value.text))
    },
    title = {
      if (deckName.text.isBlank()) {
        AnnotatedString(
          LocalStrings.current.deckTitleNoName,
          SpanStyle(
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
          ),
        )
      } else {
        AnnotatedString(deckName.text)
      }
    },
    floatingActionButton = { isScrolled ->
      ExtendedFloatingActionButton(
        expanded = !isScrolled,
        onClick = { eventSink(AddCards()) },
        text = { Text("Add cards") },
        icon = { Icon(Icons.Rounded.AddCard, contentDescription = null) },
      )
    },
    bottomSheetContent = {
      DeckBuilderBottomSheet(state)
    },
    onNavClick = { eventSink(NavigateBack) },
    onAddClick = {
      coroutineScope.launch {
        when (val result = overlayHost.showBottomSheetScreen(BoosterPackPickerScreen())) {
          BoosterPackPickerScreen.Response.NewPack -> eventSink(NewBoosterPack)
          is BoosterPackPickerScreen.Response.Pack -> eventSink(AddBoosterPack(result.boosterPack))
          null -> Unit // Do nothing
        }
      }
    },
    onCardClick = { card -> eventSink(CardClick(card.card)) },
    onAddCardClick = { card -> eventSink(IncrementCard(card.card.id)) },
    onRemoveCardClick = { card -> eventSink(DecrementCard(card.card.id)) },
    onTipClick = { tip ->
      when (tip) {
        CardUiModel.Tip.Pokemon -> eventSink(AddCards(SuperType.POKEMON))
        CardUiModel.Tip.Trainer -> eventSink(AddCards(SuperType.TRAINER))
        CardUiModel.Tip.Energy -> eventSink(AddCards(SuperType.ENERGY))
      }
    },
    cardsState = state.cards,
    isValid = validation.isValid && !validation.isEmpty,
    legalities = deck?.legalities ?: Legalities(standard = Legality.LEGAL),
    modifier = modifier,
  )
}
