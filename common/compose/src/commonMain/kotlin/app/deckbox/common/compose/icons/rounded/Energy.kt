package app.deckbox.common.compose.icons.rounded

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp


private var _energy: ImageVector? = null

val Icons.Rounded.Energy: ImageVector
  get() {
    if (_energy != null) {
      return _energy!!
    }
    _energy = ImageVector.Builder(
      name = "Rounded.Energy",
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
        moveTo(18.399f, 11f)
        horizontalLineTo(14f)
        lineToRelative(0.992f, -8.443f)
        curveToRelative(0.054f, -0.482f, -0.541f, -0.75f, -0.867f, -0.392f)
        lineToRelative(-8.968f, 9.831f)
        curveTo(4.807f, 12.382f, 5.08f, 13f, 5.601f, 13f)
        horizontalLineTo(10f)
        lineToRelative(0.008f, 8.443f)
        curveToRelative(-0.054f, 0.482f, 0.541f, 0.75f, 0.867f, 0.392f)
        lineToRelative(7.968f, -9.831f)
        curveTo(19.193f, 11.618f, 18.92f, 11f, 18.399f, 11f)
        close()
      }
    }.build()
    return _energy!!
  }

