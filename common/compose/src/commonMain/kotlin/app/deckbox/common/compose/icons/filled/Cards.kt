package app.deckbox.common.compose.icons.filled

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private var _cards: ImageVector? = null

val Icons.Filled.Cards: ImageVector
  get() {
    if (_cards != null) {
      return _cards!!
    }
    _cards = ImageVector.Builder(
      name = "Filled.Cards",
      defaultWidth = 24.dp,
      defaultHeight = 24.dp,
      viewportWidth = 24f,
      viewportHeight = 24f,
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
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(21.6504f, 4.1f)
        lineTo(20.3104f, 3.54f)
        verticalLineTo(12.57f)
        lineTo(22.7404f, 6.71f)
        curveTo(23.1504f, 5.69f, 22.6804f, 4.52f, 21.6504f, 4.1f)
        close()
        moveTo(2.15043f, 7.8f)
        lineTo(7.11043f, 19.75f)
        curveTo(7.4204f, 20.52f, 8.1504f, 20.99f, 8.9204f, 21.01f)
        curveTo(9.1804f, 21.01f, 9.4504f, 20.96f, 9.7104f, 20.85f)
        lineTo(17.0804f, 17.8f)
        curveTo(17.8304f, 17.49f, 18.2904f, 16.75f, 18.3104f, 16.01f)
        curveTo(18.3204f, 15.75f, 18.2704f, 15.46f, 18.1804f, 15.2f)
        lineTo(13.1804f, 3.25f)
        curveTo(12.8904f, 2.48f, 12.1504f, 2.01f, 11.3704f, 2f)
        curveTo(11.1104f, 2f, 10.8504f, 2.06f, 10.6004f, 2.15f)
        lineTo(3.24043f, 5.2f)
        curveTo(2.2204f, 5.62f, 1.7304f, 6.79f, 2.1504f, 7.8f)
        close()
        moveTo(18.3004f, 4f)
        curveTo(18.3004f, 2.8954f, 17.405f, 2f, 16.3004f, 2f)
        horizontalLineTo(14.8504f)
        lineTo(18.3004f, 10.34f)
      }
    }.build()
    return _cards!!
  }
