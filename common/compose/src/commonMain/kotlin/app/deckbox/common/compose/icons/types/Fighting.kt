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


private var _fighting: ImageVector? = null

val DeckBoxIcons.Types.Fighting: ImageVector
  get() {
    if (_fighting != null) {
      return _fighting!!
    }
    _fighting = ImageVector.Builder(
      name = "Types.Fighting",
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
          moveTo(3.56487f, 5.0503f)
          lineTo(4.01203f, 4.48872f)
          horizontalLineTo(6.72171f)
          lineTo(7.18602f, 5.0503f)
          verticalLineTo(10.8407f)
          lineTo(6.72171f, 11.3552f)
          horizontalLineTo(4.01203f)
          lineTo(3.56487f, 10.8407f)
          verticalLineTo(5.0503f)
          close()
          moveTo(16.76f, 5.05157f)
          lineTo(17.2072f, 4.49f)
          horizontalLineTo(19.9168f)
          lineTo(20.3812f, 5.05157f)
          verticalLineTo(10.842f)
          lineTo(19.9168f, 11.3565f)
          horizontalLineTo(17.2072f)
          lineTo(16.76f, 10.842f)
          verticalLineTo(5.05157f)
          close()
          moveTo(7.92501f, 4.0255f)
          lineTo(8.37217f, 3.44491f)
          horizontalLineTo(11.0818f)
          lineTo(11.5462f, 4.0255f)
          verticalLineTo(10.8503f)
          lineTo(11.0818f, 11.3552f)
          horizontalLineTo(8.37217f)
          lineTo(7.92501f, 10.8503f)
          verticalLineTo(4.0255f)
          close()
          moveTo(12.45f, 4.02059f)
          lineTo(12.8972f, 3.44f)
          horizontalLineTo(15.6068f)
          lineTo(16.0712f, 4.02059f)
          verticalLineTo(10.8454f)
          lineTo(15.6068f, 11.3503f)
          horizontalLineTo(12.8972f)
          lineTo(12.45f, 10.8454f)
          verticalLineTo(4.02059f)
          close()
          moveTo(4.37898f, 12.2197f)
          horizontalLineTo(11.5462f)
          lineTo(12.45f, 13.2176f)
          verticalLineTo(14.8f)
          lineTo(11.5462f, 15.724f)
          horizontalLineTo(9.64561f)
          lineTo(8.80944f, 16.6268f)
          verticalLineTo(18.3654f)
          lineTo(7.92501f, 19.2284f)
          horizontalLineTo(4.37898f)
          lineTo(3.56487f, 18.3654f)
          verticalLineTo(13.069f)
          lineTo(4.37898f, 12.2197f)
          close()
          moveTo(14.1943f, 12.2197f)
          horizontalLineTo(19.6861f)
          lineTo(20.4797f, 13.1915f)
          verticalLineTo(18.37f)
          lineTo(19.6861f, 19.2284f)
          horizontalLineTo(16.9966f)
          lineTo(16.0712f, 20.095f)
          verticalLineTo(21.8731f)
          horizontalLineTo(8.00744f)
          verticalLineTo(20.095f)
          horizontalLineTo(8.93028f)
          lineTo(9.73558f, 19.2284f)
          verticalLineTo(17.4312f)
          lineTo(10.6541f, 16.5112f)
          horizontalLineTo(12.45f)
          lineTo(13.3112f, 15.724f)
          verticalLineTo(13.1915f)
          lineTo(14.1943f, 12.2197f)
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
    return _fighting!!
  }

