package app.deckbox.ui.decks.picker

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.icons.Pokedex
import app.deckbox.common.compose.icons.rounded.AddDeck
import app.deckbox.common.compose.widgets.ContentLoadingSize
import app.deckbox.common.compose.widgets.DefaultEmptyView
import app.deckbox.common.compose.widgets.EmptyView
import app.deckbox.common.compose.widgets.FilledTonalIconButton
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.common.screens.DeckPickerScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Deck
import app.deckbox.ui.decks.picker.composables.DeckPickerItem
import app.deckbox.ui.decks.picker.composables.NewDeckPackItem
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.navigationBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import kotlinx.collections.immutable.ImmutableList

private val MinSheetHeight = 300.dp

@CircuitInject(MergeActivityScope::class, DeckPickerScreen::class)
@Composable
fun DeckPicker(
  state: DeckPickerUiState,
  modifier: Modifier = Modifier,
) {
  val eventSink = state.eventSink
  Column(modifier) {
    if (state.isLoading) {
      LoadingContent()
    } else if (state.decks.isEmpty()) {
      EmptyContent(
        onNewClick = { eventSink(DeckPickerUiEvent.NewDeck) },
      )
    } else {
      LoadedContent(
        decks = state.decks,
        onDeckClick = { deck -> eventSink(DeckPickerUiEvent.DeckPicked(deck)) },
        onNewClick = { eventSink(DeckPickerUiEvent.NewDeck) },
      )
    }
  }
}

@Composable
private fun LoadingContent(
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(MinSheetHeight),
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
  DefaultEmptyView(
    modifier = modifier
      .fillMaxWidth()
      .height(MinSheetHeight),
  )
}

@Composable
private fun EmptyContent(
  onNewClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  EmptyView(
    image = {
      Image(
        DeckBoxIcons.Logos.Pokedex,
        contentDescription = null,
        modifier = Modifier.size(80.dp),
      )
    },
    label = { Text(LocalStrings.current.deckPickerEmptyMessage) },
    action = {
      FilledTonalIconButton(
        onClick = onNewClick,
        icon = {
          Icon(Icons.Rounded.AddDeck, contentDescription = null)
        },
        label = {
          Text(LocalStrings.current.fabActionNewDeckButton)
        },
      )
    },
    modifier = modifier
      .fillMaxWidth()
      .padding(
        top = 8.dp,
        bottom = 16.dp,
      ),
  )
}

@Composable
private fun LoadedContent(
  decks: ImmutableList<Deck>,
  onDeckClick: (Deck) -> Unit,
  onNewClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier
      .fillMaxWidth()
      .defaultMinSize(minHeight = MinSheetHeight),
    contentPadding = PaddingValues(
      bottom = with(LocalDensity.current) {
        WindowInsets.navigationBars.getBottom(this).toDp()
      },
    ),
  ) {
    item {
      Text(
        text = LocalStrings.current.decks,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(
            start = 16.dp,
            end = 16.dp,
            bottom = 4.dp,
          ),
      )
    }

    items(
      items = decks,
      key = { it.id },
    ) { deck ->
      DeckPickerItem(
        deck = deck,
        modifier = Modifier
          .clickable {
            onDeckClick(deck)
          },
      )
    }

    item {
      NewDeckPackItem(
        modifier = Modifier.clickable(onClick = onNewClick),
      )
    }
  }
}
