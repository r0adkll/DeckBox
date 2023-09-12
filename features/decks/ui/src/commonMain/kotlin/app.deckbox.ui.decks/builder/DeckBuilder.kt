package app.deckbox.ui.decks.builder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.LazyGridState
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
import app.deckbox.common.compose.icons.rounded.AddBoosterPack
import app.deckbox.common.compose.icons.rounded.AddCard
import app.deckbox.common.compose.overlays.showBottomSheetScreen
import app.deckbox.common.compose.widgets.ContentLoadingSize
import app.deckbox.common.compose.widgets.DefaultEmptyView
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.common.screens.BoosterPackPickerScreen
import app.deckbox.common.screens.DeckBuilderScreen
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.BoosterPack
import app.deckbox.core.model.SuperType
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.AddBoosterPack
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.AddCards
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.CardClick
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.DecrementCard
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.IncrementCard
import app.deckbox.ui.decks.builder.DeckBuilderUiEvent.NavigateBack
import app.deckbox.ui.decks.builder.composables.DeckBuilderBottomSheet
import app.deckbox.ui.decks.builder.composables.DeckCardList
import app.deckbox.ui.decks.builder.composables.SheetHeaderHeight
import app.deckbox.ui.decks.builder.model.CardUiModel
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.navigationBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(MergeActivityScope::class, DeckBuilderScreen::class)
@Composable
fun DeckBuilder(
  state: DeckBuilderUiState,
  modifier: Modifier = Modifier,
) {
  val coroutineScope = rememberCoroutineScope()
  val overlayHost = LocalOverlayHost.current
  val focusManager = LocalFocusManager.current
  val eventSink = state.eventSink

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
          state.session.deckOrNull()?.let { deck ->
            val name = if (deck.name.isBlank()) {
              AnnotatedString(
                LocalStrings.current.deckTitleNoName,
                SpanStyle(
                  fontStyle = FontStyle.Italic,
                  color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                ),
              )
            } else {
              AnnotatedString(deck.name)
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
            onClick = { eventSink(NavigateBack) },
          ) {
            Icon(Icons.Rounded.ArrowBack, contentDescription = null)
          }
        },
        actions = {
          IconButton(
            onClick = {
              coroutineScope.launch {
                val result = overlayHost.showBottomSheetScreen<BoosterPack>(BoosterPackPickerScreen())
                if (result != null) {
                  eventSink(AddBoosterPack(result))
                }
              }
            },
          ) {
            Icon(
              Icons.Rounded.AddBoosterPack,
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
        scrollBehavior = scrollBehavior,
      )
    },
    sheetContent = {
      DeckBuilderBottomSheet(
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
  ) { paddingValues ->
    Box(
      modifier = Modifier.fillMaxSize(),
    ) {
      val lazyGridState = rememberLazyGridState()

      when (state.cards) {
        is LoadState.Loaded -> LoadedContent(
          models = state.cards.data,
          isEditing = isEditing,
          onEditClick = { isEditing = true },
          eventSink = eventSink,
          contentPadding = paddingValues,
          lazyGridState = lazyGridState,
        )
        LoadState.Error -> ErrorContent(Modifier.padding(paddingValues))
        LoadState.Loading -> LoadingContent(Modifier.padding(paddingValues))
      }

      val isScrolled by remember {
        derivedStateOf {
          lazyGridState.firstVisibleItemIndex > 0
        }
      }

      ExtendedFloatingActionButton(
        expanded = !isScrolled,
        onClick = { eventSink(AddCards()) },
        text = { Text("Add cards") },
        icon = { Icon(Icons.Rounded.AddCard, contentDescription = null) },
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

@Composable
private fun LoadingContent(
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    SpinningPokeballLoadingIndicator(
      size = ContentLoadingSize,
    )
  }
}

@Composable
private fun ErrorContent(
  modifier: Modifier = Modifier,
) {
  DefaultEmptyView(modifier = modifier)
}

@Composable
private fun LoadedContent(
  models: ImmutableList<CardUiModel>,
  isEditing: Boolean,
  onEditClick: () -> Unit,
  eventSink: (DeckBuilderUiEvent) -> Unit,
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues = PaddingValues(),
  lazyGridState: LazyGridState = rememberLazyGridState(),
) {
  DeckCardList(
    isEditing = isEditing,
    models = models,
    onCardClick = {
      eventSink(CardClick(it.card))
    },
    onCardLongClick = { onEditClick() },
    onAddCardClick = {
      eventSink(IncrementCard(it.card.id))
    },
    onRemoveCardClick = {
      eventSink(DecrementCard(it.card.id))
    },
    onTipClick = {
      when (it) {
        CardUiModel.Tip.Pokemon -> eventSink(AddCards(SuperType.POKEMON))
        CardUiModel.Tip.Trainer -> eventSink(AddCards(SuperType.TRAINER))
        CardUiModel.Tip.Energy -> eventSink(AddCards(SuperType.ENERGY))
      }
    },
    lazyGridState = lazyGridState,
    contentPadding = contentPadding + PaddingValues(bottom = 88.dp),
    modifier = modifier,
  )
}
