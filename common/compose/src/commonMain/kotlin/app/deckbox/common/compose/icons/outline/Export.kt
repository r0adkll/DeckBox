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


private var _export: ImageVector? = null

val DeckBoxIcons.Outline.Export: ImageVector
  get() {
    if (_export != null) {
      return _export!!
    }
    _export = ImageVector.Builder(
      name = "Outline.Export",
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
        moveTo(8f, 2f)
        curveTo(6.895f, 2f, 6f, 2.895f, 6f, 4f)
        lineTo(6f, 20f)
        curveTo(6f, 21.105f, 6.895f, 22f, 8f, 22f)
        lineTo(16f, 22f)
        curveTo(17.105f, 22f, 18f, 21.105f, 18f, 20f)
        lineTo(18f, 15f)
        lineTo(16f, 15f)
        lineTo(16f, 20f)
        lineTo(8f, 20f)
        lineTo(8f, 4f)
        lineTo(16f, 4f)
        lineTo(16f, 9f)
        lineTo(18f, 9f)
        lineTo(18f, 4f)
        curveTo(18f, 2.895f, 17.105f, 2f, 16f, 2f)
        lineTo(8f, 2f)
        close()
        moveTo(20f, 8f)
        lineTo(20f, 11f)
        lineTo(11f, 11f)
        lineTo(11f, 13f)
        lineTo(20f, 13f)
        lineTo(20f, 16f)
        lineTo(24f, 12f)
        lineTo(20f, 8f)
        close()
      }
    }.build()
    return _export!!
  }

