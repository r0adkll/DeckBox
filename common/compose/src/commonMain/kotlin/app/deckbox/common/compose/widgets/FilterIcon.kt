package app.deckbox.common.compose.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FilterIcon(
  isEmpty: Boolean,
  modifier: Modifier = Modifier,
  contentDescription: String? = null,
) {
  Icon(
    if (isEmpty) Icons.Outlined.FilterAlt else Icons.Rounded.FilterAlt,
    tint = if (isEmpty) LocalContentColor.current else MaterialTheme.colorScheme.secondary,
    contentDescription = contentDescription ?: "Filter the list of cards",
    modifier = modifier,
  )
}
