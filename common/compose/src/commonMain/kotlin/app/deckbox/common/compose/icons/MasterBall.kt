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


private var _masterBall: ImageVector? = null

val DeckBoxIcons.MasterBall: ImageVector
  get() {
    if (_masterBall != null) {
      return _masterBall!!
    }
    _masterBall = ImageVector.Builder(
      name = "MasterBall",
      defaultWidth = 48.dp,
      defaultHeight = 48.dp,
      viewportWidth = 48f,
      viewportHeight = 48f
    ).apply {
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
          curveToRelative(11f, 0f, 20f, -9f, 20f, -20f)
          horizontalLineTo(4f)
          curveTo(4f, 35f, 13f, 44f, 24f, 44f)
          close()
        }
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
          curveToRelative(11f, 0f, 20f, -9f, 20f, -20f)
          curveToRelative(0f, 0f, -0.2f, 16f, -20f, 16f)
          reflectiveCurveTo(4f, 24f, 4f, 24f)
          curveTo(4f, 35f, 13f, 44f, 24f, 44f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFFD500F9)),
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
          curveTo(13f, 4f, 4f, 13f, 4f, 24f)
          horizontalLineToRelative(40f)
          curveTo(44f, 13f, 35f, 4f, 24f, 4f)
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
          curveToRelative(0f, 0.3f, 0f, 0.7f, 0.1f, 1f)
          horizontalLineToRelative(39.9f)
          curveToRelative(0f, -0.3f, 0.1f, -0.7f, 0.1f, -1f)
          reflectiveCurveToRelative(0f, -0.7f, -0.1f, -1f)
          horizontalLineTo(4.1f)
          curveTo(4f, 23.3f, 4f, 23.7f, 4f, 24f)
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
          moveTo(24f, 29f)
          curveToRelative(-2.8f, 0f, -5f, -2.2f, -5f, -5f)
          reflectiveCurveToRelative(2.2f, -5f, 5f, -5f)
          reflectiveCurveToRelative(5f, 2.2f, 5f, 5f)
          reflectiveCurveTo(26.8f, 29f, 24f, 29f)
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
          curveToRelative(2.2f, 0f, 4f, 1.8f, 4f, 4f)
          reflectiveCurveToRelative(-1.8f, 4f, -4f, 4f)
          reflectiveCurveToRelative(-4f, -1.8f, -4f, -4f)
          reflectiveCurveTo(21.8f, 20f, 24f, 20f)
          moveTo(24f, 18f)
          curveToRelative(-3.3f, 0f, -6f, 2.7f, -6f, 6f)
          reflectiveCurveToRelative(2.7f, 6f, 6f, 6f)
          curveToRelative(3.3f, 0f, 6f, -2.7f, 6f, -6f)
          reflectiveCurveTo(27.3f, 18f, 24f, 18f)
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
          curveToRelative(0f, 1.1f, -0.9f, 2f, -2f, 2f)
          reflectiveCurveToRelative(-2f, -0.9f, -2f, -2f)
          reflectiveCurveToRelative(0.9f, -2f, 2f, -2f)
          reflectiveCurveTo(26f, 22.9f, 26f, 24f)
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
          moveTo(24f, 16f)
          curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
          verticalLineToRelative(-3f)
          horizontalLineToRelative(-2f)
          verticalLineToRelative(3f)
          curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
          reflectiveCurveToRelative(-1f, -0.4f, -1f, -1f)
          verticalLineToRelative(-4f)
          curveToRelative(0f, -0.6f, 0.4f, -1f, 1f, -1f)
          horizontalLineToRelative(4f)
          curveToRelative(0.6f, 0f, 1f, 0.4f, 1f, 1f)
          verticalLineToRelative(4f)
          curveTo(25f, 15.6f, 24.6f, 16f, 24f, 16f)
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
          moveTo(28f, 16f)
          curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
          verticalLineToRelative(-3f)
          horizontalLineToRelative(-3f)
          curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
          reflectiveCurveToRelative(0.4f, -1f, 1f, -1f)
          horizontalLineToRelative(4f)
          curveToRelative(0.6f, 0f, 1f, 0.4f, 1f, 1f)
          verticalLineToRelative(4f)
          curveTo(29f, 15.6f, 28.6f, 16f, 28f, 16f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFFEC8FFF)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(42.5f, 16.3f)
          curveToRelative(-2f, -4.8f, -5.9f, -8.7f, -10.7f, -10.8f)
          curveToRelative(-1.4f, 2.4f, -0.7f, 6f, 2f, 8.7f)
          curveTo(36.4f, 16.9f, 40.1f, 17.7f, 42.5f, 16.3f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFFEC8FFF)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(5.5f, 16.3f)
          curveToRelative(2f, -4.8f, 5.9f, -8.7f, 10.7f, -10.8f)
          curveToRelative(1.4f, 2.4f, 0.7f, 6f, -2f, 8.7f)
          curveTo(11.6f, 16.9f, 7.9f, 17.7f, 5.5f, 16.3f)
          close()
        }
      }
    }.build()
    return _masterBall!!
  }

