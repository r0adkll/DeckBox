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


private var _pokeball: ImageVector? = null

val Icons.Outlined.Pokeball: ImageVector
  get() {
    if (_pokeball != null) {
      return _pokeball!!
    }
    _pokeball = ImageVector.Builder(
      name = "Outlined.Pokeball",
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
        moveTo(12f, 2f)
        curveTo(6.489f, 2f, 2f, 6.489f, 2f, 12f)
        curveTo(2f, 17.511f, 6.489f, 22f, 12f, 22f)
        curveTo(17.511f, 22f, 22f, 17.511f, 22f, 12f)
        curveTo(22f, 6.489f, 17.511f, 2f, 12f, 2f)
        close()
        moveTo(12f, 4f)
        curveTo(16.0907f, 4f, 19.4412f, 7.0458f, 19.9316f, 11f)
        lineTo(14.8125f, 11f)
        curveTo(14.3951f, 9.8427f, 13.2932f, 9f, 12f, 9f)
        curveTo(10.7068f, 9f, 9.6049f, 9.8427f, 9.1875f, 11f)
        lineTo(4.0683594f, 11f)
        curveTo(4.5588f, 7.0458f, 7.9093f, 4f, 12f, 4f)
        close()
        moveTo(12f, 11f)
        curveTo(12.5641f, 11f, 13f, 11.4359f, 13f, 12f)
        curveTo(13f, 12.5641f, 12.5641f, 13f, 12f, 13f)
        curveTo(11.4359f, 13f, 11f, 12.5641f, 11f, 12f)
        curveTo(11f, 11.4359f, 11.4359f, 11f, 12f, 11f)
        close()
        moveTo(4.0683594f, 13f)
        lineTo(9.1875f, 13f)
        curveTo(9.6049f, 14.1573f, 10.7068f, 15f, 12f, 15f)
        curveTo(13.2932f, 15f, 14.3951f, 14.1573f, 14.8125f, 13f)
        lineTo(19.931641f, 13f)
        curveTo(19.4412f, 16.9542f, 16.0907f, 20f, 12f, 20f)
        curveTo(7.9093f, 20f, 4.5588f, 16.9542f, 4.0684f, 13f)
        close()
      }
    }.build()
    return _pokeball!!
  }

