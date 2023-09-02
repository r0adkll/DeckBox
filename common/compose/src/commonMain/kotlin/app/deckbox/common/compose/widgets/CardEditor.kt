package app.deckbox.common.compose.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.rounded.AddCard
import app.deckbox.common.compose.icons.rounded.RemoveCard
import app.deckbox.common.compose.icons.rounded.SubtractCard

@Composable
fun CardEditor(
  count: Int,
  isEditing: Boolean,
  onAddClick: () -> Unit,
  onRemoveClick: () -> Unit,
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(8.dp),
  content: @Composable () -> Unit,
) {
  Box(
    modifier = modifier
      .wrapContentSize(),
  ) {
    content()
    AnimatedVisibility(
      visible = isEditing,
      enter = fadeIn(),
      exit = fadeOut(),
      modifier = Modifier.matchParentSize(),
    ) {
      Column(
        modifier = Modifier
          .matchParentSize()
          .clip(shape),
      ) {
        Box(
          modifier = Modifier
            .background(
              color = MaterialTheme.colorScheme.primaryContainer.copy(
                alpha = 0.75f,
              ),
            )
            .weight(1f)
            .fillMaxWidth()
            .clickable(onClick = onAddClick),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            Icons.Rounded.AddCard,
            contentDescription = null,
          )
        }
        val removeColor = if (count > 1) {
          MaterialTheme.colorScheme.secondaryContainer
        } else {
          MaterialTheme.colorScheme.tertiaryContainer
        }
        Box(
          modifier = Modifier
            .background(
              color = removeColor.copy(
                alpha = 0.75f,
              ),
            )
            .weight(1f)
            .fillMaxWidth()
            .clickable(onClick = onRemoveClick),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            if (count > 1) Icons.Rounded.SubtractCard else Icons.Rounded.RemoveCard,
            contentDescription = null,
          )
        }
      }
    }
  }
}
