package app.deckbox.ui.filter.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Chip(
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit,
) {
  val backgroundColor = if (isSelected) {
    MaterialTheme.colorScheme.secondaryContainer
  } else {
    MaterialTheme.colorScheme.surface
  }
  val contentColor = if (isSelected) {
    MaterialTheme.colorScheme.onSecondaryContainer
  } else {
    MaterialTheme.colorScheme.onSurface
  }

  Surface(
    modifier = modifier,
    color = backgroundColor,
    contentColor = contentColor,
    shape = RoundedCornerShape(8.dp),
    border = if (!isSelected) {
      BorderStroke(
        width = 1.dp,
        color = contentColor,
      )
    } else {
      null
    },
    shadowElevation = if (isSelected) 2.dp else 0.dp,
    onClick = onClick,
  ) {
    Box(Modifier.padding(8.dp)) {
      ProvideTextStyle(MaterialTheme.typography.labelLarge) {
        content()
      }
    }
  }
}
