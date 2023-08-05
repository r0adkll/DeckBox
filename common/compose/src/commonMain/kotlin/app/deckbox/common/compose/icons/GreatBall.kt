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


private var _greatBall: ImageVector? = null

public val DeckBoxIcons.GreatBall: ImageVector
  get() {
    if (_greatBall != null) {
      return _greatBall!!
    }
    _greatBall = ImageVector.Builder(
      name = "GreatBall",
      defaultWidth = 48.dp,
      defaultHeight = 48.dp,
      viewportWidth = 48f,
      viewportHeight = 48f
    ).apply {
      group {
        path(
          fill = SolidColor(Color(0xFF2196F3)),
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
      group {
        path(
          fill = SolidColor(Color(0xFFFF3D00)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(39.973f, 12f)
          horizontalLineTo(31f)
          verticalLineToRelative(5f)
          horizontalLineToRelative(11.716f)
          curveTo(42.04f, 15.194f, 41.113f, 13.515f, 39.973f, 12f)
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
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(5.284f, 17f)
          horizontalLineTo(17f)
          verticalLineToRelative(-5f)
          horizontalLineTo(8.027f)
          curveTo(6.887f, 13.515f, 5.96f, 15.194f, 5.284f, 17f)
          close()
        }
      }
    }.build()
    return _greatBall!!
  }

