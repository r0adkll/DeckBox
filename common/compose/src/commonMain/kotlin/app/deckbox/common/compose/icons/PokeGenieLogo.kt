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

private var _pokeGenie: ImageVector? = null

val DeckBoxIcons.Logos.PokeGenie: ImageVector
  get() {
    if (_pokeGenie != null) {
      return _pokeGenie!!
    }
    _pokeGenie = ImageVector.Builder(
      name = "Logos.PokeGenie",
      defaultWidth = 100.dp,
      defaultHeight = 100.dp,
      viewportWidth = 100f,
      viewportHeight = 100f,
    ).apply {
      path(
        fill = SolidColor(Color(0xFFC7EDE6)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(86.277f, 56.241f)
        curveToRelative(1.135f, -2.155f, 1.785f, -4.605f, 1.785f, -7.21f)
        curveToRelative(0f, -6.621f, -4.159f, -12.257f, -10.001f, -14.478f)
        curveToRelative(0f, -0.007f, 0.001f, -0.014f, 0.001f, -0.022f)
        curveToRelative(0f, -11.598f, -9.402f, -21f, -21f, -21f)
        curveToRelative(-9.784f, 0f, -17.981f, 6.701f, -20.313f, 15.757f)
        curveToRelative(-1.476f, -0.485f, -3.049f, -0.757f, -4.687f, -0.757f)
        curveToRelative(-7.692f, 0f, -14.023f, 5.793f, -14.89f, 13.252f)
        curveToRelative(-5.204f, 1.101f, -9.11f, 5.717f, -9.11f, 11.248f)
        curveToRelative(0f, 6.351f, 5.149f, 11.5f, 11.5f, 11.5f)
        curveToRelative(0.177f, 0f, 0.352f, -0.012f, 0.526f, -0.022f)
        curveToRelative(-0.004f, 0.175f, -0.026f, 0.346f, -0.026f, 0.522f)
        curveToRelative(0f, 11.322f, 9.178f, 20.5f, 20.5f, 20.5f)
        curveToRelative(6.437f, 0f, 12.175f, -2.972f, 15.934f, -7.614f)
        curveToRelative(2.178f, 2.225f, 5.206f, 3.614f, 8.566f, 3.614f)
        curveToRelative(4.65f, 0f, 8.674f, -2.65f, 10.666f, -6.518f)
        curveToRelative(1.052f, 0.335f, 2.171f, 0.518f, 3.334f, 0.518f)
        curveToRelative(6.075f, 0f, 11f, -4.925f, 11f, -11f)
        curveTo(90.063f, 61.22f, 88.592f, 58.258f, 86.277f, 56.241f)
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
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(67.43f, 21.492f)
        horizontalLineToRelative(-10f)
        curveToRelative(-0.276f, 0f, -0.5f, -0.224f, -0.5f, -0.5f)
        reflectiveCurveToRelative(0.224f, -0.5f, 0.5f, -0.5f)
        horizontalLineToRelative(10f)
        curveToRelative(0.276f, 0f, 0.5f, 0.224f, 0.5f, 0.5f)
        reflectiveCurveTo(67.707f, 21.492f, 67.43f, 21.492f)
        close()
        moveTo(70.43f, 21.492f)
        horizontalLineToRelative(-1f)
        curveToRelative(-0.276f, 0f, -0.5f, -0.224f, -0.5f, -0.5f)
        reflectiveCurveToRelative(0.224f, -0.5f, 0.5f, -0.5f)
        horizontalLineToRelative(1f)
        curveToRelative(0.276f, 0f, 0.5f, 0.224f, 0.5f, 0.5f)
        reflectiveCurveTo(70.707f, 21.492f, 70.43f, 21.492f)
        close()
        moveTo(75.421f, 23.492f)
        horizontalLineTo(66.43f)
        curveToRelative(-0.276f, 0f, -0.5f, -0.224f, -0.5f, -0.5f)
        reflectiveCurveToRelative(0.224f, -0.5f, 0.5f, -0.5f)
        horizontalLineToRelative(8.991f)
        curveToRelative(0.276f, 0f, 0.5f, 0.224f, 0.5f, 0.5f)
        reflectiveCurveTo(75.697f, 23.492f, 75.421f, 23.492f)
        close()
        moveTo(64.43f, 23.492f)
        horizontalLineToRelative(-1f)
        curveToRelative(-0.276f, 0f, -0.5f, -0.224f, -0.5f, -0.5f)
        reflectiveCurveToRelative(0.224f, -0.5f, 0.5f, -0.5f)
        horizontalLineToRelative(1f)
        curveToRelative(0.276f, 0f, 0.5f, 0.224f, 0.5f, 0.5f)
        reflectiveCurveTo(64.707f, 23.492f, 64.43f, 23.492f)
        close()
        moveTo(61.43f, 23.492f)
        horizontalLineToRelative(-2f)
        curveToRelative(-0.276f, 0f, -0.5f, -0.224f, -0.5f, -0.5f)
        reflectiveCurveToRelative(0.224f, -0.5f, 0.5f, -0.5f)
        horizontalLineToRelative(2f)
        curveToRelative(0.276f, 0f, 0.5f, 0.224f, 0.5f, 0.5f)
        reflectiveCurveTo(61.707f, 23.492f, 61.43f, 23.492f)
        close()
        moveTo(67.43f, 25.492f)
        horizontalLineToRelative(-2f)
        curveToRelative(-0.276f, 0f, -0.5f, -0.224f, -0.5f, -0.5f)
        reflectiveCurveToRelative(0.224f, -0.5f, 0.5f, -0.5f)
        horizontalLineToRelative(2f)
        curveToRelative(0.276f, 0f, 0.5f, 0.224f, 0.5f, 0.5f)
        reflectiveCurveTo(67.706f, 25.492f, 67.43f, 25.492f)
        close()
        moveTo(70.43f, 16.492f)
        curveToRelative(-0.177f, 0f, -0.823f, 0f, -1f, 0f)
        curveToRelative(-0.276f, 0f, -0.5f, 0.224f, -0.5f, 0.5f)
        reflectiveCurveToRelative(0.224f, 0.5f, 0.5f, 0.5f)
        curveToRelative(0.177f, 0f, 0.823f, 0f, 1f, 0f)
        curveToRelative(0.276f, 0f, 0.5f, -0.224f, 0.5f, -0.5f)
        reflectiveCurveTo(70.706f, 16.492f, 70.43f, 16.492f)
        close()
        moveTo(70.43f, 18.492f)
        curveToRelative(-0.177f, 0f, -4.823f, 0f, -5f, 0f)
        curveToRelative(-0.276f, 0f, -0.5f, 0.224f, -0.5f, 0.5f)
        reflectiveCurveToRelative(0.224f, 0.5f, 0.5f, 0.5f)
        curveToRelative(0.177f, 0f, 4.823f, 0f, 5f, 0f)
        curveToRelative(0.276f, 0f, 0.5f, -0.224f, 0.5f, -0.5f)
        reflectiveCurveTo(70.706f, 18.492f, 70.43f, 18.492f)
        close()
        moveTo(75.43f, 20.492f)
        curveToRelative(-0.177f, 0f, -2.823f, 0f, -3f, 0f)
        curveToRelative(-0.276f, 0f, -0.5f, 0.224f, -0.5f, 0.5f)
        reflectiveCurveToRelative(0.224f, 0.5f, 0.5f, 0.5f)
        curveToRelative(0.177f, 0f, 2.823f, 0f, 3f, 0f)
        curveToRelative(0.276f, 0f, 0.5f, -0.224f, 0.5f, -0.5f)
        reflectiveCurveTo(75.706f, 20.492f, 75.43f, 20.492f)
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
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(33.069f, 79.493f)
        horizontalLineTo(23.1f)
        curveToRelative(-0.274f, 0f, -0.497f, -0.223f, -0.497f, -0.497f)
        curveToRelative(0f, -0.274f, 0.223f, -0.497f, 0.497f, -0.497f)
        horizontalLineToRelative(9.969f)
        curveToRelative(0.274f, 0f, 0.497f, 0.223f, 0.497f, 0.497f)
        curveTo(33.566f, 79.27f, 33.344f, 79.493f, 33.069f, 79.493f)
        close()
        moveTo(36.43f, 78.996f)
        curveToRelative(0f, -0.274f, -0.223f, -0.497f, -0.497f, -0.497f)
        horizontalLineToRelative(-1.437f)
        curveToRelative(-0.274f, 0f, -0.497f, 0.223f, -0.497f, 0.497f)
        curveToRelative(0f, 0.274f, 0.223f, 0.497f, 0.497f, 0.497f)
        horizontalLineToRelative(1.437f)
        curveTo(36.208f, 79.493f, 36.43f, 79.27f, 36.43f, 78.996f)
        close()
        moveTo(40.46f, 78.996f)
        curveToRelative(0f, -0.274f, -0.223f, -0.497f, -0.497f, -0.497f)
        horizontalLineToRelative(-2.531f)
        curveToRelative(-0.274f, 0f, -0.497f, 0.223f, -0.497f, 0.497f)
        curveToRelative(0f, 0.274f, 0.223f, 0.497f, 0.497f, 0.497f)
        horizontalLineToRelative(2.531f)
        curveTo(40.238f, 79.493f, 40.46f, 79.27f, 40.46f, 78.996f)
        close()
        moveTo(40.46f, 80.984f)
        curveToRelative(0f, -0.274f, -0.223f, -0.497f, -0.497f, -0.497f)
        horizontalLineToRelative(-9.558f)
        curveToRelative(-0.274f, 0f, -0.497f, 0.223f, -0.497f, 0.497f)
        curveToRelative(0f, 0.274f, 0.223f, 0.497f, 0.497f, 0.497f)
        horizontalLineToRelative(9.558f)
        curveTo(40.238f, 81.481f, 40.46f, 81.258f, 40.46f, 80.984f)
        close()
        moveTo(29.181f, 80.984f)
        curveToRelative(0f, -0.274f, -0.223f, -0.497f, -0.497f, -0.497f)
        horizontalLineToRelative(-0.576f)
        curveToRelative(-0.274f, 0f, -0.497f, 0.223f, -0.497f, 0.497f)
        curveToRelative(0f, 0.274f, 0.223f, 0.497f, 0.497f, 0.497f)
        horizontalLineToRelative(0.576f)
        curveTo(28.958f, 81.481f, 29.181f, 81.258f, 29.181f, 80.984f)
        close()
        moveTo(26.819f, 80.984f)
        curveToRelative(0f, -0.274f, -0.223f, -0.497f, -0.497f, -0.497f)
        horizontalLineToRelative(-1.448f)
        curveToRelative(-0.274f, 0f, -0.497f, 0.223f, -0.497f, 0.497f)
        curveToRelative(0f, 0.274f, 0.223f, 0.497f, 0.497f, 0.497f)
        horizontalLineToRelative(1.448f)
        curveTo(26.597f, 81.481f, 26.819f, 81.258f, 26.819f, 80.984f)
        close()
        moveTo(35.899f, 77.008f)
        curveToRelative(0f, -0.274f, -0.223f, -0.497f, -0.497f, -0.497f)
        horizontalLineToRelative(-4.996f)
        curveToRelative(-0.274f, 0f, -0.497f, 0.223f, -0.497f, 0.497f)
        reflectiveCurveToRelative(0.223f, 0.497f, 0.497f, 0.497f)
        horizontalLineToRelative(4.996f)
        curveTo(35.677f, 77.505f, 35.899f, 77.283f, 35.899f, 77.008f)
        close()
        moveTo(35.899f, 75.02f)
        curveToRelative(0f, -0.274f, -0.223f, -0.497f, -0.497f, -0.497f)
        horizontalLineToRelative(-1.249f)
        curveToRelative(-0.274f, 0f, -0.497f, 0.223f, -0.497f, 0.497f)
        reflectiveCurveToRelative(0.223f, 0.497f, 0.497f, 0.497f)
        horizontalLineToRelative(1.249f)
        curveTo(35.677f, 75.517f, 35.899f, 75.295f, 35.899f, 75.02f)
        close()
        moveTo(32.65f, 82.972f)
        curveToRelative(0f, -0.274f, -0.223f, -0.497f, -0.497f, -0.497f)
        horizontalLineToRelative(-1.747f)
        curveToRelative(-0.274f, 0f, -0.497f, 0.223f, -0.497f, 0.497f)
        curveToRelative(0f, 0.274f, 0.223f, 0.497f, 0.497f, 0.497f)
        horizontalLineToRelative(1.747f)
        curveTo(32.428f, 83.469f, 32.65f, 83.246f, 32.65f, 82.972f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFFFDFCEF)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(28.531f, 48.719f)
        curveToRelative(0f, 0f, 1.567f, 0f, 3.5f, 0f)
        reflectiveCurveToRelative(3.5f, -1.567f, 3.5f, -3.5f)
        curveToRelative(0f, -1.781f, -1.335f, -3.234f, -3.055f, -3.455f)
        curveToRelative(0.028f, -0.179f, 0.055f, -0.358f, 0.055f, -0.545f)
        curveToRelative(0f, -1.933f, -1.567f, -3.5f, -3.5f, -3.5f)
        curveToRelative(-1.032f, 0f, -1.95f, 0.455f, -2.59f, 1.165f)
        curveToRelative(-0.384f, -1.808f, -1.987f, -3.165f, -3.91f, -3.165f)
        curveToRelative(-2.209f, 0f, -4f, 1.791f, -4f, 4f)
        curveToRelative(0f, 0.191f, 0.03f, 0.374f, 0.056f, 0.558f)
        curveToRelative(-0.428f, -0.344f, -0.964f, -0.558f, -1.556f, -0.558f)
        curveToRelative(-1.228f, 0f, -2.245f, 0.887f, -2.455f, 2.055f)
        curveToRelative(-0.179f, -0.028f, -0.358f, -0.055f, -0.545f, -0.055f)
        curveToRelative(-1.933f, 0f, -3.5f, 1.567f, -3.5f, 3.5f)
        curveToRelative(0f, 1.933f, 1.567f, 3.5f, 3.5f, 3.5f)
        reflectiveCurveToRelative(7.5f, 0f, 7.5f, 0f)
        verticalLineToRelative(0.5f)
        horizontalLineToRelative(7f)
        verticalLineTo(48.719f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF472B29)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(30.281f, 44.219f)
        curveToRelative(-0.138f, 0f, -0.25f, -0.112f, -0.25f, -0.25f)
        curveToRelative(0f, -1.223f, 0.995f, -2.218f, 2.218f, -2.218f)
        curveToRelative(0.034f, 0.009f, 0.737f, -0.001f, 1.244f, 0.136f)
        curveToRelative(0.133f, 0.036f, 0.212f, 0.173f, 0.176f, 0.306f)
        curveToRelative(-0.036f, 0.134f, -0.173f, 0.213f, -0.306f, 0.176f)
        curveToRelative(-0.444f, -0.12f, -1.1f, -0.12f, -1.113f, -0.118f)
        curveToRelative(-0.948f, 0f, -1.719f, 0.771f, -1.719f, 1.718f)
        curveTo(30.531f, 44.107f, 30.419f, 44.219f, 30.281f, 44.219f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF472B29)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(24.031f, 48.719f)
        arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 23.531f, 49.219f)
        arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 23.031f, 48.719f)
        arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 24.031f, 48.719f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF472B29)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(32.031f, 49.219f)
        horizontalLineToRelative(-3.5f)
        curveToRelative(-0.276f, 0f, -0.5f, -0.224f, -0.5f, -0.5f)
        curveToRelative(0f, -0.276f, 0.224f, -0.5f, 0.5f, -0.5f)
        horizontalLineToRelative(3.5f)
        curveToRelative(1.654f, 0f, 3f, -1.346f, 3f, -3f)
        curveToRelative(0f, -1.496f, -1.125f, -2.768f, -2.618f, -2.959f)
        curveToRelative(-0.134f, -0.018f, -0.255f, -0.088f, -0.336f, -0.196f)
        reflectiveCurveToRelative(-0.115f, -0.244f, -0.094f, -0.377f)
        curveToRelative(0.023f, -0.154f, 0.048f, -0.308f, 0.048f, -0.468f)
        curveToRelative(0f, -1.654f, -1.346f, -3f, -3f, -3f)
        curveToRelative(-0.85f, 0f, -1.638f, 0.355f, -2.219f, 1f)
        curveToRelative(-0.125f, 0.139f, -0.321f, 0.198f, -0.5f, 0.148f)
        curveToRelative(-0.182f, -0.049f, -0.321f, -0.195f, -0.36f, -0.379f)
        curveToRelative(-0.341f, -1.604f, -1.78f, -2.769f, -3.421f, -2.769f)
        curveToRelative(-1.93f, 0f, -3.5f, 1.57f, -3.5f, 3.5f)
        curveToRelative(0f, 0.143f, 0.021f, 0.28f, 0.041f, 0.418f)
        curveToRelative(0.029f, 0.203f, -0.063f, 0.438f, -0.242f, 0.54f)
        curveToRelative(-0.179f, 0.102f, -0.396f, 0.118f, -0.556f, -0.01f)
        curveToRelative(-0.365f, -0.293f, -0.794f, -0.448f, -1.243f, -0.448f)
        curveToRelative(-0.966f, 0f, -1.792f, 0.691f, -1.963f, 1.644f)
        curveToRelative(-0.048f, 0.267f, -0.296f, 0.446f, -0.569f, 0.405f)
        curveToRelative(-0.154f, -0.024f, -0.308f, -0.049f, -0.468f, -0.049f)
        curveToRelative(-1.654f, 0f, -3f, 1.346f, -3f, 3f)
        curveToRelative(0f, 1.654f, 1.346f, 3f, 3f, 3f)
        horizontalLineToRelative(7.5f)
        curveToRelative(0.276f, 0f, 0.5f, 0.224f, 0.5f, 0.5f)
        curveToRelative(0f, 0.276f, -0.224f, 0.5f, -0.5f, 0.5f)
        horizontalLineToRelative(-7.5f)
        curveToRelative(-2.206f, 0f, -4f, -1.794f, -4f, -4f)
        curveToRelative(0f, -2.206f, 1.794f, -4f, 4f, -4f)
        curveToRelative(0.059f, 0f, 0.116f, 0.002f, 0.174f, 0.006f)
        curveToRelative(0.414f, -1.186f, 1.537f, -2.006f, 2.826f, -2.006f)
        curveToRelative(0.349f, 0f, 0.689f, 0.061f, 1.011f, 0.18f)
        curveToRelative(0.165f, -2.333f, 2.115f, -4.18f, 4.489f, -4.18f)
        curveToRelative(1.831f, 0f, 3.466f, 1.127f, 4.153f, 2.774f)
        curveToRelative(0.68f, -0.498f, 1.502f, -0.774f, 2.347f, -0.774f)
        curveToRelative(2.206f, 0f, 4f, 1.794f, 4f, 4f)
        curveToRelative(0f, 0.048f, -0.001f, 0.095f, -0.004f, 0.142f)
        curveToRelative(1.743f, 0.448f, 3.004f, 2.027f, 3.004f, 3.858f)
        curveTo(36.031f, 47.425f, 34.237f, 49.219f, 32.031f, 49.219f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF472B29)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(26.531f, 48.219f)
        curveToRelative(-0.159f, 0f, -0.841f, 0f, -1f, 0f)
        curveToRelative(-0.276f, 0f, -0.5f, 0.224f, -0.5f, 0.5f)
        curveToRelative(0f, 0.276f, 0.224f, 0.5f, 0.5f, 0.5f)
        curveToRelative(0.159f, 0f, 0.841f, 0f, 1f, 0f)
        curveToRelative(0.276f, 0f, 0.5f, -0.224f, 0.5f, -0.5f)
        curveTo(27.031f, 48.443f, 26.807f, 48.219f, 26.531f, 48.219f)
        close()
      }
      path(
        fill = SolidColor(Color(0xFF3070B7)),
        fillAlpha = 1.0f,
        stroke = SolidColor(Color(0xFF472B29)),
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.4f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 10f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(63.729f, 71.531f)
        horizontalLineTo(34.396f)
        curveToRelative(-4.05f, 0f, -7.333f, -3.283f, -7.333f, -7.333f)
        verticalLineTo(34.865f)
        curveToRelative(0f, -4.05f, 3.283f, -7.333f, 7.333f, -7.333f)
        horizontalLineToRelative(29.333f)
        curveToRelative(4.05f, 0f, 7.333f, 3.283f, 7.333f, 7.333f)
        verticalLineToRelative(29.333f)
        curveTo(71.063f, 68.248f, 67.779f, 71.531f, 63.729f, 71.531f)
        close()
      }
      path(
        fill = null,
        fillAlpha = 1.0f,
        stroke = SolidColor(Color(0xFF472B29)),
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 10f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(31.271f, 50.21f)
        curveToRelative(-1.377f, -1.627f, -2.209f, -3.729f, -2.209f, -6.023f)
        curveToRelative(0f, -5.146f, 4.187f, -9.333f, 9.333f, -9.333f)
        curveToRelative(2.416f, 0f, 4.62f, 0.923f, 6.28f, 2.434f)
      }
      path(
        fill = null,
        fillAlpha = 1.0f,
        stroke = SolidColor(Color(0xFF472B29)),
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 10f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(47.729f, 44.188f)
        curveToRelative(0f, 0.968f, -0.148f, 1.901f, -0.423f, 2.779f)
        curveToRelative(-1.186f, 3.793f, -4.732f, 6.554f, -8.911f, 6.554f)
        curveToRelative(-1.166f, 0f, -2.283f, -0.215f, -3.312f, -0.607f)
        curveToRelative(-0.775f, -0.295f, -1.502f, -0.691f, -2.163f, -1.172f)
      }
      path(
        fill = SolidColor(Color(0xFFFFFDFD)),
        fillAlpha = 1.0f,
        stroke = SolidColor(Color(0xFF472B29)),
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 10f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(46.068f, 38.878f)
        curveToRelative(0.372f, 0.536f, 0.689f, 1.112f, 0.944f, 1.721f)
        curveToRelative(0.216f, 0.517f, 0.387f, 1.057f, 0.508f, 1.616f)
      }
      path(
        fill = SolidColor(Color(0xFFF5905F)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(45.125f, 44.188f)
        arcTo(6.667f, 6.667f, 0f, isMoreThanHalf = false, isPositiveArc = true, 38.458f, 50.855000000000004f)
        arcTo(6.667f, 6.667f, 0f, isMoreThanHalf = false, isPositiveArc = true, 31.790999999999997f, 44.188f)
        arcTo(6.667f, 6.667f, 0f, isMoreThanHalf = false, isPositiveArc = true, 45.125f, 44.188f)
        close()
      }
      path(
        fill = null,
        fillAlpha = 1.0f,
        stroke = SolidColor(Color(0xFF472B29)),
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 10f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(67.729f, 48.188f)
        lineTo(71.063f, 48.188f)
      }
      path(
        fill = null,
        fillAlpha = 1.0f,
        stroke = SolidColor(Color(0xFF472B29)),
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 10f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(65.74f, 48.188f)
        lineTo(65.375f, 48.188f)
      }
      path(
        fill = null,
        fillAlpha = 1.0f,
        stroke = SolidColor(Color(0xFF472B29)),
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 10f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(59.792f, 48.188f)
        lineTo(61.656f, 48.188f)
        lineTo(63.76f, 48.219f)
      }
      path(
        fill = null,
        fillAlpha = 1.0f,
        stroke = SolidColor(Color(0xFF472B29)),
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 10f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(35.125f, 59.531f)
        horizontalLineTo(39f)
        curveToRelative(2.145f, 0f, 4.118f, -0.665f, 5.676f, -2.139f)
        lineToRelative(7.439f, -7.038f)
        curveToRelative(1.492f, -1.411f, 3.592f, -2.166f, 5.645f, -2.166f)
      }
      path(
        fill = null,
        fillAlpha = 1.0f,
        stroke = SolidColor(Color(0xFF472B29)),
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 10f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(29.812f, 59.531f)
        lineTo(31.125f, 59.531f)
        lineTo(33.5f, 59.531f)
      }
      path(
        fill = null,
        fillAlpha = 1.0f,
        stroke = SolidColor(Color(0xFF472B29)),
        strokeAlpha = 1.0f,
        strokeLineWidth = .5f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 10f,
        pathFillType = PathFillType.NonZero,
      ) {
        moveTo(45.125f, 44.188f)
        arcTo(6.667f, 6.667f, 0f, isMoreThanHalf = false, isPositiveArc = true, 38.458f, 50.855000000000004f)
        arcTo(6.667f, 6.667f, 0f, isMoreThanHalf = false, isPositiveArc = true, 31.790999999999997f, 44.188f)
        arcTo(6.667f, 6.667f, 0f, isMoreThanHalf = false, isPositiveArc = true, 45.125f, 44.188f)
        close()
      }
      group {
        path(
          fill = SolidColor(Color(0xFFFDFCEF)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(75.024f, 66.943f)
          horizontalLineToRelative(7.934f)
          curveToRelative(0.133f, 0f, 0.71f, 0f, 0.874f, 0f)
          curveToRelative(2.161f, 0f, 3.903f, -1.756f, 3.818f, -3.889f)
          curveToRelative(-0.06f, -1.503f, -1.084f, -2.828f, -2.522f, -3.356f)
          curveToRelative(-0.752f, -0.276f, -1.466f, -0.285f, -2.123f, -0.143f)
          curveToRelative(-0.093f, -1.392f, -1.274f, -2.494f, -2.72f, -2.494f)
          curveToRelative(-0.79f, 0f, -1.5f, 0.331f, -1.998f, 0.856f)
          curveToRelative(0.109f, -0.526f, 0.121f, -1.087f, 0.011f, -1.665f)
          curveToRelative(-0.342f, -1.802f, -1.88f, -3.208f, -3.739f, -3.431f)
          curveToRelative(-2.542f, -0.305f, -4.715f, 1.532f, -4.9f, 3.913f)
          curveToRelative(-0.722f, -0.936f, -1.865f, -1.543f, -3.155f, -1.543f)
          curveToRelative(-2.185f, 0f, -3.957f, 1.734f, -3.957f, 3.873f)
          curveToRelative(0f, 0.163f, 0.014f, 0.322f, 0.034f, 0.48f)
          curveToRelative(-1.733f, 0.355f, -3.035f, 1.858f, -3.035f, 3.66f)
          curveToRelative(0f, 2.065f, 1.711f, 3.739f, 3.821f, 3.739f)
          curveToRelative(0.023f, 0f, 1.657f, 0f, 3.54f, 0f)
          moveTo(73.024f, 66.943f)
          horizontalLineToRelative(0.36f)
        }
        path(
          fill = SolidColor(Color(0xFF472B29)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(59.047f, 63.203f)
          curveToRelative(0f, -1.85f, 1.239f, -3.483f, 3.003f, -4.038f)
          curveToRelative(-0.001f, -0.035f, -0.001f, -0.069f, -0.001f, -0.103f)
          curveToRelative(0f, -2.411f, 1.999f, -4.373f, 4.457f, -4.373f)
          curveToRelative(1.056f, 0f, 2.051f, 0.359f, 2.842f, 1.007f)
          curveToRelative(0.268f, -0.893f, 0.798f, -1.689f, 1.546f, -2.302f)
          curveToRelative(1.041f, -0.853f, 2.361f, -1.238f, 3.726f, -1.071f)
          curveToRelative(2.074f, 0.248f, 3.789f, 1.824f, 4.17f, 3.833f)
          curveToRelative(0.046f, 0.243f, 0.074f, 0.485f, 0.083f, 0.727f)
          curveToRelative(0.437f, -0.21f, 0.921f, -0.323f, 1.414f, -0.323f)
          curveToRelative(1.506f, 0f, 2.791f, 1.028f, 3.135f, 2.424f)
          curveToRelative(0.64f, -0.06f, 1.27f, 0.019f, 1.88f, 0.244f)
          curveToRelative(1.637f, 0.602f, 2.782f, 2.131f, 2.849f, 3.806f)
          curveToRelative(0.046f, 1.149f, -0.37f, 2.242f, -1.173f, 3.078f)
          curveToRelative(-0.813f, 0.846f, -1.959f, 1.331f, -3.144f, 1.331f)
          horizontalLineToRelative(-8.807f)
          curveToRelative(-0.276f, 0f, -0.5f, -0.224f, -0.5f, -0.5f)
          reflectiveCurveToRelative(0.224f, -0.5f, 0.5f, -0.5f)
          horizontalLineToRelative(8.807f)
          curveToRelative(0.927f, 0f, 1.788f, -0.364f, 2.423f, -1.024f)
          curveToRelative(0.612f, -0.637f, 0.93f, -1.47f, 0.895f, -2.346f)
          curveToRelative(-0.051f, -1.274f, -0.933f, -2.442f, -2.195f, -2.905f)
          curveToRelative(-0.603f, -0.223f, -1.207f, -0.263f, -1.846f, -0.125f)
          curveToRelative(-0.142f, 0.031f, -0.29f, -0.002f, -0.406f, -0.089f)
          curveToRelative(-0.116f, -0.088f, -0.188f, -0.22f, -0.198f, -0.366f)
          curveToRelative(-0.076f, -1.136f, -1.052f, -2.027f, -2.221f, -2.027f)
          curveToRelative(-0.626f, 0f, -1.207f, 0.249f, -1.635f, 0.7f)
          curveToRelative(-0.154f, 0.163f, -0.397f, 0.202f, -0.594f, 0.1f)
          curveToRelative(-0.198f, -0.104f, -0.303f, -0.326f, -0.258f, -0.545f)
          curveToRelative(0.1f, -0.482f, 0.103f, -0.977f, 0.009f, -1.472f)
          curveToRelative(-0.301f, -1.585f, -1.661f, -2.83f, -3.307f, -3.027f)
          curveToRelative(-1.088f, -0.126f, -2.144f, 0.173f, -2.973f, 0.853f)
          curveToRelative(-0.804f, 0.66f, -1.29f, 1.584f, -1.369f, 2.604f)
          curveToRelative(-0.016f, 0.206f, -0.157f, 0.38f, -0.355f, 0.44f)
          curveToRelative(-0.198f, 0.057f, -0.413f, -0.01f, -0.539f, -0.174f)
          curveToRelative(-0.662f, -0.858f, -1.668f, -1.349f, -2.76f, -1.349f)
          curveToRelative(-1.906f, 0f, -3.457f, 1.513f, -3.457f, 3.373f)
          curveToRelative(0f, 0.142f, 0.012f, 0.28f, 0.03f, 0.417f)
          curveToRelative(0.033f, 0.26f, -0.14f, 0.501f, -0.396f, 0.554f)
          curveToRelative(-1.528f, 0.312f, -2.636f, 1.646f, -2.636f, 3.17f)
          curveToRelative(0f, 1.786f, 1.49f, 3.239f, 3.321f, 3.239f)
          horizontalLineToRelative(3.54f)
          curveToRelative(0.276f, 0f, 0.5f, 0.224f, 0.5f, 0.5f)
          reflectiveCurveToRelative(-0.224f, 0.5f, -0.5f, 0.5f)
          horizontalLineToRelative(-3.54f)
          curveTo(60.984f, 67.444f, 59.046f, 65.542f, 59.047f, 63.203f)
          close()
          moveTo(72.524f, 66.944f)
          curveToRelative(0f, -0.276f, 0.224f, -0.5f, 0.5f, -0.5f)
          horizontalLineToRelative(0.36f)
          curveToRelative(0.276f, 0f, 0.5f, 0.224f, 0.5f, 0.5f)
          reflectiveCurveToRelative(-0.224f, 0.5f, -0.5f, 0.5f)
          horizontalLineToRelative(-0.36f)
          curveTo(72.748f, 67.444f, 72.524f, 67.22f, 72.524f, 66.944f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFF472B29)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(65.074f, 61.64f)
          curveToRelative(0.018f, 0f, 0.036f, -0.002f, 0.055f, -0.006f)
          curveToRelative(0.135f, -0.03f, 0.22f, -0.164f, 0.189f, -0.299f)
          curveToRelative(-0.037f, -0.164f, -0.094f, -0.325f, -0.17f, -0.479f)
          curveToRelative(-0.604f, -1.224f, -2.272f, -1.677f, -3.722f, -1.007f)
          curveToRelative(-0.125f, 0.058f, -0.18f, 0.206f, -0.122f, 0.331f)
          curveToRelative(0.058f, 0.126f, 0.207f, 0.179f, 0.332f, 0.122f)
          curveToRelative(1.201f, -0.556f, 2.577f, -0.208f, 3.063f, 0.774f)
          curveToRelative(0.058f, 0.118f, 0.102f, 0.242f, 0.13f, 0.367f)
          curveTo(64.856f, 61.561f, 64.959f, 61.64f, 65.074f, 61.64f)
          close()
        }
        path(
          fill = SolidColor(Color(0xFF472B29)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(70.233f, 67.444f)
          horizontalLineToRelative(1.107f)
          curveToRelative(0.276f, 0f, 0.5f, -0.224f, 0.5f, -0.5f)
          reflectiveCurveToRelative(-0.224f, -0.5f, -0.5f, -0.5f)
          horizontalLineToRelative(-1.107f)
          curveToRelative(-0.276f, 0f, -0.5f, 0.224f, -0.5f, 0.5f)
          reflectiveCurveTo(69.956f, 67.444f, 70.233f, 67.444f)
          close()
        }
      }
    }.build()
    return _pokeGenie!!
  }
