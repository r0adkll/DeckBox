package app.deckbox.ui.expansions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DensityLarge
import androidx.compose.material.icons.rounded.DensityMedium
import androidx.compose.material.icons.rounded.DensitySmall
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import app.deckbox.common.compose.widgets.SearchBar
import app.deckbox.common.compose.widgets.SearchBarHeight
import app.deckbox.common.screens.ExpansionsScreen
import app.deckbox.expansions.ui.ExpansionCardStyle
import app.deckbox.ui.expansions.ExpansionsUiEvent.ChangeCardStyle
import app.deckbox.ui.expansions.ExpansionsUiEvent.ExpansionClicked
import app.deckbox.ui.expansions.ExpansionsUiEvent.SearchCleared
import app.deckbox.ui.expansions.ExpansionsUiEvent.SearchUpdated
import app.deckbox.ui.expansions.composables.ExpansionsContent
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.statusBars
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

@Inject
class ExpansionsUiFactory : Ui.Factory {
  override fun create(screen: Screen, context: CircuitContext): Ui<*>? = when (screen) {
    is ExpansionsScreen -> {
      ui<ExpansionsUiState> { state, modifier ->
        Expansions(state, modifier)
      }
    }

    else -> null
  }
}

@Composable
internal fun Expansions(
  state: ExpansionsUiState,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier,
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize(),
      contentAlignment = Alignment.TopStart,
    ) {
      SearchBar(
        initialValue = state.query,
        onQueryUpdated = { query ->
          state.eventSink(SearchUpdated(query))
        },
        onQueryCleared = {
          state.eventSink(SearchCleared)
        },
        leading = {
          Box(modifier = Modifier.padding(start = 16.dp)) {
            Icon(
              Icons.Rounded.Search,
              contentDescription = null,
            )
          }
        },
        placeholder = { Text(LocalStrings.current.expansionSearchHint) },
        trailing = {
          Box {
            IconButton(
              onClick = {
                val nextCardStyleOrdinal = (state.expansionCardStyle.ordinal + 1) % ExpansionCardStyle.values().size
                state.eventSink(ChangeCardStyle(ExpansionCardStyle.values()[nextCardStyleOrdinal]))
              },
            ) {
              val icon = when (state.expansionCardStyle) {
                ExpansionCardStyle.Large -> Icons.Rounded.DensityLarge
                ExpansionCardStyle.Small -> Icons.Rounded.DensityMedium
                ExpansionCardStyle.Compact -> Icons.Rounded.DensitySmall
              }
              Icon(icon, contentDescription = "Change expansion card style")
            }
          }
        },
        modifier = Modifier
          .windowInsetsPadding(WindowInsets.statusBars)
          .zIndex(1f),
      )

      ExpansionsContent(
        loadState = state.loadState,
        style = state.expansionCardStyle,
        onClick = { state.eventSink(ExpansionClicked(it)) },
        contentPadding = PaddingValues(
          start = 16.dp,
          end = 16.dp,
          top = 16.dp + SearchBarHeight / 2,
        ),
        modifier = Modifier
          .windowInsetsPadding(WindowInsets.statusBars)
          .padding(top = 8.dp + SearchBarHeight / 2),
      )
    }
  }
}
