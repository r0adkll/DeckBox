package app.deckbox.common.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val SearchBarHeight = 56.dp
private val SearchBarElevation = 6.dp
private val SearchBarPadding = 16.dp

@Composable
fun SearchBar(
  onQueryUpdated: (String) -> Unit,
  onQueryCleared: () -> Unit,
  modifier: Modifier = Modifier,
  initialValue: String? = null,
  trailing: (@Composable () -> Unit)? = null,
  leading: @Composable () -> Unit,
  placeholder: @Composable () -> Unit,
) {
  Column(
    modifier = modifier
      .background(
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(SearchBarElevation),
        shape = RoundedCornerShape(50),
      ),
  ) {
    Row(
      modifier = Modifier.height(SearchBarHeight),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Spacer(Modifier.width(SearchBarPadding))
      CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.onSurface,
      ) {
        leading()
      }
      Spacer(Modifier.width(SearchBarPadding))

      Box(
        Modifier.weight(1f),
      ) {
        var query by remember { mutableStateOf(initialValue) }

        Row(
          verticalAlignment = Alignment.CenterVertically,
        ) {
          BasicTextField(
            modifier = Modifier.weight(1f),
            value = query ?: "",
            textStyle = MaterialTheme.typography.bodyLarge.copy(
              color = MaterialTheme.colorScheme.onSurface,
            ),
            onValueChange = { newValue ->
              query = newValue
              onQueryUpdated(newValue)
            },
          )

          if (!query.isNullOrEmpty()) {
            IconButton(
              onClick = {
                query = null
                onQueryCleared()
              },
            ) {
              Icon(Icons.Rounded.Close, contentDescription = null)
            }
          }
        }

        if (query.isNullOrBlank()) {
          CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.bodyLarge,
            LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
          ) {
            placeholder()
          }
        }
      }

      if (trailing != null) {
        Box(
          modifier = Modifier.size(SearchBarHeight),
          contentAlignment = Alignment.Center,
        ) {
          CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
          ) {
            trailing()
          }
        }
      }
    }
  }
}
