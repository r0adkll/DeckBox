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

val Icons.Rounded.LessThanEqual: ImageVector
  get() {
    if (_vector != null) {
      return _vector!!
    }
    _vector = ImageVector.Builder(
      name = "LessThanEqual",
      defaultWidth = 24.dp,
      defaultHeight = 24.dp,
      viewportWidth = 24f,
      viewportHeight = 24f
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
        pathFillType = PathFillType.NonZero
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
        moveTo(15.003906f, 5.9941406f)
        arcTo(1.0001f, 1.0001f, 0f, isMoreThanHalf = false, isPositiveArc = false, 14.552734f, 6.1054688f)
        lineTo(8.5527344f, 9.1054688f)
        arcTo(1.0001f, 1.0001f, 0f, isMoreThanHalf = false, isPositiveArc = false, 8.5527344f, 10.894531f)
        lineTo(14.552734f, 13.894531f)
        arcTo(1.0001163f, 1.0001163f, 0f, isMoreThanHalf = true, isPositiveArc = false, 15.447266f, 12.105469f)
        lineTo(11.236328f, 10f)
        lineTo(15.447266f, 7.8945312f)
        arcTo(1.0001f, 1.0001f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15.003906f, 5.9941406f)
        close()
        moveTo(9f, 15f)
        curveTo(8.448f, 15f, 8f, 15.448f, 8f, 16f)
        curveTo(8f, 16.552f, 8.448f, 17f, 9f, 17f)
        lineTo(15f, 17f)
        curveTo(15.552f, 17f, 16f, 16.552f, 16f, 16f)
        curveTo(16f, 15.448f, 15.552f, 15f, 15f, 15f)
        lineTo(9f, 15f)
        close()
      }
    }.build()
    return _vector!!
  }

