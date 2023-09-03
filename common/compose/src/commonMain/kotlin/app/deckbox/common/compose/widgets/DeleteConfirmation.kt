package app.deckbox.common.compose.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings

@Composable
fun DeleteConfirmation(
  visible: Boolean,
  onDelete: () -> Unit,
  onCancel: () -> Unit,
  modifier: Modifier = Modifier,
) {
  AnimatedVisibility(
    visible = visible,
    enter = slideInVertically { it } + fadeIn(initialAlpha = 0.5f),
    exit = slideOutVertically { it } + fadeOut(targetAlpha = 0.5f),
    modifier = modifier,
  ) {
    Row(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.error, RoundedCornerShape(16.dp))
        .fillMaxWidth()
        .padding(
          horizontal = 16.dp,
          vertical = 16.dp,
        ),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.onError,
      ) {
        Text(
          text = LocalStrings.current.actionDeleteAreYouSure,
          style = MaterialTheme.typography.titleMedium,
        )

        Spacer(Modifier.weight(1f))

        TextButton(
          onClick = onCancel,
          colors = ButtonDefaults.textButtonColors(contentColor = LocalContentColor.current),
        ) {
          Text(LocalStrings.current.actionCancel)
        }

        FilledTonalButton(
          onClick = onDelete,
          contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        ) {
          Icon(
            Icons.Rounded.DeleteOutline,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize),
          )
          Spacer(Modifier.size(ButtonDefaults.IconSpacing))
          Text(LocalStrings.current.actionDelete)
        }
      }
    }
  }
}
