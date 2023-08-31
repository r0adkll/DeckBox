package app.deckbox.common.compose.icons.rounded

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp


private var _pokeball: ImageVector? = null

val Icons.Rounded.Pokeball: ImageVector
  get() {
    if (_pokeball != null) {
      return _pokeball!!
    }
    _pokeball = ImageVector.Builder(
      name = "Rounded.Pokeball",
      defaultWidth = 24.dp,
      defaultHeight = 24.dp,
      viewportWidth = 24f,
      viewportHeight = 24f
    ).apply {
      path(
        fill = SolidColor(Color.Black),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
      ) {
        moveTo(12f, 2f)
        curveTo(7.253f, 2f, 3.2551f, 5.3391f, 2.2461f, 9.7871f)
        curveTo(2.1051f, 10.4081f, 2.5935f, 11f, 3.2305f, 11f)
        lineTo(8.5546875f, 11f)
        curveTo(8.9107f, 11f, 9.2351f, 10.808f, 9.4141f, 10.5f)
        curveTo(9.9351f, 9.606f, 10.895f, 9f, 12f, 9f)
        curveTo(13.105f, 9f, 14.0649f, 9.606f, 14.5859f, 10.5f)
        curveTo(14.7649f, 10.808f, 15.0893f, 11f, 15.4453f, 11f)
        lineTo(20.769531f, 11f)
        curveTo(21.4075f, 11f, 21.8949f, 10.4081f, 21.7539f, 9.7871f)
        curveTo(20.7449f, 5.3391f, 16.747f, 2f, 12f, 2f)
        close()
        moveTo(12f, 11f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 11f, 12f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 13f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 13f, 12f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 11f)
        close()
        moveTo(3.2304688f, 13f)
        curveTo(2.5925f, 13f, 2.1051f, 13.5919f, 2.2461f, 14.2129f)
        curveTo(3.2551f, 18.6609f, 7.253f, 22f, 12f, 22f)
        curveTo(16.747f, 22f, 20.7439f, 18.6609f, 21.7539f, 14.2129f)
        curveTo(21.8949f, 13.5919f, 21.4065f, 13f, 20.7695f, 13f)
        lineTo(15.445312f, 13f)
        curveTo(15.0893f, 13f, 14.7649f, 13.192f, 14.5859f, 13.5f)
        curveTo(14.0649f, 14.394f, 13.105f, 15f, 12f, 15f)
        curveTo(10.895f, 15f, 9.9351f, 14.394f, 9.4141f, 13.5f)
        curveTo(9.2351f, 13.192f, 8.9107f, 13f, 8.5547f, 13f)
        lineTo(3.2304688f, 13f)
        close()
      }
    }.build()
    return _pokeball!!
  }

