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

private var _wrench: ImageVector? = null

val Icons.Rounded.Wrench: ImageVector
  get() {
    if (_wrench != null) {
      return _wrench!!
    }
    _wrench = ImageVector.Builder(
      name = "Rounded.Wrench",
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
        moveTo(15f, 3f)
        curveTo(11.691f, 3f, 9f, 5.691f, 9f, 9f)
        curveTo(9f, 9.518f, 9.0709f, 10.0359f, 9.2109f, 10.5469f)
        lineTo(4.6347656f, 15.123047f)
        curveTo(3.1295f, 15.5138f, 2f, 16.879f, 2f, 18.5f)
        curveTo(2f, 20.4212f, 3.5788f, 22f, 5.5f, 22f)
        curveTo(7.121f, 22f, 8.4862f, 20.8706f, 8.877f, 19.3652f)
        lineTo(13.453125f, 14.789062f)
        curveTo(13.9641f, 14.9291f, 14.482f, 15f, 15f, 15f)
        curveTo(18.309f, 15f, 21f, 12.309f, 21f, 9f)
        curveTo(21f, 8.35f, 20.889f, 7.7005f, 20.668f, 7.0645f)
        curveTo(20.668f, 7.0645f, 20.3747f, 6.4534f, 19.8438f, 6.9844f)
        lineTo(18.414062f, 8.4140625f)
        curveTo(18.0241f, 8.8051f, 17.512f, 9f, 17f, 9f)
        curveTo(16.488f, 9f, 15.9759f, 8.8051f, 15.5859f, 8.4141f)
        curveTo(14.8049f, 7.6331f, 14.8049f, 6.3669f, 15.5859f, 5.5859f)
        lineTo(17f, 4.171875f)
        curveTo(17.594f, 3.5779f, 17f, 3.3535f, 17f, 3.3535f)
        curveTo(16.343f, 3.1185f, 15.672f, 3f, 15f, 3f)
        close()
        moveTo(5.5f, 17f)
        curveTo(6.3403f, 17f, 7f, 17.6597f, 7f, 18.5f)
        curveTo(7f, 19.3403f, 6.3403f, 20f, 5.5f, 20f)
        curveTo(4.6597f, 20f, 4f, 19.3403f, 4f, 18.5f)
        curveTo(4f, 17.6597f, 4.6597f, 17f, 5.5f, 17f)
        close()
      }
    }.build()
    return _wrench!!
  }
