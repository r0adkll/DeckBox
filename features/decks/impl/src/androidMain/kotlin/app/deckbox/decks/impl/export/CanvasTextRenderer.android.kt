package app.deckbox.decks.impl.export

import android.graphics.Paint
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas

actual fun DrawScope.renderCardCount(
  count: Int,
  size: Size
) {
  drawContext.canvas.nativeCanvas.drawText(
    count.toString(),
    0f, 0f,
    Paint()
  )
}
