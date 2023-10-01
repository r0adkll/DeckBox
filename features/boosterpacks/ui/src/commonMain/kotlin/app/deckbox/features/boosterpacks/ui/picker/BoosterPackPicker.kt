package app.deckbox.features.boosterpacks.ui.picker

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
import app.deckbox.common.compose.icons.Booster
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.icons.rounded.AddBoosterPack
import app.deckbox.common.compose.widgets.ContentLoadingSize
import app.deckbox.common.compose.widgets.DefaultEmptyView
import app.deckbox.common.compose.widgets.EmptyView
import app.deckbox.common.compose.widgets.FilledTonalIconButton
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.common.screens.BoosterPackPickerScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.BoosterPack
import app.deckbox.features.boosterpacks.ui.list.BoosterPackLoadState
import app.deckbox.features.boosterpacks.ui.picker.BoosterPackPickerUiEvent.BoosterPackClick
import app.deckbox.features.boosterpacks.ui.picker.BoosterPackPickerUiEvent.NewPackClick
import app.deckbox.features.boosterpacks.ui.picker.composables.BoosterPackPickerItem
import app.deckbox.features.boosterpacks.ui.picker.composables.NewBoosterPackItem
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.navigationBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject

private val MinSheetHeight = 300.dp
private val EmptySheetHeightMax = 400.dp

@CircuitInject(MergeActivityScope::class, BoosterPackPickerScreen::class)
@Composable
fun BoosterPackPicker(
  state: BoosterPackPickerUiState,
  modifier: Modifier = Modifier,
) {
  val eventSink = state.eventSink

  Column {
    when (val packState = state.packLoadState) {
      BoosterPackLoadState.Loading -> LoadingContent(modifier)
      BoosterPackLoadState.Error -> ErrorContent(modifier)
      is BoosterPackLoadState.Loaded -> if (packState.packs.isEmpty()) {
        EmptyContent(
          modifier = modifier,
          onNewClick = {
            eventSink(NewPackClick)
          },
        )
      } else {
        LoadedContent(
          packs = packState.packs,
          onPackClick = { pack ->
            eventSink(BoosterPackClick(pack))
          },
          onNewClick = {
            eventSink(NewPackClick)
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
        DeckBoxIcons.Logos.Booster,
        contentDescription = null,
        modifier = Modifier.size(80.dp),
      )
    },
    label = { Text(LocalStrings.current.boosterPickerEmptyMessage) },
    action = {
      FilledTonalIconButton(
        onClick = onNewClick,
        icon = {
          Icon(Icons.Rounded.AddBoosterPack, contentDescription = null)
        },
        label = {
          Text(LocalStrings.current.fabActionNewBoosterPack)
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
  packs: List<BoosterPack>,
  onPackClick: (BoosterPack) -> Unit,
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
        text = LocalStrings.current.boosterPacksTitleLong,
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

    item {
      NewBoosterPackItem(
        modifier = Modifier.clickable(onClick = onNewClick),
      )
    }
  }
}
