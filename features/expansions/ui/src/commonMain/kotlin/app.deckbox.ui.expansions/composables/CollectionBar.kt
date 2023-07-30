package app.deckbox.ui.expansions.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
internal fun CollectionBar(
  count: Int,
  printedTotal: Int,
  modifier: Modifier = Modifier,
) {
  // Progress Bar
  BoxWithConstraints(
    modifier = modifier
      .padding(top = 8.dp)
      .background(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(50),
      )
      .fillMaxWidth()
      .height(24.dp)
  ) {
    val progress = count.toFloat() / printedTotal.toFloat()
    val progressWidth = maxWidth * progress
    Box(
      modifier = Modifier
        .background(
          color = MaterialTheme.colorScheme.secondaryContainer,
          shape = RoundedCornerShape(50),
        )
        .size(
          width = progressWidth,
          height = maxHeight,
        )
    ) {
      if (progress > 0.10f) {
        Text(
          text = "${progress.times(100).roundToInt()}%",
          style = MaterialTheme.typography.labelSmall,
          modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(end = 8.dp),
        )
      }
    }
  }
}
