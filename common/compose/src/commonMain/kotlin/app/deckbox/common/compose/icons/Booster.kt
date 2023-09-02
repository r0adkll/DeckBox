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

val DeckBoxIcons.Logos.Booster: ImageVector
  get() {
    if (_vector != null) {
      return _vector!!
    }
    _vector = ImageVector.Builder(
      name = "vector",
      defaultWidth = 48.dp,
      defaultHeight = 48.dp,
      viewportWidth = 48f,
      viewportHeight = 48f
    ).apply {
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
        moveTo(8.444f, 32.9f)
        curveToRelative(1.562f, -1.563f, 4.095f, -1.563f, 5.657f, 0f)
        curveToRelative(1.562f, 1.562f, 1.562f, 4.095f, 0f, 5.656f)
        curveToRelative(-1.562f, 1.563f, -7.778f, 2.121f, -7.778f, 2.121f)
        reflectiveCurveTo(6.882f, 34.462f, 8.444f, 32.9f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFFEB3B)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(9.858f, 34.314f)
        curveToRelative(0.781f, -0.781f, 2.048f, -0.781f, 2.829f, 0f)
        reflectiveCurveToRelative(0.781f, 2.047f, 0f, 2.828f)
        reflectiveCurveTo(9.151f, 37.85f, 9.151f, 37.85f)
        reflectiveCurveTo(9.078f, 35.096f, 9.858f, 34.314f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFD1C4E9)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(29.658f, 8.858f)
        curveToRelative(-2.121f, 2.12f, -20.507f, 20.507f, -20.507f, 20.507f)
        lineToRelative(8.485f, 8.484f)
        curveToRelative(0f, 0f, 18.385f, -18.384f, 20.506f, -20.506f)
        curveToRelative(1.118f, -1.118f, 2.036f, -4.391f, 2.658f, -7.24f)
        lineToRelative(-3.902f, -3.902f)
        curveTo(34.047f, 6.823f, 30.775f, 7.741f, 29.658f, 8.858f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF7E57C2)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(21.172f, 41.385f)
        lineTo(19.05f, 36.437f)
        lineTo(27.535f, 27.951f)
        lineTo(26.122f, 36.436f)
        close()
        moveTo(5.616f, 25.829f)
        lineTo(10.565f, 27.951f)
        lineTo(19.05f, 19.466f)
        lineTo(10.565f, 20.879f)
        close()
        moveTo(36.898f, 6.2f)
        lineToRelative(3.902f, 3.902f)
        curveToRelative(0.559f, -2.56f, 0.877f, -4.78f, 0.877f, -4.78f)
        reflectiveCurveTo(39.459f, 5.643f, 36.898f, 6.2f)
        close()
        moveTo(17.637f, 37.85f)
        lineTo(14.808f, 37.85f)
        lineTo(9.151f, 32.193f)
        lineTo(9.151f, 29.365f)
        close()
      }
      group {
        path(
          fill = SolidColor(Color(0xFF311B92)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero
        ) {
          moveTo(29.657f, 15.344000000000001f)
          arcTo(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = false, 29.657f, 19.344f)
          arcTo(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = false, 29.657f, 15.344000000000001f)
          close()
          moveTo(24.707f, 20.293f)
          arcTo(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = false, 24.707f, 24.293f)
          arcTo(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = false, 24.707f, 20.293f)
          close()
          moveTo(19.758f, 25.243f)
          arcTo(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = false, 19.758f, 29.243f)
          arcTo(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = false, 19.758f, 25.243f)
          close()
        }
      }
    }.build()
    return _vector!!
  }

