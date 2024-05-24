package app.deckbox.decks.impl.export

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import org.jetbrains.skia.Paint
import org.jetbrains.skia.TextLine

actual fun DrawScope.renderCardCount(
  count: Int,
  size: Size
) {
  drawContext.canvas.nativeCanvas.drawTextLine(
    TextLine.Companion.make(count.toString(), null),
    0f, 0f,
    Paint(),
  )
}
