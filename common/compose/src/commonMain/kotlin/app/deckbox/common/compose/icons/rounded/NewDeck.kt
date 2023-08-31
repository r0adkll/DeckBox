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

private var _newDeck: ImageVector? = null

val Icons.Rounded.NewDeck: ImageVector
  get() {
    if (_newDeck != null) {
      return _newDeck!!
    }
    _newDeck = ImageVector.Builder(
      name = "Rounded.NewDeck",
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
        moveTo(19f, 18f)
        horizontalLineTo(5f)
        curveTo(4.4477f, 18f, 4f, 17.5523f, 4f, 17f)
        curveTo(4f, 16.4477f, 4.4477f, 16f, 5f, 16f)
        horizontalLineTo(19f)
        curveTo(19.5523f, 16f, 20f, 16.4477f, 20f, 17f)
        curveTo(20f, 17.5523f, 19.5523f, 18f, 19f, 18f)
        close()
        moveTo(19f, 22f)
        horizontalLineTo(5f)
        curveTo(4.4477f, 22f, 4f, 21.5523f, 4f, 21f)
        curveTo(4f, 20.4477f, 4.4477f, 20f, 5f, 20f)
        horizontalLineTo(19f)
        curveTo(19.5523f, 20f, 20f, 20.4477f, 20f, 21f)
        curveTo(20f, 21.5523f, 19.5523f, 22f, 19f, 22f)
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
        moveTo(20f, 12f)
        curveTo(20f, 13.1046f, 19.1046f, 14f, 18f, 14f)
        horizontalLineTo(6f)
        curveTo(4.8954f, 14f, 4f, 13.1046f, 4f, 12f)
        verticalLineTo(4f)
        curveTo(4f, 2.8954f, 4.8954f, 2f, 6f, 2f)
        horizontalLineTo(13f)
        curveTo(14.5f, 2f, 14.5f, 4f, 13f, 4f)
        horizontalLineTo(7f)
        curveTo(6.4477f, 4f, 6f, 4.4477f, 6f, 5f)
        verticalLineTo(11f)
        curveTo(6f, 11.5523f, 6.4477f, 12f, 7f, 12f)
        horizontalLineTo(17f)
        curveTo(17.5523f, 12f, 18f, 11.5523f, 18f, 11f)
        verticalLineTo(10f)
        curveTo(18f, 8.5f, 20f, 8.5f, 20f, 10f)
        verticalLineTo(12f)
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
        moveTo(18.4942f, 0.518037f)
        curveTo(18.7716f, 0.234f, 19.2284f, 0.234f, 19.5058f, 0.518f)
        lineTo(19.9518f, 0.974786f)
        curveTo(20.0869f, 1.1131f, 20.2728f, 1.1901f, 20.4661f, 1.1878f)
        lineTo(21.1044f, 1.1802f)
        curveTo(21.5015f, 1.1755f, 21.8245f, 1.4985f, 21.8198f, 1.8956f)
        lineTo(21.8122f, 2.5339f)
        curveTo(21.8099f, 2.7272f, 21.8869f, 2.9131f, 22.0252f, 3.0482f)
        lineTo(22.482f, 3.49416f)
        curveTo(22.766f, 3.7716f, 22.766f, 4.2284f, 22.482f, 4.5058f)
        lineTo(22.0252f, 4.95184f)
        curveTo(21.8869f, 5.0869f, 21.8099f, 5.2728f, 21.8122f, 5.4661f)
        lineTo(21.8198f, 6.10444f)
        curveTo(21.8245f, 6.5015f, 21.5015f, 6.8245f, 21.1044f, 6.8198f)
        lineTo(20.4661f, 6.8122f)
        curveTo(20.2728f, 6.8099f, 20.0869f, 6.8869f, 19.9518f, 7.0252f)
        lineTo(19.5058f, 7.48196f)
        curveTo(19.2284f, 7.766f, 18.7716f, 7.766f, 18.4942f, 7.482f)
        lineTo(18.0482f, 7.02521f)
        curveTo(17.9131f, 6.8869f, 17.7272f, 6.8099f, 17.5339f, 6.8122f)
        lineTo(16.8956f, 6.8198f)
        curveTo(16.4985f, 6.8245f, 16.1755f, 6.5015f, 16.1802f, 6.1044f)
        lineTo(16.1878f, 5.4661f)
        curveTo(16.1901f, 5.2728f, 16.1131f, 5.0869f, 15.9748f, 4.9518f)
        lineTo(15.518f, 4.50584f)
        curveTo(15.234f, 4.2284f, 15.234f, 3.7716f, 15.518f, 3.4942f)
        lineTo(15.9748f, 3.04816f)
        curveTo(16.1131f, 2.9131f, 16.1901f, 2.7272f, 16.1878f, 2.5339f)
        lineTo(16.1802f, 1.89556f)
        curveTo(16.1755f, 1.4985f, 16.4985f, 1.1755f, 16.8956f, 1.1802f)
        lineTo(17.5339f, 1.1878f)
        curveTo(17.7272f, 1.1901f, 17.9131f, 1.1131f, 18.0482f, 0.9748f)
        lineTo(18.4942f, 0.518037f)
        close()
      }
    }.build()
    return _newDeck!!
  }
