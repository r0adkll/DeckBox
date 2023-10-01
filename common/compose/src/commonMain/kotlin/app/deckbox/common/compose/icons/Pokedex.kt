package app.deckbox.common.compose.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private var _vector: ImageVector? = null

val DeckBoxIcons.Logos.Pokedex: ImageVector
  get() {
    if (_vector != null) {
      return _vector!!
    }
    _vector = ImageVector.Builder(
      name = "Pokedex",
      defaultWidth = 48.dp,
      defaultHeight = 48.dp,
      viewportWidth = 48f,
      viewportHeight = 48f,
    ).apply {
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
        moveTo(33f, 27f)
        lineToRelative(6f, 0.001f)
        curveToRelative(3.314f, 0f, 6f, 2.686f, 6f, 5.999f)
        lineToRelative(0f, 0f)
        curveToRelative(0f, 3.313f, -2.686f, 6f, -6f, 6f)
        horizontalLineToRelative(-6f)
        verticalLineTo(27f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF546E7A)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(39f, 29f)
        arcTo(4f, 4f, 0f, isMoreThanHalf = true, isPositiveArc = false, 39f, 37f)
        arcTo(4f, 4f, 0f, isMoreThanHalf = true, isPositiveArc = false, 39f, 29f)
        close()
        moveTo(11f, 25.001f)
        curveToRelative(-4.418f, 0f, -8f, 3.582f, -8f, 8f)
        lineToRelative(0f, 0f)
        curveToRelative(0f, 4.419f, 3.582f, 8f, 8f, 8f)
        verticalLineTo(25.001f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFDD2C00)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(13f, 23f)
        horizontalLineTo(32f)
        verticalLineTo(25f)
        horizontalLineTo(13f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFF3D00)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(11f, 25.001f)
        verticalLineTo(41f)
        curveToRelative(0f, 0f, 17.334f, 0f, 20f, 0f)
        curveToRelative(3f, 0f, 3f, -3f, 3f, -3f)
        verticalLineTo(25.001f)
        horizontalLineTo(11f)
        close()
        moveTo(31f, 7f)
        curveToRelative(-3f, 0f, -14f, 0f, -17f, 0f)
        reflectiveCurveToRelative(-3f, 3f, -3f, 3f)
        verticalLineToRelative(13f)
        horizontalLineToRelative(23f)
        verticalLineTo(10f)
        curveTo(34f, 10f, 34f, 7f, 31f, 7f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF263238)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(23f, 28f)
        horizontalLineTo(10.976f)
        curveToRelative(-2.717f, 0f, -4.919f, 2.283f, -4.919f, 5f)
        lineToRelative(0f, 0f)
        curveToRelative(0f, 2.717f, 2.202f, 5f, 4.919f, 5f)
        horizontalLineTo(23f)
        verticalLineTo(28f)
        close()
        moveTo(29f, 19f)
        curveToRelative(0f, 0.553f, -0.447f, 1f, -1f, 1f)
        horizontalLineTo(15f)
        curveToRelative(-0.553f, 0f, -1f, -0.447f, -1f, -1f)
        verticalLineToRelative(-8f)
        curveToRelative(0f, -0.553f, 0.447f, -1f, 1f, -1f)
        horizontalLineToRelative(13f)
        curveToRelative(0.553f, 0f, 1f, 0.447f, 1f, 1f)
        verticalLineTo(19f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF90A4AE)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(31f, 38f)
        curveToRelative(0f, 0.552f, -0.447f, 1f, -1f, 1f)
        horizontalLineTo(18f)
        curveToRelative(-0.553f, 0f, -1f, -0.448f, -1f, -1f)
        verticalLineTo(28f)
        curveToRelative(0f, -0.552f, 0.447f, -1f, 1f, -1f)
        horizontalLineToRelative(12f)
        curveToRelative(0.553f, 0f, 1f, 0.448f, 1f, 1f)
        verticalLineTo(38f)
        close()
        moveTo(31f, 20f)
        curveToRelative(0f, 0.552f, -0.447f, 1f, -1f, 1f)
        horizontalLineTo(18f)
        curveToRelative(-0.553f, 0f, -1f, -0.448f, -1f, -1f)
        verticalLineTo(10f)
        curveToRelative(0f, -0.552f, 0.447f, -1f, 1f, -1f)
        horizontalLineToRelative(12f)
        curveToRelative(0.553f, 0f, 1f, 0.448f, 1f, 1f)
        verticalLineTo(20f)
        close()
      }
      group {
        path(
          fill = SolidColor(Color(0xFFBBDEFB)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(19f, 11f)
          horizontalLineTo(29f)
          verticalLineTo(19f)
          horizontalLineTo(19f)
          close()
          moveTo(19f, 29f)
          horizontalLineTo(29f)
          verticalLineTo(37f)
          horizontalLineTo(19f)
          close()
        }
      }
      group {
        path(
          fill = SolidColor(Color(0xFFECEFF1)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(11f, 30f)
          horizontalLineTo(13f)
          verticalLineTo(36f)
          horizontalLineTo(11f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFFECEFF1)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(9f, 32f)
          horizontalLineTo(15f)
          verticalLineTo(34f)
          horizontalLineTo(9f)
          close()
        }
      }
      group {
        path(
          fill = SolidColor(Color(0xFFAEEA00)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(39f, 31f)
          arcTo(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = false, 39f, 35f)
          arcTo(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = false, 39f, 31f)
          close()
          moveTo(3f, 32.994f)
          curveTo(3f, 32.996f, 3f, 32.998f, 3f, 33f)
          curveToRelative(0f, 0.302f, 0.022f, 0.597f, 0.052f, 0.891f)
          curveTo(3.057f, 33.928f, 3.064f, 33.963f, 3.068f, 34f)
          horizontalLineTo(4f)
          curveToRelative(0.553f, 0f, 1f, -0.448f, 1f, -1f)
          reflectiveCurveToRelative(-0.447f, -1f, -1f, -1f)
          horizontalLineTo(3.069f)
          curveToRelative(-0.005f, 0.038f, -0.013f, 0.075f, -0.018f, 0.113f)
          curveTo(3.023f, 32.404f, 3.001f, 32.695f, 3f, 32.994f)
          close()
        }
      }
    }.build()
    return _vector!!
  }
