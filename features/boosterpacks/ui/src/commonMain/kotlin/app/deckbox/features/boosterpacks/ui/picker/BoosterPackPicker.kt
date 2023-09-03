package app.deckbox.features.boosterpacks.ui.picker

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
import app.deckbox.common.compose.widgets.DefaultEmptyView
import app.deckbox.common.compose.widgets.EmptyView
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.common.screens.BoosterPackPickerScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.BoosterPack
import app.deckbox.features.boosterpacks.ui.list.BoosterPackLoadState
import app.deckbox.features.boosterpacks.ui.picker.BoosterPackPickerUiEvent.BoosterPackClick
import app.deckbox.features.boosterpacks.ui.picker.composables.BoosterPackPickerItem
import cafe.adriel.lyricist.LocalStrings
import com.r0adkll.kotlininject.merge.annotations.CircuitInject

private val MinSheetHeight = 300.dp

@CircuitInject(MergeActivityScope::class, BoosterPackPickerScreen::class)
@Composable
fun BoosterPackPicker(
  state: BoosterPackPickerUiState,
  modifier: Modifier = Modifier,
) {
  Column {
    Text(
      text = LocalStrings.current.boosterPacksTitleLong,
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

    when (val packState = state.packLoadState) {
      BoosterPackLoadState.Loading -> LoadingContent(modifier)
      BoosterPackLoadState.Error -> ErrorContent(modifier)
      is BoosterPackLoadState.Loaded -> if (packState.packs.isEmpty()) {
        EmptyContent(modifier)
      } else {
        LoadedContent(
          packs = packState.packs,
          onPackClick = { pack ->
            state.eventSink(BoosterPackClick(pack))
          },
          modifier = modifier,
        )
      }
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
      size = 64.dp,
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
  packs: List<BoosterPack>,
  onPackClick: (BoosterPack) -> Unit,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier
      .fillMaxWidth()
      .defaultMinSize(minHeight = MinSheetHeight),
  ) {
    items(
      items = packs,
      key = { it.id },
    ) { pack ->
      BoosterPackPickerItem(
        pack = pack,
        modifier = Modifier
          .clickable {
            onPackClick(pack)
          },
      )
    }
  }
}
