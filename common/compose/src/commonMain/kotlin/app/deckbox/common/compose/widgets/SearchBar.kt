package app.deckbox.common.compose.widgets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

val SearchBarHeight = 48.dp

enum class SearchBarState {
  Normal,
  Filter,
}

@Composable
fun SearchBar(
  modifier: Modifier = Modifier,
  onQueryUpdated: (String) -> Unit = { },
  onQueryCleared: () -> Unit = { },
  state: SearchBarState = SearchBarState.Normal,
  initialValue: String? = null,
  filter: @Composable () -> Unit = {},
  trailing: @Composable RowScope.() -> Unit = {},
  leading: @Composable RowScope.() -> Unit,
  placeholder: @Composable BoxScope.() -> Unit,
) {
  val elevation = if (state == SearchBarState.Filter) 4.dp else 0.dp
  SearchBarWithFilter(
    modifier = modifier
      .padding(horizontal = 16.dp, vertical = 8.dp)
      .graphicsLayer(
        shadowElevation = with(LocalDensity.current) { elevation.toPx() },
        shape = RoundedCornerShape(24.dp),
      )
      .wrapContentHeight()
      .animateContentSize(),
    onQueryUpdated = onQueryUpdated,
    onQueryCleared = onQueryCleared,
    initialValue = initialValue,
    leading = leading,
    placeholder = placeholder,
    trailing = trailing,
    filter = {
      if (state == SearchBarState.Filter) {
        Box(Modifier.weight(1f)) {
          filter()
        }
      }
    }
  )
}

@Composable
private fun SearchBarWithFilter(
  modifier: Modifier = Modifier,
  onQueryUpdated: (String) -> Unit = { },
  onQueryCleared: () -> Unit = { },
  initialValue: String? = null,
  filter: @Composable ColumnScope.() -> Unit = { },
  leading: @Composable RowScope.() -> Unit,
  placeholder: @Composable BoxScope.() -> Unit,
  trailing: @Composable RowScope.() -> Unit,
) {
  Column(
    modifier = modifier
      .background(
        color = MaterialTheme.colorScheme.inverseOnSurface,
        shape = RoundedCornerShape(24.dp),
      ),
  ) {
    Row(
      modifier = Modifier.height(SearchBarHeight),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      leading()
      Box(
        Modifier
          .weight(1f)
          .padding(start = 16.dp)
      ) {
        var query by remember { mutableStateOf(initialValue) }

        Row(
          verticalAlignment = Alignment.CenterVertically,
        ) {
          BasicTextField(
            modifier = Modifier.weight(1f),
            value = query ?: "",
            textStyle = MaterialTheme.typography.bodyLarge,
            onValueChange = { newValue ->
              query = newValue
              onQueryUpdated(newValue)
            },
          )

          if (!query.isNullOrEmpty()) {
            IconButton(onClick = {
              query = null
              onQueryCleared()
            }) {
              Icon(Icons.Rounded.Close, contentDescription = null)
            }
          }
        }

        if (query.isNullOrBlank()) {
          placeholder()
        }
      }
      trailing()
    }

    filter()
  }
}
