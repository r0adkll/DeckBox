package app.deckbox.ui.decks.list

import DeckBoxRootAppBar
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.rounded.Lan
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.extensions.plus
import app.deckbox.common.compose.icons.rounded.Import
import app.deckbox.common.compose.icons.rounded.NewDeck
import app.deckbox.common.compose.navigation.DetailNavigation
import app.deckbox.common.compose.navigation.LocalDetailNavigation
import app.deckbox.common.compose.widgets.ContentLoadingSize
import app.deckbox.common.compose.widgets.DropdownHeaderText
import app.deckbox.common.compose.widgets.DropdownIconButton
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.common.screens.DecksScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.DeckCardConfig
import app.deckbox.core.settings.SortOption
import app.deckbox.features.decks.public.ui.DeckCard
import app.deckbox.features.decks.public.ui.events.DeckCardEvent
import app.deckbox.ui.decks.list.composables.FilterBar
import app.deckbox.ui.decks.list.composables.WelcomeTips
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.navigationBars
import com.moriatsushi.insetsx.systemBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import deckbox.features.decks.ui.generated.resources.Res
import deckbox.features.decks.ui.generated.resources.import_deck_menu_header
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(MergeActivityScope::class, DecksScreen::class)
@Composable
internal fun Decks(
  state: DecksUiState,
  modifier: Modifier = Modifier,
) {
  val eventSink = state.eventSink
  val lazyListState = rememberLazyListState()

  val detailNavigationState = LocalDetailNavigation.current
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  Scaffold(
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      if (detailNavigationState is DetailNavigation.None) {
        DeckBoxRootAppBar(
          title = LocalStrings.current.decks,
          actions = {
            DropdownIconButton(
              options = DeckImportOptions.entries,
              offset = DpOffset(0.dp, (-56).dp),
              onOptionClick = { option ->
                when (option) {
                  DeckImportOptions.Tournaments -> eventSink(DecksUiEvent.ImportTournamentDeck)
                  DeckImportOptions.Text -> eventSink(DecksUiEvent.ImportDeck)
                }
              },
              optionIcon = { option ->
                Icon(
                  when (option) {
                    DeckImportOptions.Tournaments -> Icons.Rounded.Lan
                    DeckImportOptions.Text -> Icons.Rounded.TextFields
                  },
                  contentDescription = option.text(),
                )
              },
              optionText = { Text(it.text()) },
              title = {
                DropdownHeaderText(
                  text = stringResource(Res.string.import_deck_menu_header),
                )
              },
              icon = {
                Icon(Icons.Rounded.Import, contentDescription = null)
              },
            )

            IconButton(
              onClick = { eventSink(DecksUiEvent.OpenAppSettings) },
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
          text = { Text(LocalStrings.current.fabActionNewDeckButton) },
          icon = { Icon(Icons.Rounded.NewDeck, contentDescription = null) },
          expanded = isExpanded,
          onClick = { eventSink(DecksUiEvent.CreateNewDeck) },
        )
      }
    },
    contentWindowInsets = WindowInsets.systemBars.exclude(WindowInsets.navigationBars),
  ) { paddingValues ->
    DeckList(
      decks = state.decks,
      deckCardConfig = state.deckCardConfig,
      sortOption = state.deckSortOrder,
      onChangeSortOption = {
        eventSink(DecksUiEvent.ChangeSortOrder(it))
      },
      onDeckEvent = { deck, event ->
        eventSink(DecksUiEvent.CardEvent(deck, event))
      },
      contentPadding = paddingValues + PaddingValues(bottom = 88.dp),
      state = lazyListState,
    )

    if (state.isLoading) {
      Box(Modifier.fillMaxSize()) {
        SpinningPokeballLoadingIndicator(size = ContentLoadingSize)
      }
    } else if (state.decks.isEmpty()) {
      WelcomeTips(
        onNewDeckClick = { eventSink(DecksUiEvent.CreateNewDeck) },
        onImportDeckClick = { eventSink(DecksUiEvent.ImportTournamentDeck) },
        modifier = Modifier.padding(paddingValues),
      )
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DeckList(
  decks: ImmutableList<Deck>,
  deckCardConfig: DeckCardConfig,
  sortOption: SortOption,
  onChangeSortOption: (SortOption) -> Unit,
  onDeckEvent: (Deck, DeckCardEvent) -> Unit,
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
    if (decks.isNotEmpty()) {
      item {
        FilterBar(
          option = sortOption,
          onOptionClick = onChangeSortOption,
        )
      }
    }

    items(
      items = decks,
      key = { it.id },
    ) { deck ->
      DeckCard(
        deck = deck,
        config = deckCardConfig,
        onEvent = { event -> onDeckEvent(deck, event) },
        interactionSource = state.interactionSource,
        modifier = Modifier.animateItemPlacement(),
      )
    }
  }
}
