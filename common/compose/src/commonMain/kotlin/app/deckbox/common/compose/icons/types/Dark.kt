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

private var _dark: ImageVector? = null

val DeckBoxIcons.Types.Dark: ImageVector
  get() {
    if (_dark != null) {
      return _dark!!
    }
    _dark = ImageVector.Builder(
      name = "Types.Dark",
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
          moveTo(9.36307f, 4.39218f)
          curveTo(8.4705f, 5.1256f, 7.9f, 6.2453f, 7.9f, 7.5f)
          curveTo(7.9f, 9.7091f, 9.6685f, 11.5f, 11.85f, 11.5f)
          curveTo(14.0315f, 11.5f, 15.8f, 9.7091f, 15.8f, 7.5f)
          curveTo(15.8f, 6.2453f, 15.2295f, 5.1256f, 14.3369f, 4.3922f)
          curveTo(18.3495f, 5.3568f, 21.3f, 8.599f, 21.3f, 12.45f)
          curveTo(21.3f, 17.0616f, 17.0691f, 20.8f, 11.85f, 20.8f)
          curveTo(6.6309f, 20.8f, 2.4f, 17.0616f, 2.4f, 12.45f)
          curveTo(2.4f, 8.599f, 5.3505f, 5.3568f, 9.3631f, 4.3922f)
          close()
        }
      }
    }.build()
    return _dark!!
  }
