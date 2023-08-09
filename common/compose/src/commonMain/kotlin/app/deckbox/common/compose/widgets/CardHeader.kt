package app.deckbox.common.compose.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A reusable header element for use in Card views
 */
@Composable
fun CardHeader(
  title: @Composable () -> Unit,
  subtitle: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  leading: (@Composable () -> Unit)? = null,
  trailing: (@Composable () -> Unit)? = null,
) {
  Row(
    modifier = modifier
      .padding(
        horizontal = 16.dp,
        vertical = 14.dp,
      ),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    if (leading != null) {
      leading()
      Spacer(Modifier.width(16.dp))
    }
    Column(
      Modifier.weight(1f),
    ) {
      CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.titleMedium,
      ) {
        title()
      }

      CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.bodyMedium,
      ) {
        subtitle()
      }
    }

    trailing?.invoke()
  }
}
