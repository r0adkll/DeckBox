package app.deckbox.common.compose.icons.rounded

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp


private var _removeCard: ImageVector? = null

val Icons.Rounded.RemoveCard: ImageVector
  get() {
    if (_removeCard != null) {
      return _removeCard!!
    }
    _removeCard = ImageVector.Builder(
      name = "Icons.RemoveCard",
      defaultWidth = 24.dp,
      defaultHeight = 24.dp,
      viewportWidth = 24f,
      viewportHeight = 24f
    ).apply {
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
        moveTo(15f, 3f)
        curveTo(16.1046f, 3f, 17f, 3.8954f, 17f, 5f)
        lineTo(17.0008f, 13.5908f)
        curveTo(16.6763f, 13.5312f, 16.3418f, 13.5f, 16f, 13.5f)
        curveTo(15.6586f, 13.5f, 15.3244f, 13.5311f, 15.0002f, 13.5907f)
        lineTo(15f, 5f)
        horizontalLineTo(7f)
        verticalLineTo(18f)
        lineTo(10.5908f, 17.9992f)
        curveTo(10.5312f, 18.3237f, 10.5f, 18.6582f, 10.5f, 19f)
        curveTo(10.5f, 19.3418f, 10.5312f, 19.6763f, 10.5908f, 20.0008f)
        lineTo(7f, 20f)
        curveTo(5.8954f, 20f, 5f, 19.1046f, 5f, 18f)
        verticalLineTo(5f)
        curveTo(5f, 3.8954f, 5.8954f, 3f, 7f, 3f)
        horizontalLineTo(15f)
        close()
      }
      path(
        fill = null,
        fillAlpha = 1.0f,
        stroke = SolidColor(Color(0xFF000000)),
        strokeAlpha = 1.0f,
        strokeLineWidth = 2f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(13.878f, 21.1198f)
        lineTo(18.1177f, 16.8802f)
        moveTo(13.8765f, 16.8787f)
        lineTo(18.1192f, 21.1213f)
      }
    }.build()
    return _removeCard!!
  }

