package app.deckbox.ui.decks.picker

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.Booster
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.widgets.ContentLoadingSize
import app.deckbox.common.compose.widgets.DefaultEmptyView
import app.deckbox.common.compose.widgets.EmptyView
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.common.screens.DeckPickerScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Deck
import app.deckbox.ui.decks.picker.composables.DeckPickerItem
import cafe.adriel.lyricist.LocalStrings
import com.r0adkll.kotlininject.merge.annotations.CircuitInject

private val MinSheetHeight = 300.dp

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(MergeActivityScope::class, DeckPickerScreen::class)
@Composable
fun DeckPicker(
  state: DeckPickerUiState,
  modifier: Modifier = Modifier,
) {
  Column(modifier) {
    Text(
      text = LocalStrings.current.deckPickerTitle,
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.SemiBold,
      modifier = Modifier
        .align(Alignment.Start)
        .padding(
          start = 16.dp,
          end = 16.dp,
          bottom = 16.dp,
        ),
    )

    if (state.isLoading) {
      LoadingContent()
    } else if (state.decks.isEmpty()) {
      EmptyContent()
    } else {
      LoadedContent(
        decks = state.decks,
        onDeckClick = { deck ->
          state.eventSink(DeckPickerUiEvent.DeckPicked(deck))
        },
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
  modifier: Modifier = Modifier,
) {
  EmptyView(
    image = {
      Image(
        DeckBoxIcons.Logos.Booster,
        contentDescription = null,
        modifier = Modifier.size(80.dp),
      )
    },
    label = { Text(LocalStrings.current.boosterPickerEmptyMessage) },
    modifier = modifier
      .fillMaxWidth()
      .height(MinSheetHeight),
  )
}

@Composable
private fun LoadedContent(
  decks: List<Deck>,
  onDeckClick: (Deck) -> Unit,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier
      .fillMaxWidth()
      .defaultMinSize(minHeight = MinSheetHeight),
  ) {
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
  }
}
