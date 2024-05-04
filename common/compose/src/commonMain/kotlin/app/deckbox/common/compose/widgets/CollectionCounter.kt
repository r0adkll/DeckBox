package app.deckbox.common.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CollectionCounterListItem(
  title: String,
  count: Int,
  onIncrementClick: () -> Unit,
  onDecrementClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  ListItem(
    modifier = modifier,
    headlineContent = {
      Text(
        text = title,
        fontWeight = FontWeight.Medium,
      )
    },
    trailingContent = {
      CollectionCounterChip(
        value = count,
        onIncrementClick = onIncrementClick,
        onDecrementClick = onDecrementClick,
      )
    },
  )
}

@Composable
fun CollectionCounterChip(
  value: Int,
  onIncrementClick: () -> Unit,
  onDecrementClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val shape = RoundedCornerShape(50)
  Row(
    modifier = modifier
      .background(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = shape,
      )
      .border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.secondary,
        shape = shape,
      ),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    IconButton(
      onClick = onDecrementClick,
      enabled = value > 0,
    ) {
      Icon(
        Icons.Rounded.ChevronLeft,
        contentDescription = null,
      )
    }

    Text(
      text = value.toString(),
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.onPrimaryContainer,
    )

    IconButton(
      onClick = onIncrementClick,
    ) {
      Icon(
        Icons.Rounded.ChevronRight,
        contentDescription = null,
      )
    }
  }
}
