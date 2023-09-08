package app.deckbox.common.compose.icons

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp


private var _vector: ImageVector? = null

val DeckBoxIcons.Squirtle: ImageVector
  get() {
    if (_vector != null) {
      return _vector!!
    }
    _vector = ImageVector.Builder(
      name = "vector",
      defaultWidth = 48.dp,
      defaultHeight = 48.dp,
      viewportWidth = 48f,
      viewportHeight = 48f
    ).apply {
      path(
        fill = SolidColor(Color(0xFF00BCD4)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(35f, 12f)
        curveToRelative(0f, 2.7f, -1.9f, 5f, -4f, 5f)
        curveToRelative(-3.7f, 0f, -2f, 8f, -6f, 8f)
        curveToRelative(-5.5f, 0f, -10f, -5.9f, -10f, -13f)
        curveToRelative(0f, -5.5f, 4.5f, -10f, 10f, -10f)
        reflectiveCurveTo(35f, 6.5f, 35f, 12f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF0097A7)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(28f, 19f)
        curveToRelative(-3.3f, 0f, -6f, -2.7f, -6f, -6f)
        reflectiveCurveToRelative(2.7f, -6f, 6f, -6f)
        curveToRelative(0.6f, 0f, 1f, 0.4f, 1f, 1f)
        reflectiveCurveToRelative(-0.4f, 1f, -1f, 1f)
        curveToRelative(-2.2f, 0f, -4f, 1.8f, -4f, 4f)
        reflectiveCurveToRelative(1.8f, 4f, 4f, 4f)
        curveToRelative(0.6f, 0f, 1f, 0.4f, 1f, 1f)
        reflectiveCurveTo(28.6f, 19f, 28f, 19f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFB2EBF2)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(24f, 14f)
        curveTo(14f, 14f, 6f, 23.6f, 6f, 31.5f)
        curveToRelative(0f, 8f, 8.1f, 14.5f, 18f, 14.5f)
        reflectiveCurveToRelative(18f, -6.5f, 18f, -14.5f)
        curveTo(42f, 23.6f, 34f, 14f, 24f, 14f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF4DD0E1)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(24f, 18f)
        curveTo(14f, 18f, 6f, 23.6f, 6f, 31.5f)
        curveToRelative(0f, 8f, 8.1f, 14.5f, 18f, 14.5f)
        reflectiveCurveToRelative(18f, -6.5f, 18f, -14.5f)
        curveTo(42f, 23.6f, 34f, 18f, 24f, 18f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF00BCD4)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(24f, 39f)
        curveTo(14f, 39f, 6f, 25.7f, 6f, 31.5f)
        curveToRelative(0f, 8f, 8.1f, 14.5f, 18f, 14.5f)
        reflectiveCurveToRelative(18f, -6.5f, 18f, -14.5f)
        curveTo(42f, 25.5f, 34f, 39f, 24f, 39f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF5D4037)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(19f, 31f)
        curveToRelative(0f, 1.7f, -1.3f, 3f, -3f, 3f)
        curveToRelative(-1.7f, 0f, -3f, -1.3f, -3f, -3f)
        reflectiveCurveToRelative(1.3f, -3f, 3f, -3f)
        curveTo(17.7f, 28f, 19f, 29.3f, 19f, 31f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFFFFFF)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(16f, 30f)
        curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
        curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
        reflectiveCurveToRelative(0.4f, -1f, 1f, -1f)
        curveTo(15.6f, 29f, 16f, 29.4f, 16f, 30f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF5D4037)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(35f, 31f)
        curveToRelative(0f, 1.7f, -1.3f, 3f, -3f, 3f)
        curveToRelative(-1.7f, 0f, -3f, -1.3f, -3f, -3f)
        reflectiveCurveToRelative(1.3f, -3f, 3f, -3f)
        curveTo(33.7f, 28f, 35f, 29.3f, 35f, 31f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFFFFFF)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(32f, 30f)
        curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
        curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
        reflectiveCurveToRelative(0.4f, -1f, 1f, -1f)
        curveTo(31.6f, 29f, 32f, 29.4f, 32f, 30f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF36464E)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(23f, 34f)
        curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
        curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
        reflectiveCurveToRelative(0.4f, -1f, 1f, -1f)
        curveTo(22.6f, 33f, 23f, 33.4f, 23f, 34f)
        close()
        moveTo(27f, 34f)
        curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
        curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
        reflectiveCurveToRelative(0.4f, -1f, 1f, -1f)
        curveTo(26.6f, 33f, 27f, 33.4f, 27f, 34f)
        close()
        moveTo(24f, 41f)
        curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
        curveToRelative(0f, -2.5f, 3.3f, -3f, 6.3f, -3.5f)
        curveToRelative(1.3f, -0.2f, 3.5f, -0.5f, 3.8f, -1f)
        curveToRelative(0.3f, -0.5f, 0.9f, -0.7f, 1.3f, -0.4f)
        curveToRelative(0.5f, 0.3f, 0.7f, 0.9f, 0.4f, 1.4f)
        curveToRelative(-0.7f, 1.3f, -2.8f, 1.7f, -5.3f, 2f)
        curveTo(28f, 38.8f, 25f, 39.2f, 25f, 40f)
        curveTo(25f, 40.6f, 24.6f, 41f, 24f, 41f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF36464E)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(24f, 41f)
        curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
        curveToRelative(0f, -0.8f, -3f, -1.2f, -4.6f, -1.5f)
        curveToRelative(-2.5f, -0.4f, -4.6f, -0.7f, -5.3f, -2f)
        curveToRelative(-0.3f, -0.5f, -0.1f, -1.1f, 0.4f, -1.3f)
        curveToRelative(0.5f, -0.3f, 1.1f, -0.1f, 1.3f, 0.4f)
        curveToRelative(0.3f, 0.4f, 2.5f, 0.8f, 3.8f, 1f)
        curveToRelative(3f, 0.4f, 6.3f, 0.9f, 6.3f, 3.5f)
        curveTo(25f, 40.6f, 24.6f, 41f, 24f, 41f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF00838F)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(28f, 29f)
        curveToRelative(-0.1f, 0f, -0.2f, 0f, -0.3f, 0f)
        curveToRelative(-0.5f, -0.2f, -0.8f, -0.7f, -0.7f, -1.2f)
        curveToRelative(0.3f, -0.9f, 1.4f, -2.7f, 4f, -2.7f)
        curveToRelative(0.6f, 0f, 1f, 0.4f, 1f, 1f)
        reflectiveCurveToRelative(-0.4f, 1f, -1f, 1f)
        curveToRelative(-1.6f, 0f, -2f, 1.2f, -2f, 1.3f)
        curveTo(28.8f, 28.8f, 28.4f, 29f, 28f, 29f)
        close()
        moveTo(20f, 29.1f)
        curveToRelative(-0.4f, 0f, -0.8f, -0.3f, -1f, -0.7f)
        curveToRelative(0f, -0.1f, -0.5f, -1.3f, -2f, -1.3f)
        curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
        reflectiveCurveToRelative(0.4f, -1f, 1f, -1f)
        curveToRelative(2.5f, 0f, 3.7f, 1.8f, 4f, 2.7f)
        curveToRelative(0.2f, 0.5f, -0.2f, 1.1f, -0.7f, 1.2f)
        curveTo(20.2f, 29f, 20.1f, 29.1f, 20f, 29.1f)
        close()
      }
    }.build()
    return _vector!!
  }

