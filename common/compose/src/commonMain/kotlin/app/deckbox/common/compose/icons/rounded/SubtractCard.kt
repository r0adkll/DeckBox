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

private var _subtractCard: ImageVector? = null

val Icons.Rounded.SubtractCard: ImageVector
  get() {
    if (_subtractCard != null) {
      return _subtractCard!!
    }
    _subtractCard = ImageVector.Builder(
      name = "Rounded.SubtractCard",
      defaultWidth = 24.dp,
      defaultHeight = 24.dp,
      viewportWidth = 24f,
      viewportHeight = 24f,
    ).apply {
      path(
        fill = SolidColor(Color(0xFF000000)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(15f, 3f)
        curveTo(16.1046f, 3f, 17f, 3.8954f, 17f, 5f)
        verticalLineTo(16f)
        horizontalLineTo(15f)
        verticalLineTo(5f)
        horizontalLineTo(7f)
        verticalLineTo(18f)
        lineTo(10.171f, 17.9991f)
        curveTo(10.0603f, 18.3121f, 10f, 18.649f, 10f, 19f)
        curveTo(10f, 19.351f, 10.0603f, 19.6879f, 10.171f, 20.0009f)
        lineTo(7f, 20f)
        curveTo(5.8954f, 20f, 5f, 19.1046f, 5f, 18f)
        verticalLineTo(5f)
        curveTo(5f, 3.8954f, 5.8954f, 3f, 7f, 3f)
        horizontalLineTo(15f)
        close()
      }
      path(
        fill = null,
        fillAlpha = 1.0f,
        stroke = SolidColor(Color(0xFF000000)),
        strokeAlpha = 1.0f,
        strokeLineWidth = 2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(13f, 19f)
        horizontalLineTo(18.9957f)
      }
    }.build()
    return _subtractCard!!
  }
