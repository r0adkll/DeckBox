import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.DeckBoxIcons


private var _psyduck: ImageVector? = null

public val DeckBoxIcons.Psyduck: ImageVector
  get() {
    if (_psyduck != null) {
      return _psyduck!!
    }
    _psyduck = ImageVector.Builder(
      name = "Psyduck",
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
          moveTo(24f, 11.9f)
          curveToRelative(-0.3f, 0f, -0.6f, -0.1f, -0.8f, -0.4f)
          lineToRelative(-5f, -5.9f)
          curveToRelative(-0.4f, -0.4f, -0.3f, -1.1f, 0.1f, -1.4f)
          curveToRelative(0.4f, -0.4f, 1.1f, -0.3f, 1.4f, 0.1f)
          lineTo(23f, 8.2f)
          verticalLineTo(3f)
          curveToRelative(0f, -0.6f, 0.4f, -1f, 1f, -1f)
          reflectiveCurveToRelative(1f, 0.4f, 1f, 1f)
          verticalLineToRelative(7.9f)
          curveToRelative(0f, 0.4f, -0.3f, 0.8f, -0.7f, 0.9f)
          curveTo(24.2f, 11.9f, 24.1f, 11.9f, 24f, 11.9f)
          close()
        }
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
          moveTo(24f, 11.9f)
          curveToRelative(-0.2f, 0f, -0.5f, -0.1f, -0.6f, -0.2f)
          curveToRelative(-0.4f, -0.4f, -0.5f, -1f, -0.1f, -1.4f)
          lineToRelative(5f, -5.9f)
          curveToRelative(0.4f, -0.4f, 1f, -0.5f, 1.4f, -0.1f)
          curveToRelative(0.4f, 0.4f, 0.5f, 1f, 0.1f, 1.4f)
          lineToRelative(-5f, 5.9f)
          curveTo(24.6f, 11.8f, 24.3f, 11.9f, 24f, 11.9f)
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
          moveTo(24f, 10f)
          curveTo(14f, 10f, 6f, 19f, 6f, 26.4f)
          curveTo(6f, 33.9f, 14.1f, 40f, 24f, 40f)
          reflectiveCurveToRelative(18f, -6.1f, 18f, -13.6f)
          curveTo(42f, 19f, 34f, 10f, 24f, 10f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFFFFC107)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(24f, 13f)
          curveTo(14f, 13f, 6f, 19f, 6f, 26.4f)
          curveTo(6f, 33.9f, 14.1f, 40f, 24f, 40f)
          reflectiveCurveToRelative(18f, -6.1f, 18f, -13.6f)
          curveTo(42f, 19f, 34f, 13f, 24f, 13f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFFFFA726)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(6.1f, 28f)
          curveToRelative(0f, 0.3f, 0.1f, 0.6f, 0.2f, 0.8f)
          curveToRelative(0f, 0.1f, 0.1f, 0.3f, 0.1f, 0.4f)
          curveToRelative(0.1f, 0.3f, 0.2f, 0.7f, 0.3f, 1f)
          curveToRelative(0.1f, 0.2f, 0.2f, 0.4f, 0.3f, 0.7f)
          curveToRelative(0.1f, 0.1f, 0.1f, 0.2f, 0.2f, 0.3f)
          curveToRelative(0.4f, 0.8f, 0.9f, 1.6f, 1.5f, 2.3f)
          curveTo(11.8f, 37.4f, 17.5f, 40f, 24f, 40f)
          reflectiveCurveToRelative(12.2f, -2.6f, 15.3f, -6.5f)
          curveToRelative(0.6f, -0.7f, 1.1f, -1.5f, 1.5f, -2.3f)
          curveToRelative(0.1f, -0.1f, 0.1f, -0.2f, 0.2f, -0.3f)
          curveToRelative(0.1f, -0.2f, 0.2f, -0.4f, 0.3f, -0.6f)
          curveToRelative(0.1f, -0.3f, 0.2f, -0.6f, 0.3f, -1f)
          curveToRelative(0f, -0.1f, 0.1f, -0.3f, 0.1f, -0.4f)
          curveToRelative(0.1f, -0.3f, 0.1f, -0.6f, 0.2f, -0.8f)
          curveToRelative(0f, -0.1f, 0f, -0.3f, 0.1f, -0.4f)
          curveToRelative(0f, -0.4f, 0.1f, -0.8f, 0.1f, -1.2f)
          curveToRelative(-3.2f, 4.2f, -4.1f, 2.1f, -4.1f, 2.1f)
          curveToRelative(-0.8f, -2.9f, -3.6f, -3.6f, -5.6f, -4f)
          curveToRelative(-1.7f, -0.4f, -2.8f, -0.7f, -3.3f, -1.6f)
          curveToRelative(-0.7f, -1.3f, -2f, -3.6f, -4.9f, -3.8f)
          lineTo(24f, 19f)
          lineToRelative(-0.2f, 0f)
          curveToRelative(-2.9f, 0.3f, -4.1f, 2.5f, -4.9f, 3.8f)
          curveToRelative(-0.5f, 0.8f, -1.6f, 1.2f, -3.3f, 1.6f)
          curveToRelative(-2f, 0.5f, -4.8f, 1.1f, -5.6f, 4f)
          curveTo(9.9f, 29f, 8.4f, 30f, 6f, 26.4f)
          curveToRelative(0f, 0.4f, 0f, 0.8f, 0.1f, 1.2f)
          curveTo(6.1f, 27.7f, 6.1f, 27.9f, 6.1f, 28f)
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
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(36f, 29f)
          curveToRelative(-1f, -3.5f, -6.8f, -1.8f, -8.7f, -5.2f)
          curveToRelative(-0.7f, -1.2f, -1.5f, -2.6f, -3.3f, -2.8f)
          curveToRelative(-1.8f, 0.2f, -2.6f, 1.6f, -3.3f, 2.8f)
          curveTo(18.8f, 27.2f, 13f, 25.5f, 12f, 29f)
          reflectiveCurveToRelative(-2f, 6.2f, -2f, 10f)
          curveToRelative(0f, 7f, 8.2f, 7f, 14f, 7f)
          reflectiveCurveToRelative(14f, 0f, 14f, -7f)
          curveTo(38f, 35.2f, 37f, 32.5f, 36f, 29f)
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
          moveTo(19f, 21f)
          curveToRelative(0f, 1.7f, -1.3f, 3f, -3f, 3f)
          curveToRelative(-1.7f, 0f, -3f, -1.3f, -3f, -3f)
          reflectiveCurveToRelative(1.3f, -3f, 3f, -3f)
          curveTo(17.6f, 18f, 19f, 19.3f, 19f, 21f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFF445963)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(16f, 20f)
          curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
          curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
          reflectiveCurveToRelative(0.4f, -1f, 1f, -1f)
          curveTo(15.5f, 19f, 16f, 19.4f, 16f, 20f)
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
          moveTo(35f, 21f)
          curveToRelative(0f, 1.7f, -1.3f, 3f, -3f, 3f)
          curveToRelative(-1.7f, 0f, -3f, -1.3f, -3f, -3f)
          reflectiveCurveToRelative(1.3f, -3f, 3f, -3f)
          curveTo(33.6f, 18f, 35f, 19.3f, 35f, 21f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFF445963)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(32f, 20f)
          curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
          curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
          reflectiveCurveToRelative(0.4f, -1f, 1f, -1f)
          curveTo(31.5f, 19f, 32f, 19.4f, 32f, 20f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFF795548)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(26f, 29f)
          curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
          verticalLineToRelative(-1f)
          curveToRelative(0f, -0.6f, 0.4f, -1f, 1f, -1f)
          reflectiveCurveToRelative(1f, 0.4f, 1f, 1f)
          verticalLineToRelative(1f)
          curveTo(27f, 28.6f, 26.6f, 29f, 26f, 29f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFF795548)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(22f, 29f)
          curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
          verticalLineToRelative(-1f)
          curveToRelative(0f, -0.6f, 0.4f, -1f, 1f, -1f)
          reflectiveCurveToRelative(1f, 0.4f, 1f, 1f)
          verticalLineToRelative(1f)
          curveTo(23f, 28.6f, 22.6f, 29f, 22f, 29f)
          close()
        }
      }
    }.build()
    return _psyduck!!
  }

