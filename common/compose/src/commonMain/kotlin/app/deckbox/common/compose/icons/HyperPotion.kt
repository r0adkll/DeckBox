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

val DeckBoxIcons.Logos.HyperPotion: ImageVector
  get() {
    if (_vector != null) {
      return _vector!!
    }
    _vector = ImageVector.Builder(
      name = "vector",
      defaultWidth = 48.dp,
      defaultHeight = 48.dp,
      viewportWidth = 48f,
      viewportHeight = 48f,
    ).apply {
      path(
        fill = SolidColor(Color(0xFFFF4081)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(11.034f, 44f)
        curveToRelative(-0.519f, 0f, -0.787f, -0.342f, -0.877f, -0.489f)
        reflectiveCurveToRelative(-0.277f, -0.54f, -0.045f, -1.004f)
        lineTo(17f, 28.987f)
        verticalLineTo(13f)
        horizontalLineToRelative(14f)
        verticalLineToRelative(15.987f)
        lineToRelative(6.892f, 13.526f)
        curveToRelative(0.229f, 0.458f, 0.042f, 0.85f, -0.048f, 0.997f)
        reflectiveCurveTo(37.485f, 44f, 36.966f, 44f)
        horizontalLineTo(11.034f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFF80AB)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(30f, 14f)
        verticalLineToRelative(14.747f)
        verticalLineToRelative(0.48f)
        lineToRelative(0.218f, 0.428f)
        lineTo(36.966f, 43f)
        lineToRelative(-25.967f, -0.032f)
        lineToRelative(6.783f, -13.313f)
        lineTo(18f, 29.227f)
        verticalLineToRelative(-0.48f)
        verticalLineTo(14f)
        horizontalLineTo(30f)
        moveTo(32f, 12f)
        horizontalLineTo(16f)
        verticalLineToRelative(16.747f)
        lineTo(9.217f, 42.06f)
        curveTo(8.542f, 43.411f, 9.524f, 45f, 11.034f, 45f)
        horizontalLineToRelative(25.932f)
        curveToRelative(1.51f, 0f, 2.493f, -1.589f, 1.817f, -2.94f)
        lineTo(32f, 28.747f)
        verticalLineTo(12f)
        lineTo(32f, 12f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF90CAF9)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(25f, 22.979f)
        lineToRelative(-2f, 0f)
        curveToRelative(-1.1f, 0f, -2f, -0.9f, -2f, -2f)
        verticalLineTo(14f)
        lineToRelative(-5f, 0f)
        verticalLineTo(5f)
        curveToRelative(0f, -1.1f, 0.9f, -2f, 2f, -2f)
        horizontalLineToRelative(12f)
        curveToRelative(1.1f, 0f, 2f, 0.9f, 2f, 2f)
        verticalLineToRelative(9f)
        lineToRelative(-5f, 0f)
        lineToRelative(0f, 6.979f)
        curveTo(27f, 22.079f, 26.1f, 22.979f, 25f, 22.979f)
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
        moveTo(24f, 6f)
        arcTo(3f, 3f, 0f, isMoreThanHalf = true, isPositiveArc = false, 24f, 12f)
        arcTo(3f, 3f, 0f, isMoreThanHalf = true, isPositiveArc = false, 24f, 6f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF37474F)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(24f, 8f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 24f, 10f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 24f, 8f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFF9FC4)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(27f, 30f)
        arcTo(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = false, 27f, 34f)
        arcTo(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = false, 27f, 30f)
        close()
        moveTo(22.5f, 35f)
        arcTo(1.5f, 1.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 22.5f, 38f)
        arcTo(1.5f, 1.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 22.5f, 35f)
        close()
        moveTo(20f, 40f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 20f, 42f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 20f, 40f)
        close()
        moveTo(28f, 39f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 28f, 41f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 28f, 39f)
        close()
        moveTo(20f, 31f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 20f, 33f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 20f, 31f)
        close()
      }
    }.build()
    return _vector!!
  }
