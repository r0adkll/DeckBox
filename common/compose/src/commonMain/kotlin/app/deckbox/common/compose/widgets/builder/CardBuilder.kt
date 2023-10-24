package app.deckbox.common.compose.widgets.builder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.PlatformBackHandler
import app.deckbox.common.compose.extensions.plus
import app.deckbox.common.compose.icons.rounded.AddBoosterPack
import app.deckbox.common.compose.widgets.ContentLoadingSize
import app.deckbox.common.compose.widgets.DefaultEmptyView
import app.deckbox.common.compose.widgets.EditingAppBar
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.common.compose.widgets.builder.composables.*
import app.deckbox.common.compose.widgets.builder.model.CardUiModel
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.model.Card
import app.deckbox.core.model.Legalities
import app.deckbox.core.model.Stacked
import com.moriatsushi.insetsx.navigationBars
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CardBuilder(
  title: @Composable () -> AnnotatedString,
  floatingActionButton: @Composable (isScrolled: Boolean) -> Unit,
  bottomSheetContent: @Composable ColumnScope.(FocusRequester) -> Unit,
  onNavClick: () -> Unit,
  onAddClick: () -> Unit,
  onCardClick: (Stacked<Card>) -> Unit,
  onAddCardClick: (Stacked<Card>) -> Unit,
  onRemoveCardClick: (Stacked<Card>) -> Unit,
  onTipClick: (CardUiModel.Tip) -> Unit,
  cardsState: LoadState<out ImmutableList<CardUiModel>>,
  modifier: Modifier = Modifier,
  isValid: Boolean = true,
  legalities: Legalities = Legalities(),
  columns: Int = DefaultColumns,
  cardSpacing: Dp = DefaultCardSpacing,
) {
  val coroutineScope = rememberCoroutineScope()
  val focusManager = LocalFocusManager.current
  val keyBoardController = LocalSoftwareKeyboardController.current

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
      keyBoardController?.hide()
      focusManager.clearFocus()
    }
  }

  var isEditing by remember { mutableStateOf(false) }

  /*
   * Intercept the platform back action and opt to cancel editing mode before
   * popping the screen
   */
  PlatformBackHandler(
    enabled = isEditing,
    onBack = { isEditing = false },
  )

  BottomSheetScaffold(
    modifier = modifier
      .nestedScroll(scrollBehavior.nestedScrollConnection)
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = {
          isEditing = false
          focusManager.clearFocus()
          keyBoardController?.hide()
        },
      ),
    scaffoldState = scaffoldState,
    topBar = {
      Crossfade(
        targetState = isEditing,
      ) { isEditingMode ->
        if (isEditingMode) {
          EditingAppBar(
            onExitClick = { isEditing = false },
            scrollBehavior = scrollBehavior,
          )
        } else {
          BuilderAppBar(
            onNavClick = onNavClick,
            onAddClick = onAddClick,
            onEditClick = { isEditing = !isEditing },
            scaffoldState = scaffoldState,
            nameFocusRequester = nameFocusRequester,
            scrollBehavior = scrollBehavior,
            title = title,
          )
        }
      }
    },
    sheetContent = {
      BuilderBottomSheet(
        isValid = isValid,
        legalities = legalities,
        cardsState = cardsState,
        focusRequester = nameFocusRequester,
        onHeaderClick = {
          coroutineScope.launch {
            scaffoldState.bottomSheetState.expand()
          }
        },
        content = { focusRequester ->
          bottomSheetContent(focusRequester)
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

      when (cardsState) {
        is LoadState.Loaded -> LoadedContent(
          models = cardsState.data,
          isEditing = isEditing,
          onEditClick = { isEditing = true },
          onCardClick = onCardClick,
          onAddCardClick = onAddCardClick,
          onRemoveCardClick = onRemoveCardClick,
          onTipClick = onTipClick,
          contentPadding = paddingValues,
          lazyGridState = lazyGridState,
          columns = columns,
          cardSpacing = cardSpacing,
        )

        LoadState.Error -> ErrorContent(Modifier.padding(paddingValues))
        LoadState.Loading -> LoadingContent(Modifier.padding(paddingValues))
      }

      val isScrolled by remember {
        derivedStateOf {
          lazyGridState.firstVisibleItemIndex > 0
        }
      }

      AnimatedVisibility(
        visible = !isEditing,
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(
            end = 16.dp,
            bottom = SheetHeaderHeight + bottomPadding,
          )
          .windowInsetsPadding(
            WindowInsets.navigationBars,
          ),
      ) {
        floatingActionButton(isScrolled)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BuilderAppBar(
  onNavClick: () -> Unit,
  onEditClick: () -> Unit,
  onAddClick: () -> Unit,
  scaffoldState: BottomSheetScaffoldState,
  nameFocusRequester: FocusRequester,
  scrollBehavior: TopAppBarScrollBehavior,
  modifier: Modifier = Modifier,
  title: @Composable () -> AnnotatedString,
) {
  val coroutineScope = rememberCoroutineScope()

  LargeTopAppBar(
    title = {
      Text(
        text = title(),
        modifier = Modifier.clickable {
          coroutineScope.launch {
            scaffoldState.bottomSheetState.expand()
            nameFocusRequester.requestFocus()
          }
        },
      )
    },
    navigationIcon = {
      IconButton(
        onClick = onNavClick,
      ) {
        Icon(Icons.Rounded.ArrowBack, contentDescription = null)
      }
    },
    actions = {
      IconButton(
        onClick = onAddClick,
      ) {
        Icon(
          Icons.Rounded.AddBoosterPack,
          contentDescription = null,
        )
      }
      IconButton(
        onClick = onEditClick,
      ) {
        Icon(
          Icons.Rounded.Edit,
          contentDescription = null,
        )
      }
    },
    scrollBehavior = scrollBehavior,
    modifier = modifier,
  )
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
  onCardClick: (Stacked<Card>) -> Unit,
  onAddCardClick: (Stacked<Card>) -> Unit,
  onRemoveCardClick: (Stacked<Card>) -> Unit,
  onTipClick: (CardUiModel.Tip) -> Unit,
  columns: Int,
  cardSpacing: Dp,
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues = PaddingValues(),
  lazyGridState: LazyGridState = rememberLazyGridState(),
) {
  CardList(
    isEditing = isEditing,
    models = models,
    onCardClick = onCardClick,
    onCardLongClick = { onEditClick() },
    onAddCardClick = onAddCardClick,
    onRemoveCardClick = onRemoveCardClick,
    onTipClick = onTipClick,
    lazyGridState = lazyGridState,
    contentPadding = contentPadding + PaddingValues(bottom = 88.dp),
    modifier = modifier,
    columns = columns,
    cardSpacing = cardSpacing,
  )
}
