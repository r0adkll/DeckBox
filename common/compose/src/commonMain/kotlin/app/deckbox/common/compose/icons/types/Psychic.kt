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

private var _psychic: ImageVector? = null

val DeckBoxIcons.Types.Psychic: ImageVector
  get() {
    if (_psychic != null) {
      return _psychic!!
    }
    _psychic = ImageVector.Builder(
      name = "vector",
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
          moveTo(9.74696f, 7.3345f)
          curveTo(7.3987f, 8.0574f, 5.3307f, 9.9522f, 3.5429f, 13.0187f)
          curveTo(5.8822f, 16.5601f, 8.7012f, 18.3308f, 12f, 18.3308f)
          curveTo(15.2988f, 18.3308f, 18.1024f, 16.5601f, 20.411f, 13.0187f)
          curveTo(18.5456f, 9.7766f, 16.357f, 7.8442f, 13.8451f, 7.2218f)
          curveTo(15.7242f, 7.9817f, 17.05f, 9.8235f, 17.05f, 11.975f)
          curveTo(17.05f, 14.8055f, 14.7555f, 17.1f, 11.925f, 17.1f)
          curveTo(9.0945f, 17.1f, 6.8f, 14.8055f, 6.8f, 11.975f)
          curveTo(6.8f, 9.9233f, 8.0056f, 8.1532f, 9.747f, 7.3345f)
          close()
          moveTo(1.38707f, 13.1533f)
          curveTo(3.9944f, 7.6691f, 7.532f, 4.9687f, 12f, 5.0522f)
          curveTo(16.468f, 5.1357f, 19.985f, 7.8361f, 22.5512f, 13.1533f)
          curveTo(19.8802f, 17.9413f, 16.3632f, 20.3352f, 12f, 20.3352f)
          curveTo(7.6368f, 20.3352f, 4.0992f, 17.9413f, 1.3871f, 13.1533f)
          close()
          moveTo(9.42032f, 12.4412f)
          curveTo(9.6859f, 13.5366f, 10.673f, 14.35f, 11.85f, 14.35f)
          curveTo(13.2307f, 14.35f, 14.35f, 13.2307f, 14.35f, 11.85f)
          curveTo(14.35f, 10.4693f, 13.2307f, 9.35f, 11.85f, 9.35f)
          curveTo(11.6272f, 9.35f, 11.4113f, 9.3791f, 11.2058f, 9.4338f)
          curveTo(11.684f, 9.7462f, 12f, 10.2862f, 12f, 10.9f)
          curveTo(12f, 11.8665f, 11.2165f, 12.65f, 10.25f, 12.65f)
          curveTo(9.9498f, 12.65f, 9.6672f, 12.5744f, 9.4203f, 12.4412f)
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
    return _psychic!!
  }
