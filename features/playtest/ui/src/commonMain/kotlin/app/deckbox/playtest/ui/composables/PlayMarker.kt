package app.deckbox.playtest.ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.widgets.CardAspectRatio
import app.deckbox.common.compose.widgets.CardCornerRadius

private val PlayMarkerBorderWidth = 4.dp

@Composable
internal fun PlayMarker(
  modifier: Modifier = Modifier,
  borderWidth: Dp = PlayMarkerBorderWidth,
  borderColor: Color = MaterialTheme.colorScheme.surfaceVariant,
  content: @Composable BoxScope.() -> Unit = {},
) {
  val shape = RoundedCornerShape(CardCornerRadius)
  Box(
    modifier = Modifier
      .aspectRatio(CardAspectRatio)
      .border(
        width = borderWidth,
        color = borderColor,
        shape = shape,
      )
      .clip(shape)
      .then(modifier),
    content = content,
  )
}
