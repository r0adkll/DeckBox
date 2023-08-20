package app.deckbox.common.compose.icons.rounded

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private var _vector: ImageVector? = null

val Icons.Rounded.LessThan: ImageVector
  get() {
    if (_vector != null) {
      return _vector!!
    }
    _vector = ImageVector.Builder(
      name = "LessThan",
      defaultWidth = 24.dp,
      defaultHeight = 24.dp,
      viewportWidth = 24f,
      viewportHeight = 24f,
    ).apply {
      path(
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(6f, 3f)
        curveTo(4.355f, 3f, 3f, 4.355f, 3f, 6f)
        lineTo(3f, 18f)
        curveTo(3f, 19.645f, 4.355f, 21f, 6f, 21f)
        lineTo(18f, 21f)
        curveTo(19.645f, 21f, 21f, 19.645f, 21f, 18f)
        lineTo(21f, 6f)
        curveTo(21f, 4.355f, 19.645f, 3f, 18f, 3f)
        lineTo(6f, 3f)
        close()
        moveTo(6f, 5f)
        lineTo(18f, 5f)
        curveTo(18.565f, 5f, 19f, 5.435f, 19f, 6f)
        lineTo(19f, 18f)
        curveTo(19f, 18.565f, 18.565f, 19f, 18f, 19f)
        lineTo(6f, 19f)
        curveTo(5.435f, 19f, 5f, 18.565f, 5f, 18f)
        lineTo(5f, 6f)
        curveTo(5f, 5.435f, 5.435f, 5f, 6f, 5f)
        close()
        moveTo(15.056641f, 6.9921875f)
        arcTo(1.0001f, 1.0001f, 0f, isMoreThanHalf = false, isPositiveArc = false, 14.949219f, 6.9941406f)
        arcTo(1.0001f, 1.0001f, 0f, isMoreThanHalf = false, isPositiveArc = false, 14.503906f, 7.1308594f)
        lineTo(7.5039062f, 11.130859f)
        arcTo(1.0001f, 1.0001f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7.5039062f, 12.869141f)
        lineTo(14.503906f, 16.869141f)
        arcTo(1.0007574f, 1.0007574f, 0f, isMoreThanHalf = true, isPositiveArc = false, 15.496094f, 15.130859f)
        lineTo(10.017578f, 12f)
        lineTo(15.496094f, 8.8691406f)
        arcTo(1.0001f, 1.0001f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15.056641f, 6.9921875f)
        close()
      }
    }.build()
    return _vector!!
  }
