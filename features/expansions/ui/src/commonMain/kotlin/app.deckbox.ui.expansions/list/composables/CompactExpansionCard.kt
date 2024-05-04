package app.deckbox.ui.expansions.list.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.widgets.CardHeader
import app.deckbox.core.extensions.readableFormat
import app.deckbox.core.model.Collected
import app.deckbox.core.model.Expansion
import com.seiko.imageloader.rememberImagePainter
import kotlin.math.roundToInt

@Composable
internal fun CompactExpansionCard(
  collectedExpansion: Collected<Expansion>,
  onClick: () -> Unit,
) {
  val expansion = collectedExpansion.item
  val shape = RoundedCornerShape(12.dp)
  Surface(
    modifier = Modifier
      .clip(shape)
      .clickable(onClick = onClick),
    color = MaterialTheme.colorScheme.surfaceVariant,
    shape = shape,
  ) {
    val count = collectedExpansion.count
    val progress = count.toFloat() / expansion.printedTotal.toFloat()

    Layout(
      content = {
        Box(
          Modifier
            .fillMaxSize()
            .background(
              color = MaterialTheme.colorScheme.secondaryContainer,
              shape = shape,
            ),
        )
        CardHeader(
          title = { Text(expansion.name) },
          subtitle = { Text(expansion.releaseDate.readableFormat) },
          leading = {
            val logoPainter = key(expansion.images.logo) { rememberImagePainter(expansion.images.logo) }
            Image(
              painter = logoPainter,
              contentDescription = "${expansion.name} Logo",
              modifier = Modifier
                .size(width = 100.dp, height = 48.dp),
            )
          },
        )
      },
    ) { measurables, constraints ->
      val progressWidth = constraints.maxWidth * progress
      val headerPlaceable = measurables[1].measure(constraints)
      val progressPlaceable = measurables[0].measure(
        constraints.copy(
          minWidth = 0,
          maxWidth = progressWidth.roundToInt(),
          maxHeight = headerPlaceable.height,
        ),
      )

      layout(constraints.maxWidth, headerPlaceable.height) {
        progressPlaceable.place(0, 0)
        headerPlaceable.place(0, 0)
      }
    }
  }
}
