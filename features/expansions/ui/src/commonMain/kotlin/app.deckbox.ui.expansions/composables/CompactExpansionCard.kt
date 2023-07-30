package app.deckbox.ui.expansions.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import app.deckbox.core.model.Expansion
import com.seiko.imageloader.rememberImagePainter
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
internal fun CompactExpansionCard(
  expansion: Expansion,
  onClick: () -> Unit,
) {
  val shape = RoundedCornerShape(12.dp)
  Surface(
    modifier = Modifier
      .clip(shape)
      .clickable(onClick = onClick),
    color = MaterialTheme.colorScheme.surfaceVariant,
    shape = shape,
  ) {
    // TODO: Collection implementation
    val count = Random.nextInt(expansion.printedTotal)
    val progress = count.toFloat() / expansion.printedTotal.toFloat()

    Layout(
      content = {
        Box(
          Modifier
            .fillMaxSize()
            .background(
              color = MaterialTheme.colorScheme.secondaryContainer,
              shape = shape,
            )
        )
        CardHeader(
          title = { Text(expansion.name) },
          subtitle = { Text(expansion.series) },
          leading = {
            val logoPainter = key(expansion.images.logo) { rememberImagePainter(expansion.images.logo) }
            Image(
              painter = logoPainter,
              contentDescription = "${expansion.name} Logo",
              modifier = Modifier
                .size(width = 100.dp, height = 48.dp)
            )
          },
        )
      }
    ) { measurables, constraints ->
      val progressWidth = constraints.maxWidth * progress
      val headerPlaceable = measurables[1].measure(constraints)
      val progressPlaceable = measurables[0].measure(constraints.copy(
        maxWidth = progressWidth.roundToInt(),
        maxHeight = headerPlaceable.height,
      ))

      layout(constraints.maxWidth, headerPlaceable.height) {
        progressPlaceable.place(0, 0)
        headerPlaceable.place(0, 0)
      }
    }
  }
}
