package app.deckbox.ui.expansions.list.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.core.model.Expansion
import app.deckbox.core.settings.ExpansionCardStyle
import app.deckbox.ui.expansions.list.ExpansionState
import app.deckbox.ui.expansions.list.ExpansionsLoadState
import app.deckbox.ui.expansions.list.Series
import cafe.adriel.lyricist.LocalStrings

@Composable
internal fun ExpansionsContent(
  expansionState: ExpansionState,
  style: ExpansionCardStyle,
  onClick: (Expansion) -> Unit,
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues = PaddingValues(),
) {
  when (expansionState) {
    ExpansionState.Loading -> LoadingContent(modifier)
    is ExpansionState.Error -> ErrorContent(expansionState.message, modifier)
    is ExpansionState.Loaded -> if (expansionState.groupedExpansions.isEmpty()) {
      EmptyContent(modifier)
    } else {
      ExpansionsContent(
        expansions = expansionState.groupedExpansions,
        style = style,
        onClick = onClick,
        modifier = modifier,
        contentPadding = contentPadding,
      )
    }
  }
}

@Composable
private fun EmptyContent(
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Icon(
      Icons.Outlined.Place,
      contentDescription = null,
    )
    Spacer(Modifier.height(16.dp))
    Text(LocalStrings.current.expansionsEmptyMessage)
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
    CircularProgressIndicator()
  }
}

@Composable
private fun ErrorContent(
  message: String,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(64.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Icon(
      Icons.Outlined.Error,
      contentDescription = null,
    )
    Spacer(Modifier.height(16.dp))
    Text(message)
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ExpansionsContent(
  expansions: Map<Series, List<Expansion>>,
  style: ExpansionCardStyle,
  onClick: (Expansion) -> Unit,
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues = PaddingValues(),
) {
  LazyColumn(
    modifier = modifier,
    contentPadding = contentPadding,
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    expansions.forEach { (series, expansions) ->
      stickyHeader {
        Text(
          text = series,
          style = MaterialTheme.typography.labelMedium,
          modifier = Modifier
            .padding(
              vertical = 8.dp,
            ),
        )
      }

      items(expansions) { expansion ->
        val clickListener = { onClick(expansion) }
        when (style) {
          ExpansionCardStyle.Large -> {
            LargeExpansionCard(
              expansion = expansion,
              onClick = clickListener,
            )
          }
          ExpansionCardStyle.Small -> {
            SmallExpansionCard(
              expansion = expansion,
              onClick = clickListener,
            )
          }
          ExpansionCardStyle.Compact -> {
            CompactExpansionCard(
              expansion = expansion,
              onClick = clickListener,
            )
          }
        }
      }
    }
  }
}
