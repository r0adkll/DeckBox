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

val Icons.Rounded.NewBoosterPack: ImageVector
  get() {
    if (_vector != null) {
      return _vector!!
    }
    _vector = ImageVector.Builder(
      name = "Rounded.NewBoosterPack",
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
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(3f, 20f)
        curveTo(3f, 21.1046f, 3.8954f, 22f, 5f, 22f)
        horizontalLineTo(19f)
        curveTo(20.1046f, 22f, 21f, 21.1075f, 21f, 20.0029f)
        curveTo(21f, 17.1308f, 21f, 12.0617f, 21f, 11f)
        curveTo(21f, 9.5f, 19f, 9.5f, 19f, 11f)
        curveTo(19f, 12.1602f, 19f, 16.91f, 19f, 19.0024f)
        curveTo(19f, 19.5547f, 18.5523f, 20f, 18f, 20f)
        horizontalLineTo(6f)
        curveTo(5.4477f, 20f, 5f, 19.5523f, 5f, 19f)
        lineTo(5f, 10f)
        curveTo(5f, 10f, 12.5f, 10f, 14f, 10f)
        curveTo(15.5f, 10f, 15.5f, 8f, 14f, 8f)
        curveTo(12.5f, 8f, 5f, 8f, 5f, 8f)
        lineTo(3f, 7f)
        verticalLineTo(20f)
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
        moveTo(4f, 3f)
        curveTo(3f, 3f, 3f, 3f, 3f, 5.5f)
        lineTo(3f, 7f)
        lineTo(5f, 8f)
        lineTo(5f, 5.5f)
        curveTo(5f, 5.5f, 5.1268f, 4.5f, 6f, 4.5f)
        curveTo(6.8732f, 4.5f, 7.1268f, 5.5f, 8f, 5.5f)
        curveTo(8.8732f, 5.5f, 9.1268f, 4.5f, 10f, 4.5f)
        curveTo(10.8732f, 4.5f, 11.1268f, 5.5f, 12f, 5.5f)
        curveTo(14f, 5.5f, 14f, 3f, 12f, 3f)
        curveTo(11.1268f, 3f, 10.8732f, 2f, 10f, 2f)
        curveTo(9.1268f, 2f, 8.8732f, 3f, 8f, 3f)
        curveTo(7.1268f, 3f, 6.8732f, 2f, 6f, 2f)
        curveTo(5.1268f, 2f, 5f, 3f, 4f, 3f)
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
        moveTo(18.4942f, 1.51804f)
        curveTo(18.7716f, 1.234f, 19.2284f, 1.234f, 19.5058f, 1.518f)
        lineTo(19.9518f, 1.97479f)
        curveTo(20.0869f, 2.1131f, 20.2728f, 2.1901f, 20.4661f, 2.1878f)
        lineTo(21.1044f, 2.1802f)
        curveTo(21.5015f, 2.1755f, 21.8245f, 2.4985f, 21.8198f, 2.8956f)
        lineTo(21.8122f, 3.5339f)
        curveTo(21.8099f, 3.7272f, 21.8869f, 3.9131f, 22.0252f, 4.0482f)
        lineTo(22.482f, 4.49416f)
        curveTo(22.766f, 4.7716f, 22.766f, 5.2284f, 22.482f, 5.5058f)
        lineTo(22.0252f, 5.95184f)
        curveTo(21.8869f, 6.0869f, 21.8099f, 6.2728f, 21.8122f, 6.4661f)
        lineTo(21.8198f, 7.10444f)
        curveTo(21.8245f, 7.5015f, 21.5015f, 7.8245f, 21.1044f, 7.8198f)
        lineTo(20.4661f, 7.8122f)
        curveTo(20.2728f, 7.8099f, 20.0869f, 7.8869f, 19.9518f, 8.0252f)
        lineTo(19.5058f, 8.48196f)
        curveTo(19.2284f, 8.766f, 18.7716f, 8.766f, 18.4942f, 8.482f)
        lineTo(18.0482f, 8.02521f)
        curveTo(17.9131f, 7.8869f, 17.7272f, 7.8099f, 17.5339f, 7.8122f)
        lineTo(16.8956f, 7.8198f)
        curveTo(16.4985f, 7.8245f, 16.1755f, 7.5015f, 16.1802f, 7.1044f)
        lineTo(16.1878f, 6.4661f)
        curveTo(16.1901f, 6.2728f, 16.1131f, 6.0869f, 15.9748f, 5.9518f)
        lineTo(15.518f, 5.50584f)
        curveTo(15.234f, 5.2284f, 15.234f, 4.7716f, 15.518f, 4.4942f)
        lineTo(15.9748f, 4.04816f)
        curveTo(16.1131f, 3.9131f, 16.1901f, 3.7272f, 16.1878f, 3.5339f)
        lineTo(16.1802f, 2.89556f)
        curveTo(16.1755f, 2.4985f, 16.4985f, 2.1755f, 16.8956f, 2.1802f)
        lineTo(17.5339f, 2.1878f)
        curveTo(17.7272f, 2.1901f, 17.9131f, 2.1131f, 18.0482f, 1.9748f)
        lineTo(18.4942f, 1.51804f)
        close()
      }
    }.build()
    return _vector!!
  }
