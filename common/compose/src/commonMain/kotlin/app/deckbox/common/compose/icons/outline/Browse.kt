package app.deckbox.common.compose.icons.outline

import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.DeckBoxIcons


private var _browse: ImageVector? = null

val DeckBoxIcons.Outline.Browse: ImageVector
  get() {
    if (_browse != null) {
      return _browse!!
    }
    _browse = ImageVector.Builder(
      name = "Outline.Browse",
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
        moveTo(6f, 2f)
        curveTo(4.895f, 2f, 4f, 2.895f, 4f, 4f)
        lineTo(4f, 20f)
        curveTo(4f, 21.105f, 4.895f, 22f, 6f, 22f)
        lineTo(12.259766f, 22f)
        curveTo(11.8378f, 21.396f, 11.5099f, 20.723f, 11.2949f, 20f)
        lineTo(6f, 20f)
        lineTo(6f, 16f)
        lineTo(11.294922f, 16f)
        curveTo(11.5099f, 15.277f, 11.8378f, 14.604f, 12.2598f, 14f)
        lineTo(6f, 14f)
        lineTo(6f, 10f)
        lineTo(18f, 10f)
        lineTo(18f, 11f)
        curveTo(18.695f, 11f, 19.366f, 11.1059f, 20f, 11.2949f)
        lineTo(20f, 4f)
        curveTo(20f, 2.895f, 19.105f, 2f, 18f, 2f)
        lineTo(6f, 2f)
        close()
        moveTo(6f, 4f)
        lineTo(18f, 4f)
        lineTo(18f, 8f)
        lineTo(6f, 8f)
        lineTo(6f, 4f)
        close()
        moveTo(18f, 13f)
        curveTo(15.2f, 13f, 13f, 15.2f, 13f, 18f)
        curveTo(13f, 20.8f, 15.2f, 23f, 18f, 23f)
        curveTo(19f, 23f, 20.0008f, 22.6992f, 20.8008f, 22.1992f)
        lineTo(22.599609f, 24f)
        lineTo(24f, 22.599609f)
        lineTo(22.199219f, 20.800781f)
        curveTo(22.6992f, 20.0008f, 23f, 19f, 23f, 18f)
        curveTo(23f, 15.2f, 20.8f, 13f, 18f, 13f)
        close()
        moveTo(18f, 15f)
        curveTo(19.7f, 15f, 21f, 16.3f, 21f, 18f)
        curveTo(21f, 19.7f, 19.7f, 21f, 18f, 21f)
        curveTo(16.3f, 21f, 15f, 19.7f, 15f, 18f)
        curveTo(15f, 16.3f, 16.3f, 15f, 18f, 15f)
        close()
      }
    }.build()
    return _browse!!
  }

