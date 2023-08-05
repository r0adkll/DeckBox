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


private var _pokemonEgg: ImageVector? = null

public val DeckBoxIcons.PokemonEgg: ImageVector
  get() {
    if (_pokemonEgg != null) {
      return _pokemonEgg!!
    }
    _pokemonEgg = ImageVector.Builder(
      name = "PokemonEgg",
      defaultWidth = 48.dp,
      defaultHeight = 48.dp,
      viewportWidth = 48f,
      viewportHeight = 48f
    ).apply {
      group {
        path(
          fill = SolidColor(Color(0xFFFFF9C4)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(24f, 45f)
          curveToRelative(-6.1f, 0f, -11f, -5f, -11f, -11.2f)
          curveToRelative(0f, -3.2f, 1.3f, -7.7f, 3.3f, -11.3f)
          curveToRelative(2.3f, -4.2f, 5f, -6.5f, 7.7f, -6.5f)
          reflectiveCurveToRelative(5.4f, 2.3f, 7.7f, 6.5f)
          curveToRelative(2f, 3.6f, 3.3f, 8.1f, 3.3f, 11.3f)
          curveTo(35f, 40f, 30.1f, 45f, 24f, 45f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFFF4E88C)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(24f, 17f)
          curveToRelative(2.2f, 0f, 4.7f, 2.2f, 6.8f, 6f)
          curveToRelative(1.9f, 3.4f, 3.2f, 7.8f, 3.2f, 10.9f)
          curveTo(34f, 39.4f, 29.5f, 44f, 24f, 44f)
          reflectiveCurveToRelative(-10f, -4.6f, -10f, -10.2f)
          curveToRelative(0f, -3f, 1.3f, -7.4f, 3.2f, -10.9f)
          curveTo(19.3f, 19.2f, 21.8f, 17f, 24f, 17f)
          moveTo(24f, 15f)
          curveToRelative(-3f, 0f, -6.1f, 2.5f, -8.6f, 7f)
          curveToRelative(-2.1f, 3.8f, -3.4f, 8.4f, -3.4f, 11.8f)
          curveTo(12f, 40.5f, 17.4f, 46f, 24f, 46f)
          reflectiveCurveToRelative(12f, -5.5f, 12f, -12.2f)
          curveToRelative(0f, -3.4f, -1.3f, -8f, -3.4f, -11.8f)
          curveTo(30.1f, 17.5f, 27f, 15f, 24f, 15f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFFF4E88C)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(32.6f, 22f)
          curveToRelative(-2.5f, -4.5f, -5.5f, -7f, -8.6f, -7f)
          verticalLineToRelative(31f)
          curveToRelative(6.6f, 0f, 12f, -5.5f, 12f, -12.2f)
          curveTo(36f, 30.4f, 34.7f, 25.8f, 32.6f, 22f)
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
          moveTo(18f, 11f)
          horizontalLineToRelative(-4f)
          curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
          verticalLineTo(3f)
          curveToRelative(0f, -0.6f, 0.4f, -1f, 1f, -1f)
          horizontalLineToRelative(4f)
          curveToRelative(0.6f, 0f, 1f, 0.4f, 1f, 1f)
          verticalLineToRelative(7f)
          curveTo(19f, 10.6f, 18.6f, 11f, 18f, 11f)
          close()
          moveTo(15f, 9f)
          horizontalLineToRelative(2f)
          verticalLineTo(4f)
          horizontalLineToRelative(-2f)
          verticalLineTo(9f)
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
          moveTo(32f, 8f)
          curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
          verticalLineTo(6f)
          curveToRelative(0f, -0.6f, 0.4f, -1f, 1f, -1f)
          horizontalLineToRelative(1f)
          verticalLineTo(4f)
          horizontalLineToRelative(-2f)
          curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
          reflectiveCurveToRelative(-1f, -0.4f, -1f, -1f)
          verticalLineTo(3f)
          curveToRelative(0f, -0.6f, 0.4f, -1f, 1f, -1f)
          horizontalLineToRelative(4f)
          curveToRelative(0.6f, 0f, 1f, 0.4f, 1f, 1f)
          verticalLineToRelative(3f)
          curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
          horizontalLineToRelative(-1f)
          curveTo(33f, 7.6f, 32.6f, 8f, 32f, 8f)
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
          moveTo(22f, 11f)
          curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
          verticalLineTo(3f)
          curveToRelative(0f, -0.6f, 0.4f, -1f, 1f, -1f)
          reflectiveCurveToRelative(1f, 0.4f, 1f, 1f)
          verticalLineToRelative(7f)
          curveTo(23f, 10.6f, 22.6f, 11f, 22f, 11f)
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
          moveTo(26f, 11f)
          curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
          verticalLineTo(3f)
          curveToRelative(0f, -0.6f, 0.4f, -1f, 1f, -1f)
          reflectiveCurveToRelative(1f, 0.4f, 1f, 1f)
          verticalLineToRelative(7f)
          curveTo(27f, 10.6f, 26.6f, 11f, 26f, 11f)
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
          moveTo(26f, 8f)
          horizontalLineToRelative(-4f)
          curveToRelative(-0.6f, 0f, -1f, -0.4f, -1f, -1f)
          reflectiveCurveToRelative(0.4f, -1f, 1f, -1f)
          horizontalLineToRelative(4f)
          curveToRelative(0.6f, 0f, 1f, 0.4f, 1f, 1f)
          reflectiveCurveTo(26.6f, 8f, 26f, 8f)
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
          moveTo(33f, 10f)
          curveToRelative(0f, 0.6f, -0.4f, 1f, -1f, 1f)
          reflectiveCurveToRelative(-1f, -0.4f, -1f, -1f)
          reflectiveCurveToRelative(0.4f, -1f, 1f, -1f)
          reflectiveCurveTo(33f, 9.4f, 33f, 10f)
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
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(35.6f, 30.2f)
          lineToRelative(-0.9f, -0.9f)
          curveToRelative(-0.4f, -0.4f, -1f, -0.4f, -1.4f, 0f)
          lineTo(32f, 30.6f)
          lineToRelative(-1.3f, -1.3f)
          curveToRelative(-0.4f, -0.4f, -1f, -0.4f, -1.4f, 0f)
          lineTo(28f, 30.6f)
          lineToRelative(-1.3f, -1.3f)
          curveToRelative(-0.4f, -0.4f, -1f, -0.4f, -1.4f, 0f)
          lineTo(24f, 30.6f)
          lineToRelative(-1.3f, -1.3f)
          curveTo(22.5f, 29.1f, 22.3f, 29f, 22f, 29f)
          curveToRelative(-0.3f, 0f, -0.5f, 0.1f, -0.7f, 0.3f)
          lineToRelative(-2f, 2.1f)
          curveToRelative(-0.4f, 0.4f, -0.4f, 1f, 0f, 1.4f)
          curveToRelative(0.2f, 0.2f, 0.4f, 0.3f, 0.7f, 0.3f)
          curveToRelative(0.3f, 0f, 0.5f, -0.1f, 0.7f, -0.3f)
          lineToRelative(1.3f, -1.4f)
          lineToRelative(1.3f, 1.3f)
          curveToRelative(0.4f, 0.4f, 1f, 0.4f, 1.4f, 0f)
          lineToRelative(1.3f, -1.3f)
          lineToRelative(1.3f, 1.3f)
          curveToRelative(0.4f, 0.4f, 1f, 0.4f, 1.4f, 0f)
          lineToRelative(1.3f, -1.3f)
          lineToRelative(1.3f, 1.3f)
          curveToRelative(0.4f, 0.4f, 1f, 0.4f, 1.4f, 0f)
          lineToRelative(1.3f, -1.3f)
          lineToRelative(1.3f, 1.3f)
          curveToRelative(0.2f, 0.2f, 0.4f, 0.3f, 0.7f, 0.3f)
          curveTo(35.9f, 32.1f, 35.8f, 31.1f, 35.6f, 30.2f)
          close()
        }
      }
    }.build()
    return _pokemonEgg!!
  }

