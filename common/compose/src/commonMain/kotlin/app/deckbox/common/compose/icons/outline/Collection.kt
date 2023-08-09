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


private var _collectionOutline: ImageVector? = null

val DeckBoxIcons.Outline.Collection: ImageVector
  get() {
    if (_collectionOutline != null) {
      return _collectionOutline!!
    }
    _collectionOutline = ImageVector.Builder(
      name = "Outline.Collection",
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
        lineTo(4f, 16f)
        curveTo(4f, 17.105f, 4.895f, 18f, 6f, 18f)
        lineTo(18f, 18f)
        curveTo(19.105f, 18f, 20f, 17.105f, 20f, 16f)
        lineTo(20f, 4f)
        curveTo(20f, 2.895f, 19.105f, 2f, 18f, 2f)
        lineTo(6f, 2f)
        close()
        moveTo(6f, 4f)
        lineTo(8f, 4f)
        lineTo(8f, 10f)
        lineTo(11f, 8.5f)
        lineTo(14f, 10f)
        lineTo(14f, 4f)
        lineTo(18f, 4f)
        lineTo(18f, 16f)
        lineTo(6f, 16f)
        lineTo(6f, 4f)
        close()
        moveTo(10f, 4f)
        lineTo(12f, 4f)
        lineTo(12f, 6.7636719f)
        lineTo(11.894531f, 6.7109375f)
        lineTo(11f, 6.2636719f)
        lineTo(10.105469f, 6.7109375f)
        lineTo(10f, 6.7636719f)
        lineTo(10f, 4f)
        close()
        moveTo(4f, 20f)
        lineTo(4f, 22f)
        lineTo(20f, 22f)
        lineTo(20f, 20f)
        lineTo(4f, 20f)
        close()
      }
    }.build()
    return _collectionOutline!!
  }

