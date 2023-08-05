package app.deckbox.common.compose.icons

import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val DeckBoxIcons.UltraBall: ImageVector
  get() {
    if (_ultraBall != null) {
      return _ultraBall!!
    }
    _ultraBall = ImageVector.Builder(
      name = "UltraBall",
      defaultWidth = 48.dp,
      defaultHeight = 48.dp,
      viewportWidth = 48f,
      viewportHeight = 48f
    ).apply {
      group {
        path(
          fill = SolidColor(Color(0xFF455A64)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(24f, 4f)
          curveTo(12.954f, 4f, 4f, 12.954f, 4f, 24f)
          horizontalLineToRelative(40f)
          curveTo(44f, 12.954f, 35.046f, 4f, 24f, 4f)
          close()
        }
      }
      group {
        path(
          fill = SolidColor(Color(0xFFE4E8EA)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(24f, 44f)
          curveToRelative(11.046f, 0f, 20f, -8.954f, 20f, -20f)
          horizontalLineTo(4f)
          curveTo(4f, 35.046f, 12.954f, 44f, 24f, 44f)
          close()
        }
      }
      group {
        path(
          fill = SolidColor(Color(0xFFCFD8DC)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(24f, 44f)
          curveToRelative(11.046f, 0f, 20f, -8.954f, 20f, -20f)
          curveToRelative(0f, 0f, -0.16f, 16f, -20f, 16f)
          reflectiveCurveTo(4f, 24f, 4f, 24f)
          curveTo(4f, 35.046f, 12.954f, 44f, 24f, 44f)
          close()
        }
      }
      path(
        fill = SolidColor(Color(0xFFFFC400)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(24f, 4f)
        curveToRelative(-4.968f, 0f, -9.503f, 1.822f, -13f, 4.82f)
        verticalLineTo(24f)
        horizontalLineToRelative(5f)
        verticalLineToRelative(-5f)
        curveToRelative(0f, -4.4f, 3.6f, -8f, 8f, -8f)
        horizontalLineToRelative(0f)
        curveToRelative(4.4f, 0f, 8f, 3.6f, 8f, 8f)
        verticalLineToRelative(5f)
        horizontalLineToRelative(5f)
        verticalLineTo(8.82f)
        curveTo(33.503f, 5.822f, 28.968f, 4f, 24f, 4f)
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
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(4f, 24f)
        curveToRelative(0f, 0.338f, 0.034f, 0.667f, 0.05f, 1f)
        horizontalLineTo(43.95f)
        curveToRelative(0.017f, -0.333f, 0.05f, -0.662f, 0.05f, -1f)
        reflectiveCurveToRelative(-0.034f, -0.667f, -0.05f, -1f)
        horizontalLineTo(4.05f)
        curveTo(4.034f, 23.333f, 4f, 23.662f, 4f, 24f)
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
        moveTo(29f, 24f)
        arcTo(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 24f, 29f)
        arcTo(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 19f, 24f)
        arcTo(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 29f, 24f)
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
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(24f, 20f)
        curveToRelative(2.206f, 0f, 4f, 1.794f, 4f, 4f)
        reflectiveCurveToRelative(-1.794f, 4f, -4f, 4f)
        reflectiveCurveToRelative(-4f, -1.794f, -4f, -4f)
        reflectiveCurveTo(21.794f, 20f, 24f, 20f)
        moveTo(24f, 18f)
        curveToRelative(-3.314f, 0f, -6f, 2.686f, -6f, 6f)
        curveToRelative(0f, 3.313f, 2.686f, 6f, 6f, 6f)
        curveToRelative(3.314f, 0f, 6f, -2.688f, 6f, -6f)
        curveTo(30f, 20.686f, 27.314f, 18f, 24f, 18f)
        lineTo(24f, 18f)
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
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(26f, 24f)
        arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 24f, 26f)
        arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22f, 24f)
        arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 26f, 24f)
        close()
      }
    }.build()
    return _ultraBall!!
  }

private var _ultraBall: ImageVector? = null

