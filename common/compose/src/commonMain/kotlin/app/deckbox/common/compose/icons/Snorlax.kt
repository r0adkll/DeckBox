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

private var _snorlax: ImageVector? = null

val DeckBoxIcons.Snorlax: ImageVector
  get() {
    if (_snorlax != null) {
      return _snorlax!!
    }
    _snorlax = ImageVector.Builder(
      name = "Snorlax",
      defaultWidth = 48.dp,
      defaultHeight = 48.dp,
      viewportWidth = 48f,
      viewportHeight = 48f,
    ).apply {
      group {
        path(
          fill = SolidColor(Color(0xFF1565C0)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(42.9f, 6f)
          curveToRelative(-4.6f, -0.4f, -9.3f, 2.3f, -11.3f, 6.3f)
          curveToRelative(-2.1f, 4.1f, -1.7f, 8.9f, 1f, 12.6f)
          curveToRelative(0.1f, 0.1f, 0.2f, 0.2f, 0.4f, 0.2f)
          curveToRelative(0f, 0f, 0.1f, 0f, 0.1f, 0f)
          curveToRelative(4.4f, 0f, 8.5f, -2.5f, 10.5f, -6.5f)
          curveTo(45.6f, 14.6f, 45.5f, 9.8f, 42.9f, 6f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFF2196F3)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(42.9f, 6f)
          curveToRelative(-4.6f, -0.4f, -9.3f, 2.3f, -11.3f, 6.3f)
          lineToRelative(6.3f, 5.7f)
          curveTo(39.9f, 14f, 46.3f, 10.2f, 42.9f, 6f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFF1565C0)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(5.1f, 6f)
          curveToRelative(4.6f, -0.4f, 9.3f, 2.3f, 11.3f, 6.3f)
          curveToRelative(2.1f, 4.1f, 1.7f, 8.9f, -1f, 12.6f)
          curveToRelative(-0.1f, 0.1f, -0.2f, 0.2f, -0.4f, 0.2f)
          curveToRelative(0f, 0f, -0.1f, 0f, -0.1f, 0f)
          curveToRelative(-4.4f, 0f, -8.5f, -2.5f, -10.5f, -6.5f)
          curveTo(2.4f, 14.6f, 2.5f, 9.8f, 5.1f, 6f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFF2196F3)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(5.1f, 6f)
          curveToRelative(4.6f, -0.4f, 9.3f, 2.3f, 11.3f, 6.3f)
          lineTo(10.1f, 18f)
          curveTo(8.1f, 14f, 1.7f, 10.2f, 5.1f, 6f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFF2196F3)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(24f, 5f)
          curveTo(11.8f, 5f, 2f, 16.4f, 2f, 25.8f)
          curveTo(2f, 35.3f, 11.9f, 43f, 24f, 43f)
          reflectiveCurveToRelative(22f, -7.7f, 22f, -17.2f)
          curveTo(46f, 16.4f, 36.2f, 5f, 24f, 5f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFFFFCC8F)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(30.2f, 15f)
          curveToRelative(-0.2f, 0f, -0.3f, 0f, -0.4f, 0.1f)
          lineTo(24f, 20.6f)
          lineToRelative(-5.8f, -5.5f)
          curveTo(18.1f, 15f, 18f, 15f, 17.8f, 15f)
          curveTo(8.9f, 16.3f, 4f, 20.4f, 4f, 26.4f)
          curveTo(4f, 34.4f, 13f, 41f, 24f, 41f)
          reflectiveCurveToRelative(20f, -6.6f, 20f, -14.6f)
          curveTo(44f, 20.4f, 39.1f, 16.3f, 30.2f, 15f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFFFFE0B2)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(30.2f, 18f)
          curveToRelative(-0.2f, 0f, -0.3f, 0f, -0.4f, 0.1f)
          lineTo(24f, 23.6f)
          lineToRelative(-5.8f, -5.5f)
          curveTo(18.1f, 18f, 18f, 18f, 17.8f, 18f)
          curveToRelative(-8.2f, 1.2f, -12.9f, 4.7f, -13.7f, 9.9f)
          curveTo(5.2f, 35.3f, 13.7f, 41f, 24f, 41f)
          reflectiveCurveToRelative(18.8f, -5.7f, 19.9f, -13.1f)
          curveTo(43.1f, 22.7f, 38.4f, 19.2f, 30.2f, 18f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFF1976D2)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(30.1f, 15f)
          curveTo(30.1f, 15f, 30.2f, 15f, 30.1f, 15f)
          curveToRelative(9f, 1.3f, 13.9f, 5.4f, 13.9f, 11.4f)
          curveTo(44f, 34.4f, 35f, 41f, 24f, 41f)
          reflectiveCurveTo(4f, 34.4f, 4f, 26.4f)
          curveToRelative(0f, -6f, 4.9f, -10.1f, 13.8f, -11.4f)
          curveToRelative(0f, 0f, 0.1f, 0f, 0.1f, 0f)
          curveToRelative(0.1f, 0f, 0.3f, 0.1f, 0.4f, 0.1f)
          lineToRelative(5.8f, 5.5f)
          lineToRelative(5.8f, -5.5f)
          curveTo(29.9f, 15.1f, 30f, 15f, 30.1f, 15f)
          moveTo(30.1f, 13f)
          curveToRelative(-0.7f, 0f, -1.3f, 0.2f, -1.7f, 0.7f)
          lineTo(24f, 17.9f)
          lineToRelative(-4.4f, -4.2f)
          curveToRelative(-0.5f, -0.4f, -1.1f, -0.7f, -1.7f, -0.7f)
          curveToRelative(-0.1f, 0f, -0.2f, 0f, -0.4f, 0f)
          curveTo(7.7f, 14.5f, 2f, 19.3f, 2f, 26.4f)
          curveTo(2f, 35.5f, 11.9f, 43f, 24f, 43f)
          reflectiveCurveToRelative(22f, -7.5f, 22f, -16.6f)
          curveToRelative(0f, -7f, -5.7f, -11.9f, -15.5f, -13.4f)
          curveTo(30.4f, 13f, 30.2f, 13f, 30.1f, 13f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFF8D6E63)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(29f, 27f)
          curveToRelative(-0.4f, 0f, -0.8f, -0.2f, -0.9f, -0.6f)
          curveToRelative(-0.2f, -0.5f, 0f, -1.1f, 0.6f, -1.3f)
          curveToRelative(0.1f, 0f, 2.7f, -1.1f, 7.4f, -1.1f)
          curveToRelative(0.6f, 0f, 1f, 0.4f, 1f, 1f)
          curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
          curveToRelative(-4.2f, 0f, -6.6f, 0.9f, -6.6f, 0.9f)
          curveTo(29.2f, 27f, 29.1f, 27f, 29f, 27f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFF8D6E63)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(19f, 27f)
          curveToRelative(-0.1f, 0f, -0.2f, 0f, -0.4f, -0.1f)
          curveToRelative(0f, 0f, -2.4f, -0.9f, -6.6f, -0.9f)
          curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
          curveToRelative(0f, -0.6f, 0.4f, -1f, 1f, -1f)
          curveToRelative(4.6f, 0f, 7.3f, 1f, 7.4f, 1.1f)
          curveToRelative(0.5f, 0.2f, 0.8f, 0.8f, 0.6f, 1.3f)
          curveTo(19.8f, 26.8f, 19.4f, 27f, 19f, 27f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFF8D6E63)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(15f, 33f)
          lineToRelative(2f, -4f)
          lineToRelative(2f, 4f)
          curveTo(19f, 33f, 17.8f, 34.4f, 15f, 33f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFF8D6E63)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(33f, 33f)
          lineToRelative(-2f, -4f)
          lineToRelative(-2f, 4f)
          curveTo(29f, 33f, 30.2f, 34.4f, 33f, 33f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFF8D6E63)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(24f, 35f)
          curveToRelative(-4.9f, 0f, -8.1f, -1f, -8.3f, -1f)
          curveToRelative(-0.5f, -0.2f, -0.8f, -0.7f, -0.7f, -1.2f)
          reflectiveCurveToRelative(0.7f, -0.8f, 1.2f, -0.7f)
          curveToRelative(0f, 0f, 3.2f, 0.9f, 7.7f, 0.9f)
          curveToRelative(4.6f, 0f, 7.7f, -0.9f, 7.7f, -0.9f)
          curveToRelative(0.5f, -0.2f, 1.1f, 0.1f, 1.2f, 0.7f)
          curveToRelative(0.2f, 0.5f, -0.1f, 1.1f, -0.7f, 1.2f)
          curveTo(32.1f, 34f, 28.9f, 35f, 24f, 35f)
          close()
        }
      }
    }.build()
    return _snorlax!!
  }
