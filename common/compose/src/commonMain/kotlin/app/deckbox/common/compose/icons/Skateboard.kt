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

val DeckBoxIcons.Logos.Skateboard: ImageVector
  get() {
    if (_vector != null) {
      return _vector!!
    }
    _vector = ImageVector.Builder(
      name = "Skateboard",
      defaultWidth = 48.dp,
      defaultHeight = 48.dp,
      viewportWidth = 48f,
      viewportHeight = 48f,
    ).apply {
      path(
        fill = SolidColor(Color(0xFFFF5722)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(39.365f, 17.681f)
        curveToRelative(0f, 0f, 3.208f, 3.224f, 4.138f, 4.153f)
        reflectiveCurveToRelative(0.41f, 2.956f, -1.161f, 4.527f)
        curveToRelative(-1.571f, 1.571f, -3.597f, 2.09f, -4.527f, 1.161f)
        curveToRelative(-0.929f, -0.929f, -4.794f, -4.903f, -4.794f, -4.903f)
      }
      path(
        fill = SolidColor(Color(0xFFBF360C)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(40.545f, 22.62f)
        arcTo(0.948f, 1.897f, 0f, isMoreThanHalf = true, isPositiveArc = false, 40.545f, 26.413999999999998f)
        arcTo(0.948f, 1.897f, 0f, isMoreThanHalf = true, isPositiveArc = false, 40.545f, 22.62f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFF5722)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(21.386f, 35.686f)
        curveToRelative(0f, 0f, 3.208f, 3.224f, 4.138f, 4.153f)
        reflectiveCurveToRelative(0.41f, 2.956f, -1.161f, 4.527f)
        curveToRelative(-1.571f, 1.571f, -3.597f, 2.09f, -4.527f, 1.161f)
        reflectiveCurveToRelative(-4.794f, -4.903f, -4.794f, -4.903f)
      }
      path(
        fill = SolidColor(Color(0xFFFFCC80)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(11.644f, 45.96f)
        lineTo(11.644f, 45.96f)
        curveToRelative(-2.573f, -0.001f, -5.084f, -1.049f, -6.89f, -2.876f)
        curveToRelative(-1.249f, -1.266f, -1.738f, -2.521f, -1.728f, -3.839f)
        curveToRelative(0.02f, -2.332f, 2.378f, -3.882f, 4.468f, -5.929f)
        curveToRelative(0.366f, -0.358f, 0.739f, -0.725f, 1.111f, -1.101f)
        lineTo(29.257f, 11.31f)
        curveToRelative(1.271f, -1.286f, 2.328f, -2.688f, 3.351f, -4.042f)
        curveToRelative(2.256f, -2.99f, 4.005f, -6.196f, 7.166f, -5.018f)
        curveToRelative(2.255f, 0.841f, 4.236f, 3.643f, 4.922f, 6.75f)
        curveTo(44.851f, 9.703f, 45f, 10.614f, 45f, 11.667f)
        curveToRelative(0f, 2.317f, -0.721f, 5.324f, -3.682f, 8.322f)
        lineTo(17.902f, 43.406f)
        curveTo(16.275f, 45.053f, 14.052f, 45.96f, 11.644f, 45.96f)
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
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(3.106f, 40.153f)
        curveToRelative(0.021f, 0.123f, 0.054f, 0.245f, 0.086f, 0.367f)
        curveToRelative(0.052f, 0.194f, 0.115f, 0.388f, 0.195f, 0.581f)
        curveToRelative(0.021f, 0.052f, 0.044f, 0.105f, 0.068f, 0.157f)
        curveToRelative(0.09f, 0.197f, 0.198f, 0.394f, 0.319f, 0.59f)
        curveToRelative(0.077f, 0.123f, 0.152f, 0.245f, 0.243f, 0.367f)
        curveToRelative(0.032f, 0.045f, 0.07f, 0.09f, 0.104f, 0.135f)
        curveToRelative(1.702f, 1.349f, 3.839f, 2.112f, 6.025f, 2.113f)
        horizontalLineToRelative(0.001f)
        curveToRelative(2.408f, 0f, 4.631f, -0.907f, 6.258f, -2.554f)
        lineToRelative(23.417f, -23.417f)
        curveToRelative(4.307f, -4.361f, 3.874f, -8.741f, 3.378f, -10.989f)
        curveToRelative(-0.329f, -1.49f, -0.793f, -2.914f, -1.587f, -4.075f)
        curveToRelative(-0.576f, -0.523f, -1.193f, -0.938f, -1.839f, -1.179f)
        curveToRelative(-3.161f, -1.179f, -4.91f, 2.027f, -7.166f, 5.018f)
        curveToRelative(-1.022f, 1.354f, -2.08f, 2.756f, -3.351f, 4.042f)
        lineTo(8.606f, 32.216f)
        curveToRelative(-0.372f, 0.376f, -0.745f, 0.742f, -1.111f, 1.101f)
        curveToRelative(-2.09f, 2.047f, -4.448f, 3.597f, -4.468f, 5.929f)
        curveToRelative(-0.002f, 0.217f, 0.017f, 0.431f, 0.042f, 0.645f)
        curveTo(3.079f, 39.978f, 3.09f, 40.065f, 3.106f, 40.153f)
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
        moveTo(32.027f, 12.983f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 32.027f, 14.983f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 32.027f, 12.983f)
        close()
        moveTo(36.027f, 15.983f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 36.027f, 17.983f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 36.027f, 15.983f)
        close()
        moveTo(29.027f, 15.983f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 29.027f, 17.983f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 29.027f, 15.983f)
        close()
        moveTo(33.027f, 18.983f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 33.027f, 20.983f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 33.027f, 18.983f)
        close()
        moveTo(15.027f, 29.983f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 15.027f, 31.983f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 15.027f, 29.983f)
        close()
        moveTo(19.027f, 32.983f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 19.027f, 34.983f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 19.027f, 32.983f)
        close()
        moveTo(12.027f, 32.983f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 12.027f, 34.983f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 12.027f, 32.983f)
        close()
        moveTo(16.027f, 35.983f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 16.027f, 37.983f)
        arcTo(1f, 1f, 0f, isMoreThanHalf = true, isPositiveArc = false, 16.027f, 35.983f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFBF360C)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(22.566f, 40.625f)
        arcTo(0.948f, 1.897f, 0f, isMoreThanHalf = true, isPositiveArc = false, 22.566f, 44.419f)
        arcTo(0.948f, 1.897f, 0f, isMoreThanHalf = true, isPositiveArc = false, 22.566f, 40.625f)
        close()
      }
    }.build()
    return _vector!!
  }
