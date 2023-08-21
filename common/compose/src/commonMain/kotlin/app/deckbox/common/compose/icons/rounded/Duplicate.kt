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

val Icons.Rounded.Duplicate: ImageVector
  get() {
    if (_vector != null) {
      return _vector!!
    }
    _vector = ImageVector.Builder(
      name = "vector",
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
        moveTo(7f, 2f)
        curveTo(6.448f, 2f, 6f, 2.448f, 6f, 3f)
        curveTo(6f, 3.552f, 6.448f, 4f, 7f, 4f)
        lineTo(20f, 4f)
        lineTo(20f, 17f)
        curveTo(20f, 17.552f, 20.448f, 18f, 21f, 18f)
        curveTo(21.552f, 18f, 22f, 17.552f, 22f, 17f)
        lineTo(22f, 4f)
        curveTo(22f, 2.895f, 21.105f, 2f, 20f, 2f)
        lineTo(7f, 2f)
        close()
        moveTo(4f, 6f)
        curveTo(2.895f, 6f, 2f, 6.895f, 2f, 8f)
        lineTo(2f, 20f)
        curveTo(2f, 21.105f, 2.895f, 22f, 4f, 22f)
        lineTo(16f, 22f)
        curveTo(17.105f, 22f, 18f, 21.105f, 18f, 20f)
        lineTo(18f, 8f)
        curveTo(18f, 6.895f, 17.105f, 6f, 16f, 6f)
        lineTo(4f, 6f)
        close()
        moveTo(10f, 10f)
        curveTo(10.552f, 10f, 11f, 10.448f, 11f, 11f)
        lineTo(11f, 13f)
        lineTo(13f, 13f)
        curveTo(13.552f, 13f, 14f, 13.448f, 14f, 14f)
        curveTo(14f, 14.552f, 13.552f, 15f, 13f, 15f)
        lineTo(11f, 15f)
        lineTo(11f, 17f)
        curveTo(11f, 17.552f, 10.552f, 18f, 10f, 18f)
        curveTo(9.448f, 18f, 9f, 17.552f, 9f, 17f)
        lineTo(9f, 15f)
        lineTo(7f, 15f)
        curveTo(6.448f, 15f, 6f, 14.552f, 6f, 14f)
        curveTo(6f, 13.448f, 6.448f, 13f, 7f, 13f)
        lineTo(9f, 13f)
        lineTo(9f, 11f)
        curveTo(9f, 10.448f, 9.448f, 10f, 10f, 10f)
        close()
      }
    }.build()
    return _vector!!
  }
