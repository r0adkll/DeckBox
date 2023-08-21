package app.deckbox.common.compose.icons.rounded

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private var _vector: ImageVector? = null

val Icons.Rounded.Experiment: ImageVector
  get() {
    if (_vector != null) {
      return _vector!!
    }
    _vector = ImageVector.Builder(
      name = "Rounded.Experiment",
      defaultWidth = 24.dp,
      defaultHeight = 24.dp,
      viewportWidth = 24f,
      viewportHeight = 24f,
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
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(16f, 2f)
        horizontalLineTo(8f)
        curveTo(7.448f, 2f, 7f, 2.448f, 7f, 3f)
        curveToRelative(0f, 0.552f, 0.448f, 1f, 1f, 1f)
        verticalLineToRelative(12.829f)
        curveToRelative(0f, 2.089f, 1.529f, 3.955f, 3.609f, 4.152f)
        curveTo(13.995f, 21.208f, 16f, 19.339f, 16f, 17f)
        verticalLineTo(4f)
        curveToRelative(0.552f, 0f, 1f, -0.448f, 1f, -1f)
        curveTo(17f, 2.448f, 16.552f, 2f, 16f, 2f)
        close()
        moveTo(11f, 17f)
        curveToRelative(-0.55f, 0f, -1f, -0.45f, -1f, -1f)
        reflectiveCurveToRelative(0.45f, -1f, 1f, -1f)
        reflectiveCurveToRelative(1f, 0.45f, 1f, 1f)
        reflectiveCurveTo(11.55f, 17f, 11f, 17f)
        close()
        moveTo(13f, 13f)
        curveToRelative(-0.55f, 0f, -1f, -0.45f, -1f, -1f)
        reflectiveCurveToRelative(0.45f, -1f, 1f, -1f)
        reflectiveCurveToRelative(1f, 0.45f, 1f, 1f)
        reflectiveCurveTo(13.55f, 13f, 13f, 13f)
        close()
        moveTo(14f, 8f)
        horizontalLineToRelative(-4f)
        verticalLineTo(4f)
        horizontalLineToRelative(4f)
        verticalLineTo(8f)
        close()
      }
    }.build()
    return _vector!!
  }
