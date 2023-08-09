package app.deckbox.common.compose.icons.types

import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.DeckBoxIcons


private var _water: ImageVector? = null

val DeckBoxIcons.Types.Water: ImageVector
  get() {
    if (_water != null) {
      return _water!!
    }
    _water = ImageVector.Builder(
      name = "Types.Water",
      defaultWidth = 24.dp,
      defaultHeight = 24.dp,
      viewportWidth = 24f,
      viewportHeight = 24f
    ).apply {
      group {
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
          moveTo(19.9906f, 4.05707f)
          curveTo(16.4671f, 5.0059f, 14.5274f, 6.3865f, 14.1716f, 8.1987f)
          curveTo(13.7045f, 10.5775f, 16.0266f, 12.0338f, 17.6582f, 14.5723f)
          curveTo(18.0672f, 15.2087f, 18.3231f, 16.0493f, 18.4261f, 17.0943f)
          curveTo(18.5287f, 18.7274f, 17.6246f, 19.9777f, 15.7137f, 20.8455f)
          curveTo(13.8029f, 21.7132f, 11.1886f, 21.5938f, 7.8708f, 20.4874f)
          curveTo(6.396f, 19.8291f, 5.2401f, 18.9786f, 4.4029f, 17.9359f)
          curveTo(3.5658f, 16.8931f, 3.1227f, 15.5799f, 3.0735f, 13.9965f)
          curveTo(3.0909f, 11.1858f, 4.5661f, 8.9024f, 7.4992f, 7.1463f)
          curveTo(10.4323f, 5.3902f, 14.5961f, 4.3604f, 19.9906f, 4.0571f)
          close()
          moveTo(9.16849f, 19.8892f)
          curveTo(10.0196f, 20.1809f, 11.8841f, 20.616f, 12.1198f, 19.8892f)
          curveTo(12.3554f, 19.1624f, 11.0979f, 18.1907f, 10.1388f, 17.8507f)
          curveTo(9.1797f, 17.5107f, 7.1765f, 17.0451f, 6.9638f, 17.8507f)
          curveTo(6.7511f, 18.6564f, 8.3174f, 19.5975f, 9.1685f, 19.8892f)
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
          moveTo(24f, 0f)
          horizontalLineTo(0f)
          verticalLineTo(24f)
          horizontalLineTo(24f)
          verticalLineTo(0f)
          close()
        }
      }
    }.build()
    return _water!!
  }

