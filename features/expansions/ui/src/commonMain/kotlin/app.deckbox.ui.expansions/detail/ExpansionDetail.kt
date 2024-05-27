package app.deckbox.ui.expansions.detail

import DeckBoxAppBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.EditOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.rounded.EditSquare
import app.deckbox.common.compose.overlays.showBottomSheetScreen
import app.deckbox.common.compose.widgets.CardCornerRadius
import app.deckbox.common.compose.widgets.CollectionBar
import app.deckbox.common.compose.widgets.ContentLoadingSize
import app.deckbox.common.compose.widgets.FilterIcon
import app.deckbox.common.compose.widgets.PokeballLoadingIndicator
import app.deckbox.common.compose.widgets.PokemonCard
import app.deckbox.common.compose.widgets.PokemonCardGrid
import app.deckbox.common.screens.CardCollectionEditorScreen
import app.deckbox.common.screens.ExpansionDetailScreen
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Card
import app.deckbox.core.settings.columnsForStyles
import app.deckbox.ui.expansions.detail.ExpansionDetailUiEvent.CardSelected
import app.deckbox.ui.expansions.detail.ExpansionDetailUiEvent.ChangeGridStyle
import app.deckbox.ui.expansions.detail.composables.ExpansionDetailFilter
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.navigationBars
import com.moriatsushi.insetsx.systemBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(MergeActivityScope::class, ExpansionDetailScreen::class)
@Composable
internal fun ExpansionDetail(
  state: ExpansionDetailUiState,
  modifier: Modifier = Modifier,
) {
  val coroutineScope = rememberCoroutineScope()
  val overlayHost = LocalOverlayHost.current

  var isFilterVisible by remember { mutableStateOf(false) }
  var isEditing by remember { mutableStateOf(false) }

  val lazyGridState = rememberLazyGridState()
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
  Scaffold(
    topBar = {
      if (state is ExpansionDetailUiState.Loaded) {
        val title = if (isEditing) {
          LocalStrings.current.collectionEditingTitle
        } else {
          state.expansion.name
        }
        DeckBoxAppBar(
          title = title,
          colors = if (isEditing) {
            TopAppBarDefaults.topAppBarColors(
              containerColor = MaterialTheme.colorScheme.secondaryContainer,
              scrolledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            )
          } else {
            TopAppBarDefaults.topAppBarColors()
          },
          navigationIcon = {
            if (isEditing) {
              IconButton(
                onClick = { isEditing = false },
              ) {
                Icon(
                  Icons.Rounded.Close,
                  contentDescription = null,
                )
              }
            } else {
              IconButton(
                onClick = { state.eventSink(ExpansionDetailUiEvent.NavigateBack) },
              ) {
                Icon(
                  Icons.AutoMirrored.Rounded.ArrowBack,
                  contentDescription = null,
                )
              }
            }
          },
          actions = {
            IconButton(
              onClick = { isEditing = !isEditing },
            ) {
              Icon(
                if (isEditing) Icons.Rounded.EditOff else Icons.Rounded.Edit,
                contentDescription = null,
              )
            }
            IconButton(
              onClick = { isFilterVisible = true },
            ) {
              FilterIcon(
                isEmpty = state.filterState.filter.isEmpty,
              )
            }
          },
          scrollBehavior = scrollBehavior,
        )
      }
    },
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    contentWindowInsets = WindowInsets.systemBars.exclude(WindowInsets.navigationBars),
  ) { paddingValues ->
    when (state) {
      ExpansionDetailUiState.Loading -> Loading(Modifier.padding(paddingValues))
      is ExpansionDetailUiState.Loaded -> PokemonCardGrid(
        state = lazyGridState,
        cards = state.cards,
        columns = state.cardGridStyle.columnsForStyles(),
        onClick = { card ->
          state.eventSink(CardSelected(card))
        },
        contentPadding = paddingValues,
        headerContent = {
          if (state.cards is LoadState.Loaded) {
            item(
              key = "DetailHeader",
              span = { GridItemSpan(maxLineSpan) },
            ) {
              DetailHeader(state)
            }
          }
        },
        itemContent = { card ->
          PokemonCardItem(
            card = card,
            collected = state.collection[card.id],
            isEditing = isEditing,
            onClick = {
              state.eventSink(CardSelected(card))
            },
            onLongClick = {
              coroutineScope.launch {
                overlayHost.showBottomSheetScreen(CardCollectionEditorScreen(card), skipHalfExpanded = true)
              }
            },
          )
        },
        modifier = Modifier.padding(horizontal = 16.dp),
      )
    }
  }

  val filterLazyListState = rememberLazyListState()
  if (state is ExpansionDetailUiState.Loaded) {
    AnimatedVisibility(
      visible = isFilterVisible,
      enter = expandVertically(
        expandFrom = Alignment.Top,
      ) + fadeIn(),
      exit = shrinkVertically(
        shrinkTowards = Alignment.Top,
      ) + fadeOut(),
    ) {
      ExpansionDetailFilter(
        state = state.filterState,
        lazyListState = filterLazyListState,
        cardGridStyle = state.cardGridStyle,
        onClose = { isFilterVisible = false },
        onChangeGridStyle = { state.eventSink(ChangeGridStyle(it)) },
      )
    }
  }
}

@Composable
private fun DetailHeader(
  state: ExpansionDetailUiState.Loaded,
  modifier: Modifier = Modifier,
) {
  Column(modifier) {
    Row(
      modifier = Modifier
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.End,
    ) {
      Text(
        text = LocalStrings.current.collectionCountOfTotal(
          state.collection.total,
          state.expansion.printedTotal,
        ),
        style = MaterialTheme.typography.labelMedium.copy(
          textAlign = TextAlign.Start,
        ),
      )
    }
    CollectionBar(
      count = state.collection.total,
      total = state.expansion.printedTotal,
      backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
    )
    Spacer(Modifier.height(8.dp))
  }
}

@Composable
private fun PokemonCardItem(
  card: Card,
  collected: Int,
  isEditing: Boolean,
  onClick: () -> Unit,
  onLongClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier.clip(RoundedCornerShape(CardCornerRadius)),
  ) {
    val overlayColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = .5f)
    PokemonCard(
      card = card,
      onClick = if (isEditing) onLongClick else onClick,
      onLongClick = if (isEditing) onClick else onLongClick,
      collected = collected.takeIf { it > 0 }, // Null if 0 for best UI
      modifier = Modifier.drawWithContent {
        drawContent()
        if (isEditing) {
          drawRect(overlayColor)
        }
      },
    )

    if (isEditing) {
      Icon(
        Icons.Rounded.EditSquare,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.secondaryContainer),
        modifier = Modifier
          .background(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(
              topEnd = CardCornerRadius,
              bottomStart = CardCornerRadius,
            ),
          )
          .padding(8.dp)
          .size(20.dp)
          .align(Alignment.TopEnd),
      )
    }
  }
}

@Composable
private fun Loading(
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    PokeballLoadingIndicator(
      size = ContentLoadingSize,
    )
  }
}
