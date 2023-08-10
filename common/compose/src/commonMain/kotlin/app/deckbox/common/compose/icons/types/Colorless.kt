package app.deckbox.common.compose.icons.types

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.DeckBoxIcons

private var _colorless: ImageVector? = null

val DeckBoxIcons.Types.Colorless: ImageVector
  get() {
    if (_colorless != null) {
      return _colorless!!
    }
    _colorless = ImageVector.Builder(
      name = "Types.Colorless",
      defaultWidth = 24.dp,
      defaultHeight = 24.dp,
      viewportWidth = 24f,
      viewportHeight = 24f,
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
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(12.138f, 1.81274f)
          curveTo(12.6522f, 5.1899f, 13.3704f, 7.1802f, 14.2924f, 7.7838f)
          curveTo(15.2145f, 8.3873f, 17.4692f, 8.0808f, 21.0566f, 6.8643f)
          curveTo(18.0208f, 9.4008f, 16.503f, 11.1127f, 16.503f, 12f)
          curveTo(16.503f, 12.8873f, 18.0208f, 14.527f, 21.0566f, 16.9189f)
          curveTo(17.4059f, 15.7735f, 15.1512f, 15.47f, 14.2924f, 16.0084f)
          curveTo(13.4336f, 16.5468f, 12.7155f, 18.5223f, 12.138f, 21.935f)
          curveTo(11.6692f, 18.5553f, 10.9259f, 16.5798f, 9.9079f, 16.0084f)
          curveTo(8.8898f, 15.437f, 6.6542f, 15.7405f, 3.201f, 16.9189f)
          curveTo(6.2605f, 14.5274f, 7.7903f, 12.8877f, 7.7903f, 12f)
          curveTo(7.7903f, 11.1122f, 6.2605f, 9.4003f, 3.201f, 6.8643f)
          curveTo(6.9592f, 8.0902f, 9.2433f, 8.3967f, 10.0534f, 7.7838f)
          curveTo(10.8635f, 7.1708f, 11.5584f, 5.1805f, 12.138f, 1.8127f)
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
          moveTo(24f, 0f)
          horizontalLineTo(-9.53674e-7f)
          verticalLineTo(24f)
          horizontalLineTo(24f)
          verticalLineTo(0f)
          close()
        }
      }
    }.build()
    return _colorless!!
  }
