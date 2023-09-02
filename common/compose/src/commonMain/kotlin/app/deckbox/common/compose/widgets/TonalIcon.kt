package app.deckbox.common.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A custom wrapper for [Icon] so that we can add a tonal circle and content coloring to
 */
@Composable
fun TonalIcon(
  icon: ImageVector,
  containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
  contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
  contentDescription: String?,
  modifier: Modifier = Modifier,
) {
  Icon(
    imageVector = icon,
    contentDescription = contentDescription,
    tint = contentColor,
    modifier = modifier
      .background(
        color = containerColor,
        shape = CircleShape,
      )
      .size(40.dp)
      .padding(8.dp),
  )
}
