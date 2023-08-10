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

private var _steel: ImageVector? = null

val DeckBoxIcons.Types.Steel: ImageVector
  get() {
    if (_steel != null) {
      return _steel!!
    }
    _steel = ImageVector.Builder(
      name = "Types.Steel",
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
          moveTo(7.05081f, 8.8931f)
          horizontalLineTo(16.8253f)
          lineTo(12f, 17.3791f)
          lineTo(7.05081f, 8.8931f)
          close()
          moveTo(14.6117f, 6.65454f)
          lineTo(15.9137f, 4.45663f)
          lineTo(19.146f, 4.77052f)
          lineTo(22.085f, 9.78994f)
          lineTo(20.6737f, 12.5619f)
          horizontalLineTo(18.3484f)
          lineTo(19.8005f, 9.78994f)
          lineTo(18.0933f, 6.65454f)
          horizontalLineTo(14.6117f)
          close()
          moveTo(9.57335f, 6.65791f)
          horizontalLineTo(6.09172f)
          lineTo(4.38452f, 9.79331f)
          lineTo(5.83668f, 12.5653f)
          horizontalLineTo(3.51136f)
          lineTo(2.10001f, 9.79331f)
          lineTo(5.039f, 4.7739f)
          lineTo(8.27137f, 4.46001f)
          lineTo(9.57335f, 6.65791f)
          close()
          moveTo(8.60269f, 17.1435f)
          lineTo(10.3277f, 19.9823f)
          horizontalLineTo(13.7479f)
          lineTo(15.552f, 17.1435f)
          lineTo(16.8253f, 19.1216f)
          lineTo(14.933f, 21.9184f)
          horizontalLineTo(9.13095f)
          lineTo(7.33531f, 19.1216f)
          lineTo(8.60269f, 17.1435f)
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
          horizontalLineTo(0f)
          verticalLineTo(24f)
          horizontalLineTo(24f)
          verticalLineTo(0f)
          close()
        }
      }
    }.build()
    return _steel!!
  }
