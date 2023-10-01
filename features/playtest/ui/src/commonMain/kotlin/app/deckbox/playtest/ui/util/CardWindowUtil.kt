package app.deckbox.playtest.ui.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size

object CardWindowUtil {

  private const val xOffsetPercentage = 0.040871933f
  private const val yOffsetPercentage = 0.13671875f
  private const val widthPercentage = 0.9182561f
  private const val heightPercentage = 0.5097656f

  fun scaledImageWindow(originalSize: Size, scale: Float): Rect {
    val scaledSize = originalSize * scale

    val xOffset = scaledSize.width * xOffsetPercentage
    val yOffset = scaledSize.height * yOffsetPercentage
    val width = scaledSize.width * widthPercentage
    val height = scaledSize.height * heightPercentage

    return Rect(
      offset = Offset(xOffset, yOffset),
      size = Size(width, height)
    )
  }

  fun windowScale(originalHeight: Float, targetWindowHeight: Float): Float {
    val originalWindowHeight = originalHeight * heightPercentage
    return targetWindowHeight / originalWindowHeight
  }
}
