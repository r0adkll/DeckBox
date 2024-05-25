package app.deckbox.decks.impl.export

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
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

private lateinit var _cachedPaint: Paint

actual fun DrawScope.renderCardCount(
  textSize: TextUnit,
  padding: PaddingValues,
  cornerRadius: Dp,
  count: Int,
  size: Size,
  color: androidx.compose.ui.graphics.Color,
) {
  if (!::_cachedPaint.isInitialized) {
    _cachedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
      this.color = Color.WHITE
      this.textSize = textSize.toPx()
      typeface = Typeface.DEFAULT_BOLD
      textAlign = Paint.Align.CENTER
    }
  }

  // Compute Text Bounds
  val bounds = Rect()
  val text = count.toString()
  _cachedPaint.getTextBounds(text, 0, text.length, bounds)

  val widthPadding = padding.calculateStartPadding(LayoutDirection.Ltr) +
    padding.calculateEndPadding(LayoutDirection.Ltr)
  val heightPadding = padding.calculateTopPadding() + padding.calculateBottomPadding()
  val boxWidth = bounds.width() + widthPadding.toPx()
  val boxHeight = bounds.height() + heightPadding.toPx()

  drawRoundRect(
    color = color,
    topLeft = Offset(
      x = 0f,
      y = size.height - boxHeight,
    ),
    size = Size(boxWidth, boxHeight),
    cornerRadius = CornerRadius(cornerRadius.toPx()),
  )

  drawContext.canvas.nativeCanvas.drawText(
    count.toString(),
    boxWidth / 2f,
    size.height - (bounds.height() / 2f) - padding.calculateBottomPadding().toPx() / 2,
    _cachedPaint,
  )
}
