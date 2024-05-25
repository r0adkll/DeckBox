package app.deckbox.decks.impl.export

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import org.jetbrains.skia.Color
import org.jetbrains.skia.Font
import org.jetbrains.skia.FontStyle
import org.jetbrains.skia.Paint
import org.jetbrains.skia.TextLine
import org.jetbrains.skia.Typeface

actual fun DrawScope.renderCardCount(
  textSize: TextUnit,
  padding: PaddingValues,
  cornerRadius: Dp,
  count: Int,
  size: Size,
  color: androidx.compose.ui.graphics.Color,
) {
  val textLine = TextLine.make(
    text = count.toString(),
    font = Font(
      typeface = Typeface.makeFromName(null, FontStyle.BOLD),
      size = textSize.toPx(),
    ),
  )

  // Compute Text Bounds

  val widthPadding = padding.calculateStartPadding(LayoutDirection.Ltr) +
    padding.calculateEndPadding(LayoutDirection.Ltr)
  val heightPadding = padding.calculateTopPadding() + padding.calculateBottomPadding()
  val boxWidth = textLine.width + widthPadding.toPx()
  val boxHeight = textLine.height + heightPadding.toPx()

  drawRoundRect(
    color = color,
    topLeft = Offset(
      x = 0f,
      y = size.height - boxHeight,
    ),
    size = Size(boxWidth, boxHeight),
    cornerRadius = CornerRadius(cornerRadius.toPx()),
  )

  drawContext.canvas.nativeCanvas.drawTextLine(
    textLine,
    boxWidth / 2f,
    size.height - (textLine.height / 2f) - padding.calculateBottomPadding().toPx() / 2,
    Paint().apply { this.color = Color.WHITE },
  )
}
