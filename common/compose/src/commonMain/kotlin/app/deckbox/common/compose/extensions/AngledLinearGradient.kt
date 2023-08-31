package app.deckbox.common.compose.extensions

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

fun Double.toRadians(): Double {
  return (this * PI) / 180.0
}

@Immutable
class AngledLinearGradient(
  private val colorStops: List<Pair<Float, Color>>,
  private val dragOffset: Offset,
  private val tileMode: TileMode = TileMode.Clamp,
  angleInDegrees: Float = 0f,
  useAsCssAngle: Boolean = false,
) : ShaderBrush() {

  // handle edge cases like: -1235, ...
  private val normalizedAngle: Float = if (useAsCssAngle) {
    ((90 - angleInDegrees) % 360 + 360) % 360
  } else {
    (angleInDegrees % 360 + 360) % 360
  }
  private val angleInRadians: Float = normalizedAngle.toDouble().toRadians().toFloat()

  override fun createShader(size: Size): Shader {
    val (from, to) = getGradientCoordinates(size = size)

    return LinearGradientShader(
      colors = List<Color>(colorStops.size) { i -> colorStops[i].second },
      colorStops = List<Float>(colorStops.size) { i -> colorStops[i].first },
      from = from + dragOffset,
      to = to + dragOffset,
      tileMode = tileMode,
    )
  }

  private fun getGradientCoordinates(size: Size): Pair<Offset, Offset> {
    val diagonal = sqrt(size.width.pow(2) + size.height.pow(2))
    val angleBetweenDiagonalAndWidth = acos(size.width / diagonal)
    val angleBetweenDiagonalAndGradientLine =
      if ((normalizedAngle > 90 && normalizedAngle < 180) ||
        (normalizedAngle > 270 && normalizedAngle < 360)
      ) {
        PI.toFloat() - angleInRadians - angleBetweenDiagonalAndWidth
      } else {
        angleInRadians - angleBetweenDiagonalAndWidth
      }
    val halfGradientLine = abs(cos(angleBetweenDiagonalAndGradientLine) * diagonal) / 2

    val horizontalOffset = halfGradientLine * cos(angleInRadians)
    val verticalOffset = halfGradientLine * sin(angleInRadians)

    val start = size.center + Offset(-horizontalOffset, verticalOffset)
    val end = size.center + Offset(horizontalOffset, -verticalOffset)

    return start to end
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is AngledLinearGradient) return false

    if (colorStops != other.colorStops) return false
    if (dragOffset != other.dragOffset) return false
    if (normalizedAngle != other.normalizedAngle) return false
    if (tileMode != other.tileMode) return false

    return true
  }

  override fun hashCode(): Int {
    var result = colorStops.hashCode()
    result = 31 * result + dragOffset.hashCode()
    result = 31 * result + normalizedAngle.hashCode()
    result = 31 * result + tileMode.hashCode()
    return result
  }

  override fun toString(): String {
    return "LinearGradient(colors=$colorStops, " +
      "dragOffset=$dragOffset, " +
      "angle=$normalizedAngle, " +
      "tileMode=$tileMode)"
  }
}
