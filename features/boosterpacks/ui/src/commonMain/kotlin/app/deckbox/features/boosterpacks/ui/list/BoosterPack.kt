package app.deckbox.features.boosterpacks.ui.list

import DeckBoxRootAppBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.extensions.plus
import app.deckbox.common.compose.icons.rounded.NewBoosterPack
import app.deckbox.common.compose.navigation.DetailNavigation
import app.deckbox.common.compose.navigation.LocalDetailNavigation
import app.deckbox.common.compose.overlays.showBottomSheetScreen
import app.deckbox.common.compose.widgets.ContentLoadingSize
import app.deckbox.common.compose.widgets.DefaultEmptyView
import app.deckbox.common.compose.widgets.SortChip
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.common.screens.BoosterPackScreen
import app.deckbox.common.screens.DeckPickerScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.BoosterPack
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.SortOption
import app.deckbox.features.boosterpacks.ui.list.BoosterPackUiEvent.AddToDeck
import app.deckbox.features.boosterpacks.ui.list.BoosterPackUiEvent.BoosterPackClick
import app.deckbox.features.boosterpacks.ui.list.BoosterPackUiEvent.ChangeSortOption
import app.deckbox.features.boosterpacks.ui.list.BoosterPackUiEvent.CreateNew
import app.deckbox.features.boosterpacks.ui.list.BoosterPackUiEvent.Delete
import app.deckbox.features.boosterpacks.ui.list.BoosterPackUiEvent.Duplicate
import app.deckbox.features.boosterpacks.ui.list.BoosterPackUiEvent.OpenAppSettings
import app.deckbox.features.boosterpacks.ui.list.composables.BoosterPackItem
import app.deckbox.features.boosterpacks.ui.list.composables.WelcomeTip
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.navigationBars
import com.moriatsushi.insetsx.systemBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(MergeActivityScope::class, BoosterPackScreen::class)
@Composable
fun BoosterPack(
  state: BoosterPackUiState,
  modifier: Modifier = Modifier,
) {
  val eventSink = state.eventSink

  val coroutineScope = rememberCoroutineScope()
  val lazyListState = rememberLazyListState()

  val detailNavigationState = LocalDetailNavigation.current
  val overlayHost = LocalOverlayHost.current

  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  Scaffold(
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      if (detailNavigationState is DetailNavigation.None) {
        DeckBoxRootAppBar(
          title = LocalStrings.current.boosterPacksTitleLong,
          actions = {
            IconButton(
              onClick = { eventSink(OpenAppSettings) },
            ) {
              Icon(Icons.Rounded.Settings, contentDescription = null)
            }
          },
          scrollBehavior = scrollBehavior,
        )
      }
    },
    floatingActionButton = {
      if (detailNavigationState is DetailNavigation.None) {
        val isExpanded by remember {
          derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0
          }
        }
        ExtendedFloatingActionButton(
          text = { Text(LocalStrings.current.fabActionNewBoosterPack) },
          icon = { Icon(Icons.Rounded.NewBoosterPack, contentDescription = null) },
          containerColor = MaterialTheme.colorScheme.secondaryContainer,
          expanded = isExpanded,
          onClick = { eventSink(CreateNew) },
        )
      }
    },
    contentWindowInsets = WindowInsets.systemBars.exclude(WindowInsets.navigationBars),
  ) { paddingValues ->
    state.packState.packs?.let { packs ->
      BoosterPackList(
        packs = packs,
        sortOption = state.sortOption,
        onChangeSortOption = { eventSink(ChangeSortOption(it)) },
        onClick = { eventSink(BoosterPackClick(it)) },
        onDelete = { eventSink(Delete(it)) },
        onDuplicate = { eventSink(Duplicate(it)) },
        onAddToDeck = { pack ->
          coroutineScope.launch {
            val result = overlayHost.showBottomSheetScreen<Deck>(DeckPickerScreen())
            if (result != null) {
              eventSink(AddToDeck(result, pack))
            }
          }
        },
        contentPadding = paddingValues + PaddingValues(bottom = 88.dp),
        state = lazyListState,
      )
    }

    if (state.packState is BoosterPackLoadState.Loading) {
      Box(Modifier.fillMaxSize()) {
        SpinningPokeballLoadingIndicator(size = ContentLoadingSize)
      }
    } else if (state.packState is BoosterPackLoadState.Error) {
      DefaultEmptyView()
    } else if (state.packState.packs.isNullOrEmpty()) {
      WelcomeTip(
        onNewClick = { eventSink(CreateNew) },
        modifier = Modifier.padding(paddingValues),
      )
    }
  }
}

@Composable
private fun BoosterPackList(
  packs: List<BoosterPack>,
  sortOption: SortOption,
  onChangeSortOption: (SortOption) -> Unit,
  onClick: (BoosterPack) -> Unit,
  onDelete: (BoosterPack) -> Unit,
  onDuplicate: (BoosterPack) -> Unit,
  onAddToDeck: (BoosterPack) -> Unit,
  contentPadding: PaddingValues,
  modifier: Modifier = Modifier,
  state: LazyListState = rememberLazyListState(),
) {
  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = contentPadding,
    modifier = modifier
      .padding(horizontal = 16.dp),
    state = state,
  ) {
    if (packs.isNotEmpty()) {
      item {
        Box(
          modifier = Modifier.fillParentMaxWidth(),
        ) {
          SortChip(
            value = sortOption,
            sortOptions = SortOption.All,
            onValueChanged = onChangeSortOption,
            modifier = Modifier.align(Alignment.CenterEnd),
          )
        }
      }
    }
    items(
      items = packs,
      key = { it.id },
    ) { pack ->
      BoosterPackItem(
        pack = pack,
        onClick = { onClick(pack) },
        onDelete = { onDelete(pack) },
        onDuplicate = { onDuplicate(pack) },
        onAddToDeck = { onAddToDeck(pack) },
        interactionSource = state.interactionSource,
      )
    }
  }
}
