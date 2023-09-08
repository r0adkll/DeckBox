package app.deckbox.common.compose.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private var _vector: ImageVector? = null

val DeckBoxIcons.Bulbasaur: ImageVector
  get() {
    if (_vector != null) {
      return _vector!!
    }
    _vector = ImageVector.Builder(
      name = "Bulbasaur",
      defaultWidth = 48.dp,
      defaultHeight = 48.dp,
      viewportWidth = 48f,
      viewportHeight = 48f,
    ).apply {
      path(
        fill = SolidColor(Color(0xFF00C853)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(40.4f, 15.4f)
        curveTo(32.3f, 7.1f, 28.3f, 3f, 24f, 3f)
        reflectiveCurveTo(15.7f, 7.1f, 7.7f, 15.4f)
        curveTo(4.5f, 18.6f, 3f, 21.7f, 3f, 24.8f)
        curveToRelative(0f, 3.1f, 1.5f, 6.2f, 4.7f, 9.4f)
        curveTo(10.6f, 37.2f, 16.8f, 39f, 24f, 39f)
        reflectiveCurveToRelative(13.5f, -1.8f, 16.3f, -4.8f)
        curveToRelative(3.1f, -3.2f, 4.7f, -6.3f, 4.7f, -9.4f)
        reflectiveCurveTo(43.5f, 18.6f, 40.4f, 15.4f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF388E3C)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(40.3f, 17f)
        curveTo(32.3f, 8.8f, 28.2f, 5f, 24f, 5f)
        curveToRelative(-4.3f, 0f, -8.3f, 3.8f, -16.3f, 12f)
        curveTo(4.5f, 20.2f, 3f, 21.7f, 3f, 24.8f)
        reflectiveCurveToRelative(1.5f, 6.2f, 4.7f, 9.4f)
        curveTo(10.5f, 37.2f, 16.8f, 39f, 24f, 39f)
        reflectiveCurveToRelative(13.5f, -1.8f, 16.3f, -4.8f)
        curveToRelative(3.1f, -3.2f, 4.7f, -6.3f, 4.7f, -9.4f)
        reflectiveCurveTo(43.5f, 20.2f, 40.3f, 17f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF1B5E20)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(24f, 32f)
        curveTo(9.9f, 32f, 4.7f, 26.5f, 3f, 24.8f)
        curveToRelative(0f, 3.1f, 1.5f, 6.2f, 4.7f, 9.4f)
        curveTo(10.6f, 37.2f, 16.8f, 39f, 24f, 39f)
        reflectiveCurveToRelative(13.5f, -1.8f, 16.3f, -4.8f)
        curveToRelative(3.1f, -3.2f, 4.7f, -6.3f, 4.7f, -9.4f)
        curveTo(43.2f, 26.8f, 38.1f, 32f, 24f, 32f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF00C853)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(31.9f, 15.5f)
        curveTo(28f, 6.9f, 26.2f, 3f, 24f, 3f)
        reflectiveCurveToRelative(-4f, 3.9f, -7.8f, 12.5f)
        curveToRelative(-2.9f, 6.3f, -2.9f, 12.2f, 0f, 18.6f)
        curveToRelative(1.4f, 3f, 4.4f, 4.9f, 7.8f, 4.9f)
        reflectiveCurveToRelative(6.5f, -1.9f, 7.9f, -4.9f)
        curveTo(34.7f, 27.8f, 34.7f, 21.9f, 31.9f, 15.5f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF388E3C)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(31.1f, 17.5f)
        curveTo(27.6f, 8.9f, 26f, 5f, 24f, 5f)
        curveToRelative(-2f, 0f, -3.6f, 3.9f, -7.1f, 12.5f)
        curveToRelative(-2.6f, 6.3f, -2.6f, 12.2f, 0f, 18.6f)
        curveToRelative(1.2f, 3f, 3.9f, 4.9f, 7.1f, 4.9f)
        curveToRelative(3.1f, 0f, 5.8f, -1.9f, 7.1f, -4.9f)
        curveTo(33.7f, 29.8f, 33.7f, 23.9f, 31.1f, 17.5f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF69F0AE)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(24f, 17f)
        curveToRelative(-9.4f, 0f, -17f, 8.4f, -17f, 15.3f)
        curveToRelative(0f, 7f, 7.6f, 12.7f, 17f, 12.7f)
        reflectiveCurveToRelative(17f, -5.7f, 17f, -12.7f)
        curveTo(41f, 25.4f, 33.4f, 17f, 24f, 17f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF69F0AE)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(37.3f, 17.7f)
        curveToRelative(0f, -0.1f, -0.1f, -0.1f, -0.2f, -0.1f)
        curveToRelative(-2.3f, -1.1f, -4.6f, -0.7f, -5.6f, 0.9f)
        curveToRelative(-0.5f, 0.7f, -0.7f, 1.7f, -0.5f, 2.7f)
        curveToRelative(0f, 0.3f, 4.8f, 3.8f, 5.1f, 3.8f)
        curveToRelative(0f, 0f, 0.1f, 0f, 0.1f, 0f)
        curveToRelative(1f, -0.2f, 1.7f, -0.7f, 2.2f, -1.4f)
        curveTo(39.5f, 22f, 39f, 19.6f, 37.3f, 17.7f)
        close()
        moveTo(10.7f, 17.7f)
        curveToRelative(0f, -0.1f, 0.1f, -0.1f, 0.2f, -0.1f)
        curveToRelative(2.3f, -1.1f, 4.6f, -0.7f, 5.6f, 0.9f)
        curveToRelative(0.5f, 0.7f, 0.7f, 1.7f, 0.5f, 2.7f)
        curveToRelative(0f, 0.3f, -4.8f, 3.8f, -5.1f, 3.8f)
        curveToRelative(0f, 0f, -0.1f, 0f, -0.1f, 0f)
        curveToRelative(-1f, -0.2f, -1.7f, -0.7f, -2.2f, -1.4f)
        curveTo(8.5f, 22f, 9f, 19.6f, 10.7f, 17.7f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF00E676)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(41f, 32.6f)
        curveTo(41f, 39.4f, 33.4f, 45f, 24f, 45f)
        reflectiveCurveTo(7f, 39.4f, 7f, 32.6f)
        curveTo(7f, 25.7f, 14.6f, 21f, 24f, 21f)
        reflectiveCurveTo(41f, 25.7f, 41f, 32.6f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF66BB6A)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(41f, 32.1f)
        curveTo(41f, 39f, 34.3f, 45f, 24f, 45f)
        curveTo(13.6f, 45f, 7f, 39f, 7f, 32.1f)
        curveToRelative(0f, -4.4f, 2.2f, 5.8f, 17f, 5.8f)
        reflectiveCurveTo(41f, 27.6f, 41f, 32.1f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFF44336)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(29f, 38.1f)
        curveToRelative(0f, 1.4f, -2.2f, 2.9f, -5f, 2.9f)
        reflectiveCurveToRelative(-5f, -1.6f, -5f, -3f)
        verticalLineToRelative(-1f)
        horizontalLineToRelative(10f)
        verticalLineTo(38.1f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF66BB6A)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(22.5f, 26f)
        curveToRelative(-1.4f, 0f, -2.5f, -1f, -2.5f, -2.3f)
        reflectiveCurveToRelative(1.7f, -1.7f, 3.1f, -1.7f)
        reflectiveCurveToRelative(1.9f, 0.5f, 1.9f, 1.7f)
        reflectiveCurveTo(23.9f, 26f, 22.5f, 26f)
        close()
        moveTo(28f, 25f)
        curveToRelative(0f, -0.6f, -0.4f, -1f, -1f, -1f)
        reflectiveCurveToRelative(-1f, 0.4f, -1f, 1f)
        reflectiveCurveToRelative(0.4f, 1f, 1f, 1f)
        reflectiveCurveTo(28f, 25.6f, 28f, 25f)
        close()
        moveTo(31f, 23.5f)
        curveToRelative(0f, 0.3f, -0.2f, 0.5f, -0.5f, 0.5f)
        curveToRelative(-0.3f, 0f, -0.5f, -0.2f, -0.5f, -0.5f)
        reflectiveCurveToRelative(0.2f, -0.5f, 0.5f, -0.5f)
        curveTo(30.8f, 23f, 31f, 23.2f, 31f, 23.5f)
        close()
        moveTo(26f, 28.5f)
        curveToRelative(0f, 0.3f, -0.2f, 0.5f, -0.5f, 0.5f)
        curveToRelative(-0.3f, 0f, -0.5f, -0.2f, -0.5f, -0.5f)
        reflectiveCurveToRelative(0.2f, -0.5f, 0.5f, -0.5f)
        curveTo(25.8f, 28f, 26f, 28.2f, 26f, 28.5f)
        close()
        moveTo(19f, 25f)
        curveToRelative(0f, -0.6f, -0.4f, -1f, -1f, -1f)
        reflectiveCurveToRelative(-1f, 0.4f, -1f, 1f)
        reflectiveCurveToRelative(0.4f, 1f, 1f, 1f)
        reflectiveCurveTo(19f, 25.6f, 19f, 25f)
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
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(19f, 31f)
        curveToRelative(0f, 1.7f, -1.3f, 3f, -3f, 3f)
        reflectiveCurveToRelative(-3f, -1.3f, -3f, -3f)
        reflectiveCurveToRelative(1.3f, -3f, 3f, -3f)
        reflectiveCurveTo(19f, 29.3f, 19f, 31f)
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
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(16f, 30f)
        curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
        reflectiveCurveToRelative(-1f, -0.4f, -1f, -1f)
        reflectiveCurveToRelative(0.4f, -1f, 1f, -1f)
        reflectiveCurveTo(16f, 29.4f, 16f, 30f)
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
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(35f, 31f)
        curveToRelative(0f, 1.7f, -1.3f, 3f, -3f, 3f)
        reflectiveCurveToRelative(-3f, -1.3f, -3f, -3f)
        reflectiveCurveToRelative(1.3f, -3f, 3f, -3f)
        reflectiveCurveTo(35f, 29.3f, 35f, 31f)
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
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(32f, 30f)
        curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
        reflectiveCurveToRelative(-1f, -0.4f, -1f, -1f)
        reflectiveCurveToRelative(0.4f, -1f, 1f, -1f)
        reflectiveCurveTo(32f, 29.4f, 32f, 30f)
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
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(23f, 34f)
        curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
        reflectiveCurveToRelative(-1f, -0.4f, -1f, -1f)
        reflectiveCurveToRelative(0.4f, -1f, 1f, -1f)
        reflectiveCurveTo(23f, 33.4f, 23f, 34f)
        close()
        moveTo(27f, 34f)
        curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
        reflectiveCurveToRelative(-1f, -0.4f, -1f, -1f)
        reflectiveCurveToRelative(0.4f, -1f, 1f, -1f)
        reflectiveCurveTo(27f, 33.4f, 27f, 34f)
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
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(15f, 37f)
        verticalLineToRelative(3f)
        lineToRelative(2f, -2f)
        lineTo(15f, 37f)
        close()
        moveTo(33f, 37f)
        verticalLineToRelative(3f)
        lineToRelative(-2f, -2f)
        lineTo(33f, 37f)
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
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(24f, 38.9f)
        curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
        curveToRelative(0f, -0.4f, 0.2f, -0.8f, 0.5f, -1.1f)
        curveTo(24.3f, 36f, 26f, 36f, 28.6f, 36f)
        curveToRelative(1.4f, 0f, 4.1f, 0f, 4.5f, -0.4f)
        curveToRelative(0.3f, -0.5f, 0.9f, -0.7f, 1.3f, -0.4f)
        curveToRelative(0.5f, 0.3f, 0.7f, 0.9f, 0.4f, 1.4f)
        curveToRelative(-0.8f, 1.5f, -3.3f, 1.5f, -6.3f, 1.5f)
        curveToRelative(-1.1f, 0f, -3f, 0f, -3.6f, 0.2f)
        curveTo(24.9f, 38.6f, 24.5f, 38.9f, 24f, 38.9f)
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
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(24f, 38.9f)
        curveToRelative(-0.4f, 0f, -0.8f, -0.3f, -1f, -0.7f)
        curveTo(22.5f, 38f, 20.6f, 38f, 19.4f, 38f)
        curveToRelative(-2.9f, 0f, -5.4f, 0.1f, -6.3f, -1.5f)
        curveToRelative(-0.3f, -0.5f, -0.1f, -1.1f, 0.4f, -1.3f)
        curveToRelative(0.5f, -0.3f, 1.1f, -0.1f, 1.3f, 0.4f)
        curveToRelative(0.4f, 0.5f, 3f, 0.4f, 4.5f, 0.4f)
        curveToRelative(3f, 0f, 5.5f, -0.1f, 5.7f, 1.9f)
        curveTo(25.1f, 38.4f, 24.6f, 38.9f, 24f, 38.9f)
        curveTo(24.1f, 38.9f, 24f, 38.9f, 24f, 38.9f)
        close()
        moveTo(14.9f, 35.6f)
        lineTo(14.9f, 35.6f)
        lineTo(14.9f, 35.6f)
        close()
      }
    }.build()
    return _vector!!
  }
