package app.deckbox.features.cards.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.ceil
import kotlin.math.roundToInt

private const val DefaultChipColumns = 3
private val DefaultHorizontalSpacing = 16.dp
private val DefaultVerticalSpacing = 16.dp

@Composable
internal fun InfoChipGroup(
  modifier: Modifier = Modifier,
  columns: Int = DefaultChipColumns,
  horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(DefaultHorizontalSpacing),
  verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(DefaultVerticalSpacing),
  content: @Composable () -> Unit,
) {
  Layout(
    content = content,
    modifier = modifier,
  ) { measurables, constraints ->
    val rowSpacing = (columns - 1) * horizontalArrangement.spacing.roundToPx()
    val chipWidth = (constraints.maxWidth - rowSpacing) / columns

    val chipConstraints = Constraints(
      minWidth = chipWidth,
      maxWidth = chipWidth,
    )

    val placeables = measurables
      .map { it.measure(chipConstraints) }

    // Compute widths and horizontal spacing
    val horizontalPositions = IntArray(columns)
    with(horizontalArrangement) {
      arrange(
        totalSize = constraints.maxWidth,
        sizes = (0 until columns).map { chipWidth }.toIntArray(),
        layoutDirection = LayoutDirection.Ltr,
        outPositions = horizontalPositions,
      )
    }

    // Compute heights and vertical spacing
    val rows = ceil(measurables.size.toFloat() / columns.toFloat()).roundToInt()
    val columnHeights = (0 until rows).map { row ->
      val from = row * columns
      val to = (from + columns).coerceAtMost(measurables.size)
      placeables.subList(from, to)
        .maxBy { it.height }
        .height
    }.toIntArray()

    val verticalPositions = IntArray(columnHeights.size)
    val totalHeight = columnHeights.sum() +
      rows.minus(1).coerceAtLeast(0).times(verticalArrangement.spacing.roundToPx())

    with(verticalArrangement) {
      arrange(
        totalSize = totalHeight,
        sizes = columnHeights,
        outPositions = verticalPositions,
      )
    }

    layout(constraints.maxWidth, totalHeight) {
      placeables.forEachIndexed { index, placeable ->
        val column = index % columns
        val x = horizontalPositions[column]
        val row = index / columns
        val y = verticalPositions[row]
        placeable.place(x, y)
      }
    }
  }
}
