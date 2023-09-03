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

val Icons.Rounded.AddDeck: ImageVector
  get() {
    if (_vector != null) {
      return _vector!!
    }
    _vector = ImageVector.Builder(
      name = "Rounded.AddDeck",
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
        pathFillType = PathFillType.EvenOdd,
      ) {
        moveTo(20f, 4f)
        verticalLineTo(12f)
        curveTo(20f, 13.1046f, 19.1046f, 14f, 18f, 14f)
        horizontalLineTo(6f)
        curveTo(4.8954f, 14f, 4f, 13.1046f, 4f, 12f)
        verticalLineTo(4f)
        curveTo(4f, 2.8954f, 4.8954f, 2f, 6f, 2f)
        horizontalLineTo(18f)
        curveTo(19.1046f, 2f, 20f, 2.8954f, 20f, 4f)
        close()
        moveTo(17f, 12f)
        horizontalLineTo(7f)
        curveTo(6.4477f, 12f, 6f, 11.5523f, 6f, 11f)
        verticalLineTo(5f)
        curveTo(6f, 4.4477f, 6.4477f, 4f, 7f, 4f)
        horizontalLineTo(17f)
        curveTo(17.5523f, 4f, 18f, 4.4477f, 18f, 5f)
        verticalLineTo(11f)
        curveTo(18f, 11.5523f, 17.5523f, 12f, 17f, 12f)
        close()
      }
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
        moveTo(13f, 18f)
        horizontalLineTo(5f)
        curveTo(4.4477f, 18f, 4f, 17.5523f, 4f, 17f)
        curveTo(4f, 16.4477f, 4.4477f, 16f, 5f, 16f)
        horizontalLineTo(13f)
        curveTo(13.5523f, 16f, 14f, 16.4477f, 14f, 17f)
        curveTo(14f, 17.5523f, 13.5523f, 18f, 13f, 18f)
        close()
      }
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
        moveTo(13f, 22f)
        horizontalLineTo(5f)
        curveTo(4.4477f, 22f, 4f, 21.5523f, 4f, 21f)
        curveTo(4f, 20.4477f, 4.4477f, 20f, 5f, 20f)
        horizontalLineTo(13f)
        curveTo(13.5523f, 20f, 14f, 20.4477f, 14f, 21f)
        curveTo(14f, 21.5523f, 13.5523f, 22f, 13f, 22f)
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
        moveTo(16f, 19f)
        horizontalLineTo(21.9957f)
        moveTo(18.9979f, 16f)
        verticalLineTo(22f)
      }
    }.build()
    return _vector!!
  }
