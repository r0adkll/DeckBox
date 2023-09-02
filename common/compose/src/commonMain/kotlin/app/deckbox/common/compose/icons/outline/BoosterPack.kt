package app.deckbox.common.compose.icons.outline

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private var _boosterPack: ImageVector? = null

val Icons.Outlined.BoosterPack: ImageVector
  get() {
    if (_boosterPack != null) {
      return _boosterPack!!
    }
    _boosterPack = ImageVector.Builder(
      name = "Outlined.BoosterPack",
      defaultWidth = 24.dp,
      defaultHeight = 24.dp,
      viewportWidth = 24f,
      viewportHeight = 24f
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
        pathFillType = PathFillType.EvenOdd
      ) {
        moveTo(2.99999f, 7f)
        verticalLineTo(20f)
        curveTo(3f, 21.1046f, 3.8954f, 22f, 5f, 22f)
        horizontalLineTo(19f)
        curveTo(20.1046f, 22f, 21f, 21.1046f, 21f, 20f)
        verticalLineTo(7f)
        lineTo(19f, 8f)
        lineTo(4.99999f, 8f)
        lineTo(2.99999f, 7f)
        close()
        moveTo(5.99999f, 20f)
        curveTo(5.4477f, 20f, 5f, 19.5523f, 5f, 19f)
        lineTo(5f, 10f)
        horizontalLineTo(19f)
        lineTo(19f, 19f)
        curveTo(19f, 19.5523f, 18.5523f, 20f, 18f, 20f)
        horizontalLineTo(5.99999f)
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
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(3.99999f, 3f)
        curveTo(3f, 3f, 3f, 3f, 3f, 5.5f)
        lineTo(2.99999f, 7f)
        lineTo(4.99999f, 8f)
        lineTo(5f, 5.5f)
        curveTo(5f, 5.5f, 5.1268f, 4.5f, 6f, 4.5f)
        curveTo(6.8732f, 4.5f, 7.1268f, 5.5f, 8f, 5.5f)
        curveTo(8.8732f, 5.5f, 9.1268f, 4.5f, 10f, 4.5f)
        curveTo(10.8732f, 4.5f, 11.1268f, 5.5f, 12f, 5.5f)
        curveTo(12.8732f, 5.5f, 13.1268f, 4.5f, 14f, 4.5f)
        curveTo(14.8732f, 4.5f, 15.1268f, 5.5f, 16f, 5.5f)
        curveTo(16.8732f, 5.5f, 17.1268f, 4.5f, 18f, 4.5f)
        curveTo(18.8732f, 4.5f, 19f, 5.5f, 19f, 5.5f)
        lineTo(19f, 8f)
        lineTo(21f, 7f)
        verticalLineTo(5.5f)
        curveTo(21f, 3f, 21f, 3f, 20f, 3f)
        curveTo(19f, 3f, 18.8732f, 2f, 18f, 2f)
        curveTo(17.1268f, 2f, 16.8732f, 3f, 16f, 3f)
        curveTo(15.1268f, 3f, 14.8732f, 2f, 14f, 2f)
        curveTo(13.1268f, 2f, 12.8732f, 3f, 12f, 3f)
        curveTo(11.1268f, 3f, 10.8732f, 2f, 10f, 2f)
        curveTo(9.1267f, 2f, 8.8732f, 3f, 8f, 3f)
        curveTo(7.1268f, 3f, 6.8732f, 2f, 6f, 2f)
        curveTo(5.1268f, 2f, 5f, 3f, 4f, 3f)
        close()
      }
    }.build()
    return _boosterPack!!
  }
