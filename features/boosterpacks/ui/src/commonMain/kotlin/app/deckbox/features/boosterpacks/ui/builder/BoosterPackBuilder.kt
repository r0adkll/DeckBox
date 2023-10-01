package app.deckbox.features.boosterpacks.ui.builder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.EditOff
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.extensions.plus
import app.deckbox.common.compose.icons.rounded.AddCard
import app.deckbox.common.compose.icons.rounded.AddDeck
import app.deckbox.common.compose.overlays.showBottomSheetScreen
import app.deckbox.common.screens.BoosterPackBuilderScreen
import app.deckbox.common.screens.DeckPickerScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Deck
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiEvent.AddCards
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiEvent.AddToDeck
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiEvent.CardClick
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiEvent.DecrementCard
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiEvent.IncrementCard
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiEvent.NavigateBack
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiEvent.NewDeck
import app.deckbox.features.boosterpacks.ui.builder.composables.BoosterPackBottomSheet
import app.deckbox.features.boosterpacks.ui.builder.composables.BoosterPackCardList
import app.deckbox.features.boosterpacks.ui.builder.composables.SheetHeaderHeight
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.navigationBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(MergeActivityScope::class, BoosterPackBuilderScreen::class)
@Composable
fun BoosterPackBuilder(
  state: BoosterPackBuilderUiState,
  modifier: Modifier = Modifier,
) {
  val coroutineScope = rememberCoroutineScope()
  val focusManager = LocalFocusManager.current
  val overlayHost = LocalOverlayHost.current

  val bottomPadding = with(LocalDensity.current) {
    WindowInsets.navigationBars.getBottom(this).toDp()
  }

  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
  val scaffoldState = rememberBottomSheetScaffoldState()
  val nameFocusRequester = remember { FocusRequester() }

  val isBottomSheetCollapsed by remember {
    derivedStateOf {
      scaffoldState.bottomSheetState.currentValue == SheetValue.PartiallyExpanded
    }
  }
  LaunchedEffect(isBottomSheetCollapsed) {
    if (isBottomSheetCollapsed) {
      focusManager.clearFocus()
    }
  }

  var isEditing by remember { mutableStateOf(false) }

  BottomSheetScaffold(
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    scaffoldState = scaffoldState,
    topBar = {
      LargeTopAppBar(
        title = {
          state.session.boosterPackOrNull()?.let { boosterPack ->
            val name = if (boosterPack.name.isNullOrBlank()) {
              AnnotatedString(
                LocalStrings.current.boosterPackPickerTitle,
                SpanStyle(
                  fontStyle = FontStyle.Italic,
                  color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                ),
              )
            } else {
              AnnotatedString(boosterPack.name!!)
            }

            Text(
              text = name,
              modifier = Modifier.clickable {
                coroutineScope.launch {
                  scaffoldState.bottomSheetState.expand()
                  nameFocusRequester.requestFocus()
                }
              },
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = { state.eventSink(NavigateBack) },
          ) {
            Icon(Icons.Rounded.ArrowBack, contentDescription = null)
          }
        },
        actions = {
          IconButton(
            onClick = {
              coroutineScope.launch {
                when (val result = overlayHost.showBottomSheetScreen(DeckPickerScreen())) {
                  DeckPickerScreen.Response.NewDeck -> state.eventSink(NewDeck)
                  is DeckPickerScreen.Response.Deck -> state.eventSink(AddToDeck(result.deck))
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
          IconButton(
            onClick = { isEditing = !isEditing },
          ) {
            Icon(
              if (isEditing) Icons.Rounded.EditOff else Icons.Rounded.Edit,
              contentDescription = null,
            )
          }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
          scrolledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        scrollBehavior = scrollBehavior,
      )
    },
    sheetContent = {
      BoosterPackBottomSheet(
        state = state,
        focusRequester = nameFocusRequester,
        onHeaderClick = {
          coroutineScope.launch {
            scaffoldState.bottomSheetState.expand()
          }
        },
      )
    },
    sheetDragHandle = null,
    sheetPeekHeight = SheetHeaderHeight + bottomPadding,
    sheetTonalElevation = 3.dp,
  ) {
    Box(
      modifier = Modifier.fillMaxSize(),
    ) {
      val lazyGridState = rememberLazyGridState()

      BoosterPackCardList(
        isEditing = isEditing,
        models = state.cards,
        onCardClick = {
          state.eventSink(CardClick(it.card))
        },
        onCardLongClick = { isEditing = true },
        onAddCardClick = {
          state.eventSink(IncrementCard(it.card.id))
        },
        onRemoveCardClick = {
          state.eventSink(DecrementCard(it.card.id))
        },
        lazyGridState = lazyGridState,
        contentPadding = it + PaddingValues(bottom = 88.dp),
      )

      val isScrolled by remember {
        derivedStateOf {
          lazyGridState.firstVisibleItemIndex > 0
        }
      }

      ExtendedFloatingActionButton(
        expanded = !isScrolled,
        onClick = { state.eventSink(AddCards()) },
        text = { Text("Add cards") },
        icon = { Icon(Icons.Rounded.AddCard, contentDescription = null) },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(
            end = 16.dp,
            bottom = SheetHeaderHeight + bottomPadding,
          )
          .windowInsetsPadding(
            WindowInsets.navigationBars,
          ),
      )
    }
  }
}
