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

val Icons.Rounded.Import: ImageVector
  get() {
    if (_vector != null) {
      return _vector!!
    }
    _vector = ImageVector.Builder(
      name = "vector",
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
        moveTo(8f, 2f)
        curveTo(6.895f, 2f, 6f, 2.895f, 6f, 4f)
        lineTo(6f, 11f)
        lineTo(11f, 11f)
        lineTo(11f, 9.5996094f)
        curveTo(11f, 9.2766f, 11.1951f, 8.9853f, 11.4941f, 8.8613f)
        curveTo(11.7941f, 8.7383f, 12.1362f, 8.8052f, 12.3652f, 9.0332f)
        lineTo(14.765625f, 11.433594f)
        curveTo(15.0776f, 11.7456f, 15.0776f, 12.2525f, 14.7656f, 12.5645f)
        lineTo(12.365234f, 14.964844f)
        curveTo(12.2112f, 15.1188f, 12.0088f, 15.1992f, 11.8008f, 15.1992f)
        curveTo(11.6978f, 15.1992f, 11.5931f, 15.1797f, 11.4941f, 15.1387f)
        curveTo(11.1951f, 15.0147f, 11f, 14.7234f, 11f, 14.4004f)
        lineTo(11f, 13f)
        lineTo(6f, 13f)
        lineTo(6f, 20f)
        curveTo(6f, 21.105f, 6.895f, 22f, 8f, 22f)
        lineTo(16f, 22f)
        curveTo(17.105f, 22f, 18f, 21.105f, 18f, 20f)
        lineTo(18f, 4f)
        curveTo(18f, 2.895f, 17.105f, 2f, 16f, 2f)
        lineTo(8f, 2f)
        close()
        moveTo(6f, 13f)
        lineTo(6f, 11f)
        lineTo(3f, 11f)
        curveTo(2.448f, 11f, 2f, 11.448f, 2f, 12f)
        curveTo(2f, 12.552f, 2.448f, 13f, 3f, 13f)
        lineTo(6f, 13f)
        close()
      }
    }.build()
    return _vector!!
  }

