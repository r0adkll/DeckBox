package app.deckbox.common.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

private const val BorderOpacity = 0.38f

@Composable
fun CollectionBar(
  count: Int,
  total: Int,
  modifier: Modifier = Modifier,
  backgroundColor: Color = MaterialTheme.colorScheme.surface,
  borderColor: Color = MaterialTheme.colorScheme.contentColorFor(backgroundColor)
    .copy(alpha = BorderOpacity),
) {
  val shape = RoundedCornerShape(50)
  // Progress Bar
  BoxWithConstraints(
    modifier = modifier
      .padding(top = 8.dp)
      .background(
        color = backgroundColor,
        shape = shape,
      )
      .border(
        width = 1.dp,
        color = borderColor,
        shape = shape,
      )
      .fillMaxWidth()
      .height(24.dp)
      .clip(shape),
  ) {
    val progress = count.toFloat() / total.toFloat()
    val progressWidth = maxWidth * progress
    Box(
      modifier = Modifier
        .background(
          color = MaterialTheme.colorScheme.secondaryContainer,
          shape = shape,
        )
        .border(
          width = 1.dp,
          color = MaterialTheme.colorScheme.secondary,
          shape = shape,
        )
        .size(
          width = progressWidth,
          height = maxHeight,
        ),
    ) {
      if (progress > 0.10f) {
        Text(
          text = "${progress.times(100).roundToInt()}%",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSecondaryContainer,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(end = 8.dp),
        )
      }
    }
  }
}
