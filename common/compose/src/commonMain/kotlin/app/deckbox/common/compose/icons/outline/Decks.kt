package app.deckbox.common.compose.icons.outline

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


private var _decksOutlined: ImageVector? = null

val DeckBoxIcons.Outline.Decks: ImageVector
  get() {
    if (_decksOutlined != null) {
      return _decksOutlined!!
    }
    _decksOutlined = ImageVector.Builder(
      name = "Outline.Decks",
      defaultWidth = 24.dp,
      defaultHeight = 24.dp,
      viewportWidth = 24f,
      viewportHeight = 24f,
    ).apply {
      group {
        path(
          fill = SolidColor(Color(0xFFFFFFFF)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(20f, 4f)
          verticalLineTo(12f)
          curveTo(20f, 13.1046f, 19.1046f, 14f, 18f, 14f)
          horizontalLineTo(6f)
          curveTo(4.8954f, 14f, 4f, 13.1046f, 4f, 12f)
          verticalLineTo(4f)
          curveTo(4f, 2.8954f, 4.8954f, 2f, 6f, 2f)
          horizontalLineTo(18f)
          curveTo(19.1046f, 2f, 20f, 2.8954f, 20f, 4f)
          close()
          moveTo(7f, 12f)
          horizontalLineTo(17f)
          curveTo(17.5523f, 12f, 18f, 11.5523f, 18f, 11f)
          verticalLineTo(5f)
          curveTo(18f, 4.4477f, 17.5523f, 4f, 17f, 4f)
          horizontalLineTo(7f)
          curveTo(6.4477f, 4f, 6f, 4.4477f, 6f, 5f)
          verticalLineTo(11f)
          curveTo(6f, 11.5523f, 6.4477f, 12f, 7f, 12f)
          close()
          moveTo(19f, 18f)
          horizontalLineTo(5f)
          curveTo(4.4477f, 18f, 4f, 17.5523f, 4f, 17f)
          curveTo(4f, 16.4477f, 4.4477f, 16f, 5f, 16f)
          horizontalLineTo(19f)
          curveTo(19.5523f, 16f, 20f, 16.4477f, 20f, 17f)
          curveTo(20f, 17.5523f, 19.5523f, 18f, 19f, 18f)
          close()
          moveTo(19f, 22f)
          horizontalLineTo(5f)
          curveTo(4.4477f, 22f, 4f, 21.5523f, 4f, 21f)
          curveTo(4f, 20.4477f, 4.4477f, 20f, 5f, 20f)
          horizontalLineTo(19f)
          curveTo(19.5523f, 20f, 20f, 20.4477f, 20f, 21f)
          curveTo(20f, 21.5523f, 19.5523f, 22f, 19f, 22f)
          close()
        }
      }
    }.build()
    return _decksOutlined!!
  }
