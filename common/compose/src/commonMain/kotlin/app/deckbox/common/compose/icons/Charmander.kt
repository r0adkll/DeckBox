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

val DeckBoxIcons.Charmander: ImageVector
  get() {
    if (_vector != null) {
      return _vector!!
    }
    _vector = ImageVector.Builder(
      name = "Charmander",
      defaultWidth = 48.dp,
      defaultHeight = 48.dp,
      viewportWidth = 48f,
      viewportHeight = 48f
    ).apply {
      path(
        fill = SolidColor(Color(0xFFFFD600)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(17.3f, 6.1f)
        curveTo(19.2f, 3.9f, 22.9f, 4f, 22.9f, 4f)
        reflectiveCurveToRelative(0.6f, 3.7f, -1.3f, 5.9f)
        curveToRelative(-1.9f, 2.2f, -5.5f, 2.1f, -5.5f, 2.1f)
        reflectiveCurveTo(15.5f, 8.3f, 17.3f, 6.1f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFF6D00)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(17.1f, 7.7f)
        curveTo(18.5f, 6f, 21f, 6f, 21f, 6f)
        reflectiveCurveToRelative(0.3f, 2.7f, -1.1f, 4.3f)
        curveToRelative(-1.4f, 1.7f, -3.9f, 1.7f, -3.9f, 1.7f)
        reflectiveCurveTo(15.7f, 9.3f, 17.1f, 7.7f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFE65100)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(21f, 6f)
        curveToRelative(0f, 0f, 0.3f, 2.7f, -1.1f, 4.3f)
        curveToRelative(-1.4f, 1.7f, -3.9f, 1.7f, -3.9f, 1.7f)
        lineTo(21f, 6f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFE65100)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(29.5f, 28.3f)
        curveToRelative(-0.1f, -0.2f, -0.4f, -0.3f, -0.6f, -0.2f)
        curveToRelative(-1.3f, 0.4f, -2.6f, 0.5f, -4f, 0.4f)
        curveToRelative(-2.9f, -0.2f, -5.5f, -1.5f, -7.3f, -3.7f)
        reflectiveCurveToRelative(-2.8f, -5f, -2.5f, -7.8f)
        curveToRelative(0.1f, -1.9f, 0.7f, -3.6f, 1.8f, -5.2f)
        curveToRelative(0.1f, -0.2f, 0.1f, -0.4f, 0f, -0.6f)
        curveTo(16.7f, 11f, 16.4f, 11f, 16.2f, 11f)
        curveToRelative(-4.6f, 1.5f, -7.8f, 5.5f, -8.2f, 10.3f)
        curveTo(7.5f, 27.8f, 12.4f, 33.5f, 18.9f, 34f)
        curveToRelative(0.3f, 0f, 0.6f, 0f, 0.9f, 0f)
        curveToRelative(3.8f, 0f, 7.5f, -1.9f, 9.7f, -5.1f)
        curveTo(29.6f, 28.7f, 29.6f, 28.5f, 29.5f, 28.3f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFF57C00)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(16.1f, 12f)
        curveToRelative(-3.2f, 1.9f, -3.9f, 4.9f, -4f, 6.9f)
        curveToRelative(-0.5f, 6.2f, 4.1f, 11.6f, 10.3f, 12.1f)
        curveToRelative(2.5f, 0.2f, 4.8f, -0.4f, 6.8f, -1.6f)
        curveToRelative(-1.3f, 0.4f, -2.7f, 0.6f, -4.1f, 0.4f)
        curveTo(18.9f, 29.3f, 14.5f, 24.2f, 15f, 18f)
        curveToRelative(0.2f, -2f, 0.6f, -4.3f, 1.7f, -5.8f)
        lineTo(16.1f, 12f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFFB300)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(23f, 16f)
        curveTo(13f, 16f, 5f, 23.6f, 5f, 31f)
        curveToRelative(0f, 7.5f, 8.1f, 14f, 18f, 14f)
        reflectiveCurveToRelative(18f, -6.5f, 18f, -14f)
        curveTo(41f, 23.6f, 33f, 16f, 23f, 16f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFF57C00)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(41f, 32.1f)
        curveTo(41f, 39.2f, 32.9f, 45f, 23f, 45f)
        reflectiveCurveTo(5f, 39.2f, 5f, 32.1f)
        curveTo(5f, 24.9f, 13.1f, 20f, 23f, 20f)
        reflectiveCurveTo(41f, 24.9f, 41f, 32.1f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFFB300)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(27f, 30.5f)
        curveToRelative(0f, 0.4f, -1.8f, 0.5f, -4f, 0.5f)
        reflectiveCurveToRelative(-4f, -0.1f, -4f, -0.5f)
        reflectiveCurveToRelative(1.8f, -1.5f, 4f, -1.5f)
        reflectiveCurveTo(27f, 30.1f, 27f, 30.5f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFE65100)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(41f, 32.1f)
        curveTo(41f, 39.2f, 32.4f, 45f, 23f, 45f)
        curveToRelative(-9.4f, 0f, -18f, -5.8f, -18f, -12.9f)
        curveToRelative(0f, -4.7f, 2.1f, -0.2f, 8f, 2.9f)
        curveToRelative(0f, 2.3f, 5.7f, 2f, 10f, 2f)
        curveToRelative(4.3f, 0f, 10f, 0.1f, 10f, -2f)
        curveTo(38.9f, 31.9f, 41f, 27.3f, 41f, 32.1f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFFD600)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(31f, 10.5f)
        curveToRelative(0f, -3f, 3f, -5.5f, 3f, -5.5f)
        reflectiveCurveToRelative(3f, 2.5f, 3f, 5.5f)
        reflectiveCurveTo(34f, 16f, 34f, 16f)
        reflectiveCurveTo(31f, 13.5f, 31f, 10.5f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFF6D00)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(32f, 12f)
        curveToRelative(0f, -2.2f, 2f, -4f, 2f, -4f)
        reflectiveCurveToRelative(2f, 1.8f, 2f, 4f)
        reflectiveCurveToRelative(-2f, 4f, -2f, 4f)
        reflectiveCurveTo(32f, 14.2f, 32f, 12f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFE65100)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(34f, 8f)
        curveToRelative(0f, 0f, 2f, 1.8f, 2f, 4f)
        reflectiveCurveToRelative(-2f, 4f, -2f, 4f)
        verticalLineTo(8f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFFD600)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(39f, 18.5f)
        curveToRelative(0f, -3f, 3f, -5.5f, 3f, -5.5f)
        reflectiveCurveToRelative(3f, 2.5f, 3f, 5.5f)
        reflectiveCurveTo(42f, 24f, 42f, 24f)
        reflectiveCurveTo(39f, 21.5f, 39f, 18.5f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFF6D00)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(40f, 20f)
        curveToRelative(0f, -2.2f, 2f, -4f, 2f, -4f)
        reflectiveCurveToRelative(2f, 1.8f, 2f, 4f)
        reflectiveCurveToRelative(-2f, 4f, -2f, 4f)
        reflectiveCurveTo(40f, 22.2f, 40f, 20f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFE65100)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(42f, 16f)
        curveToRelative(0f, 0f, 2f, 1.8f, 2f, 4f)
        reflectiveCurveToRelative(-2f, 4f, -2f, 4f)
        verticalLineTo(16f)
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
        moveTo(14f, 37f)
        verticalLineToRelative(3f)
        lineToRelative(2f, -2f)
        lineTo(14f, 37f)
        close()
        moveTo(32f, 37f)
        verticalLineToRelative(3f)
        lineToRelative(-2f, -2f)
        lineTo(32f, 37f)
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
        moveTo(23f, 38.9f)
        curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
        curveToRelative(0f, -0.4f, 0.2f, -0.8f, 0.5f, -1.1f)
        curveToRelative(0.8f, -0.8f, 2.4f, -0.8f, 5f, -0.8f)
        curveToRelative(1.3f, 0f, 4.3f, 0.1f, 4.6f, -0.5f)
        curveToRelative(0.3f, -0.5f, 0.9f, -0.7f, 1.4f, -0.4f)
        curveToRelative(0.5f, 0.3f, 0.7f, 0.8f, 0.4f, 1.3f)
        curveToRelative(-0.8f, 1.7f, -3.6f, 1.6f, -6.5f, 1.6f)
        curveToRelative(-1.2f, 0f, -2.9f, -0.1f, -3.4f, 0.2f)
        curveTo(23.8f, 38.6f, 23.5f, 38.9f, 23f, 38.9f)
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
        moveTo(23f, 38.9f)
        curveToRelative(-0.4f, 0f, -0.8f, -0.3f, -1f, -0.7f)
        curveTo(21.5f, 38f, 19.7f, 38f, 18.6f, 38f)
        curveToRelative(-2.9f, 0.1f, -5.6f, 0.1f, -6.4f, -1.6f)
        curveToRelative(-0.3f, -0.5f, -0.1f, -1.1f, 0.4f, -1.3f)
        curveToRelative(0.5f, -0.3f, 1.1f, -0.1f, 1.3f, 0.4f)
        curveToRelative(0.3f, 0.6f, 3.3f, 0.5f, 4.6f, 0.5f)
        curveToRelative(2.9f, -0.1f, 5.3f, -0.1f, 5.5f, 1.8f)
        curveTo(24f, 38.4f, 23.6f, 38.9f, 23f, 38.9f)
        curveTo(23f, 38.9f, 23f, 38.9f, 23f, 38.9f)
        close()
        moveTo(22f, 33f)
        curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
        reflectiveCurveToRelative(-1f, -0.4f, -1f, -1f)
        reflectiveCurveToRelative(0.4f, -1f, 1f, -1f)
        reflectiveCurveTo(22f, 32.4f, 22f, 33f)
        close()
        moveTo(26f, 33f)
        curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
        reflectiveCurveToRelative(-1f, -0.4f, -1f, -1f)
        reflectiveCurveToRelative(0.4f, -1f, 1f, -1f)
        reflectiveCurveTo(26f, 32.4f, 26f, 33f)
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
        moveTo(18f, 28f)
        curveToRelative(0f, 1.7f, -1.3f, 3f, -3f, 3f)
        reflectiveCurveToRelative(-3f, -1.3f, -3f, -3f)
        reflectiveCurveToRelative(1.3f, -3f, 3f, -3f)
        reflectiveCurveTo(18f, 26.3f, 18f, 28f)
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
        moveTo(15f, 27f)
        curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
        reflectiveCurveToRelative(-1f, -0.4f, -1f, -1f)
        reflectiveCurveToRelative(0.4f, -1f, 1f, -1f)
        reflectiveCurveTo(15f, 26.4f, 15f, 27f)
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
        moveTo(34f, 28f)
        curveToRelative(0f, 1.7f, -1.3f, 3f, -3f, 3f)
        reflectiveCurveToRelative(-3f, -1.3f, -3f, -3f)
        reflectiveCurveToRelative(1.3f, -3f, 3f, -3f)
        reflectiveCurveTo(34f, 26.3f, 34f, 28f)
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
        moveTo(31f, 27f)
        curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
        reflectiveCurveToRelative(-1f, -0.4f, -1f, -1f)
        reflectiveCurveToRelative(0.4f, -1f, 1f, -1f)
        reflectiveCurveTo(31f, 26.4f, 31f, 27f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFFD600)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(13.1f, 7f)
        curveToRelative(0f, -2.8f, 3f, -5f, 3f, -5f)
        reflectiveCurveToRelative(3f, 2.2f, 3f, 5f)
        curveToRelative(0f, 2.8f, -3f, 5f, -3f, 5f)
        reflectiveCurveTo(13.1f, 9.7f, 13.1f, 7f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFF6D00)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(14.1f, 8.5f)
        curveToRelative(0f, -1.9f, 2f, -3.5f, 2f, -3.5f)
        reflectiveCurveToRelative(2f, 1.6f, 2f, 3.5f)
        reflectiveCurveToRelative(-2f, 3.5f, -2f, 3.5f)
        reflectiveCurveTo(14.1f, 10.4f, 14.1f, 8.5f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFE65100)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(16.1f, 5f)
        curveToRelative(0f, 0f, 2f, 1.6f, 2f, 3.5f)
        reflectiveCurveToRelative(-2f, 3.5f, -2f, 3.5f)
        verticalLineTo(5f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFFD600)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(21.1f, 9f)
        curveToRelative(2.8f, 0f, 5f, 3f, 5f, 3f)
        reflectiveCurveToRelative(-2.2f, 3f, -5f, 3f)
        reflectiveCurveToRelative(-5f, -3f, -5f, -3f)
        reflectiveCurveTo(18.3f, 9f, 21.1f, 9f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFF6D00)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(19.6f, 10f)
        curveToRelative(1.9f, 0f, 3.5f, 2f, 3.5f, 2f)
        reflectiveCurveToRelative(-1.6f, 2f, -3.5f, 2f)
        reflectiveCurveToRelative(-3.5f, -2f, -3.5f, -2f)
        reflectiveCurveTo(17.6f, 10f, 19.6f, 10f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFE65100)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(23.1f, 12f)
        curveToRelative(0f, 0f, -1.6f, 2f, -3.5f, 2f)
        reflectiveCurveToRelative(-3.5f, -2f, -3.5f, -2f)
        horizontalLineTo(23.1f)
        close()
      }
    }.build()
    return _vector!!
  }

