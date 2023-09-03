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

val Icons.Rounded.AddBoosterPack: ImageVector
  get() {
    if (_vector != null) {
      return _vector!!
    }
    _vector = ImageVector.Builder(
      name = "Rounded.AddBoosterPack",
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
        moveTo(8.00004f, 22f)
        horizontalLineTo(13f)
        curveTo(14.5f, 22f, 14.5f, 20f, 13f, 20f)
        lineTo(8.00004f, 20f)
        verticalLineTo(22f)
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
        moveTo(5.00005f, 10f)
        horizontalLineTo(19f)
        verticalLineTo(13f)
        curveTo(19f, 14.5f, 21f, 14.5f, 21f, 13f)
        lineTo(21f, 7f)
        lineTo(19f, 8f)
        lineTo(5.00004f, 8f)
        lineTo(5.00005f, 5.5f)
        curveTo(5f, 5.5f, 5.1268f, 4.5f, 6f, 4.5f)
        curveTo(6.8733f, 4.5f, 7.1268f, 5.5f, 8f, 5.5f)
        curveTo(8.8733f, 5.5f, 9.1268f, 4.5f, 10f, 4.5f)
        curveTo(10.8733f, 4.5f, 11.1268f, 5.5f, 12f, 5.5f)
        curveTo(12.8733f, 5.5f, 13.1268f, 4.5f, 14f, 4.5f)
        curveTo(14.8733f, 4.5f, 15.1268f, 5.5f, 16f, 5.5f)
        curveTo(16.8733f, 5.5f, 17.1268f, 4.5f, 18f, 4.5f)
        curveTo(18.8733f, 4.5f, 19f, 5.5f, 19f, 5.5f)
        lineTo(19f, 8f)
        lineTo(21f, 7f)
        verticalLineTo(5.5f)
        curveTo(21f, 3f, 21f, 3f, 20f, 3f)
        curveTo(19f, 3f, 18.8733f, 2f, 18f, 2f)
        curveTo(17.1268f, 2f, 16.8733f, 3f, 16f, 3f)
        curveTo(15.1268f, 3f, 14.8733f, 2f, 14f, 2f)
        curveTo(13.1268f, 2f, 12.8733f, 3f, 12f, 3f)
        curveTo(11.1268f, 3f, 10.8733f, 2f, 10f, 2f)
        curveTo(9.1268f, 2f, 8.8733f, 3f, 8f, 3f)
        curveTo(7.1268f, 3f, 6.8733f, 2f, 6f, 2f)
        curveTo(5.1268f, 2f, 5f, 3f, 4f, 3f)
        curveTo(3f, 3f, 3f, 3f, 3f, 5.5f)
        lineTo(3.00004f, 7f)
        verticalLineTo(10f)
        horizontalLineTo(5.00005f)
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
        moveTo(5.00005f, 10f)
        horizontalLineTo(3.00004f)
        verticalLineTo(20f)
        curveTo(3f, 21f, 3.8164f, 22f, 5f, 22f)
        horizontalLineTo(8.00004f)
        verticalLineTo(20f)
        horizontalLineTo(6.00004f)
        curveTo(5.4023f, 20f, 5f, 19.5f, 5f, 19f)
        lineTo(5.00005f, 10f)
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
