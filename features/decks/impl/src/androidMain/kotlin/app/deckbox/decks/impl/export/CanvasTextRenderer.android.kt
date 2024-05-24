package app.deckbox.decks.impl.export

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

const val COUNT_RADIUS = 200f

private lateinit var _cachedPaint: Paint

actual fun DrawScope.renderCardCount(
  textSize: TextUnit,
  padding: Dp,
  count: Int,
  size: Size,
  color: androidx.compose.ui.graphics.Color,
) {
  if (!::_cachedPaint.isInitialized) {
    _cachedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
      color = Color.WHITE
      this.textSize = textSize.toPx()
      typeface = Typeface.DEFAULT_BOLD
      textAlign = Paint.Align.CENTER
    }
  }

  // Compute Text Bounds
  val bounds = Rect()
  val text = count.toString()
  _cachedPaint.getTextBounds(text, 0, text.length, bounds)

  val boxWidth = bounds.width() + padding.toPx() * 2f
  val boxHeight = bounds.height() + padding.toPx() * 2f

  drawRoundRect(
    color = color,
    topLeft = Offset(
      x = 0f,
      y = size.height - boxHeight
    )
  )

  drawContext.canvas.nativeCanvas.drawText(
    count.toString(),
    size.width / 2f,
    size.height - COUNT_RADIUS,
    ,
  )
}
