package app.deckbox.features.boosterpacks.ui.builder

import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.rounded.AddCard
import app.deckbox.common.compose.icons.rounded.AddDeck
import app.deckbox.common.compose.overlays.showBottomSheetScreen
import app.deckbox.common.compose.widgets.builder.CardBuilder
import app.deckbox.common.compose.widgets.builder.model.CardUiModel
import app.deckbox.common.screens.BoosterPackBuilderScreen
import app.deckbox.common.screens.DeckPickerScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Legalities
import app.deckbox.core.model.Legality
import app.deckbox.core.model.SuperType
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiEvent.AddCards
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiEvent.AddToDeck
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiEvent.CardClick
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiEvent.DecrementCard
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiEvent.IncrementCard
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiEvent.NavigateBack
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiEvent.NewDeck
import app.deckbox.features.boosterpacks.ui.builder.composables.BoosterPackBottomSheet
import cafe.adriel.lyricist.LocalStrings
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.coroutines.launch

@CircuitInject(MergeActivityScope::class, BoosterPackBuilderScreen::class)
@Composable
fun BoosterPackBuilder(
  state: BoosterPackBuilderUiState,
  modifier: Modifier = Modifier,
) {
  val coroutineScope = rememberCoroutineScope()
  val overlayHost = LocalOverlayHost.current
  val eventSink = state.eventSink
  val boosterPack = state.session.boosterPackOrNull()

  var boosterPackName by remember(state.session.boosterPackOrNull() != null) {
    mutableStateOf(TextFieldValue(state.session.boosterPackOrNull()?.name ?: ""))
  }

  CardBuilder(
    name = boosterPackName,
    onNameChange = { value ->
      boosterPackName = value
      eventSink(BoosterPackBuilderUiEvent.EditName(value.text))
    },
    title = {
      if (boosterPackName.text.isBlank()) {
        AnnotatedString(
          LocalStrings.current.boosterPackTitleNoName,
          SpanStyle(
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
          ),
        )
      } else {
        AnnotatedString(boosterPackName.text)
      }
    },
    floatingActionButton = { isScrolled ->
      ExtendedFloatingActionButton(
        expanded = !isScrolled,
        onClick = { state.eventSink(AddCards()) },
        text = { Text("Add cards") },
        icon = { Icon(Icons.Rounded.AddCard, contentDescription = null) },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
      )
    },
    bottomSheetContent = {
      BoosterPackBottomSheet(state)
    },
    topBarActions = {
      IconButton(
        onClick = {
          coroutineScope.launch {
            when (val result = overlayHost.showBottomSheetScreen(DeckPickerScreen())) {
              DeckPickerScreen.Response.NewDeck -> eventSink(NewDeck)
              is DeckPickerScreen.Response.Deck -> eventSink(AddToDeck(result.deck))
              null -> Unit // Do Nothing
            }
          }
        },
      ) {
        Icon(
          Icons.Rounded.AddDeck,
          contentDescription = null,
        )
      }
    },
    onNavClick = { eventSink(NavigateBack) },
    onCardClick = { card -> eventSink(CardClick(card.card)) },
    onAddCardClick = { card -> eventSink(IncrementCard(card.card.id)) },
    onRemoveCardClick = { card -> eventSink(DecrementCard(card.card.id)) },
    onTipClick = { tip ->
      when (tip) {
        CardUiModel.Tip.Pokemon -> eventSink(AddCards(SuperType.POKEMON))
        CardUiModel.Tip.Trainer -> eventSink(AddCards(SuperType.TRAINER))
        is CardUiModel.Tip.Energy -> eventSink(AddCards(SuperType.ENERGY))
      }
    },
    onTipExtraClick = { /*Do Nothing*/ },
    cardsState = state.cards,
    legalities = boosterPack?.legalities ?: Legalities(standard = Legality.LEGAL),
    columns = 4,
    cardSpacing = 8.dp,
    modifier = modifier,
  )
}
