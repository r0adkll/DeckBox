package app.deckbox.common.compose.icons.filled

import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.DeckBoxIcons


private var _collectionFilled: ImageVector? = null

val DeckBoxIcons.Filled.Collection: ImageVector
  get() {
    if (_collectionFilled != null) {
      return _collectionFilled!!
    }
    _collectionFilled = ImageVector.Builder(
      name = "Filled.Collection",
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
        curveTo(4.89f, 2f, 4f, 2.89f, 4f, 4f)
        lineTo(4f, 16f)
        curveTo(4f, 17.1f, 4.89f, 18f, 6f, 18f)
        lineTo(18f, 18f)
        curveTo(19.1f, 18f, 20f, 17.1f, 20f, 16f)
        lineTo(20f, 4f)
        curveTo(20f, 2.89f, 19.1f, 2f, 18f, 2f)
        lineTo(6f, 2f)
        close()
        moveTo(8f, 4f)
        lineTo(13f, 4f)
        lineTo(13f, 10f)
        lineTo(10.5f, 8.5f)
        lineTo(8f, 10f)
        lineTo(8f, 4f)
        close()
        moveTo(4f, 20f)
        lineTo(4f, 22f)
        lineTo(20f, 22f)
        lineTo(20f, 20f)
        lineTo(4f, 20f)
        close()
      }
    }.build()
    return _collectionFilled!!
  }

