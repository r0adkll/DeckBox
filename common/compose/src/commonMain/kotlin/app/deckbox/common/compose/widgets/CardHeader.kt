package app.deckbox.common.compose.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
        start = 16.dp,
        end = if (trailing == null) 16.dp else 0.dp,
        top = 16.dp,
        bottom = 16.dp,
      ),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    if (leading != null) {
      leading()
      Spacer(Modifier.width(16.dp))
    }
    Column(
      modifier = Modifier.weight(1f),
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

    if (trailing != null) {
      Box(
        modifier = Modifier
          .size(
            width = 56.dp,
            height = 40.dp,
          ),
        contentAlignment = Alignment.Center,
      ) {
        trailing()
      }
    }
  }
}
