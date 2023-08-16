package app.deckbox.common.compose.icons.types

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.DeckBoxIcons

private var _grass: ImageVector? = null

val DeckBoxIcons.Types.Grass: ImageVector
  get() {
    if (_grass != null) {
      return _grass!!
    }
    _grass = ImageVector.Builder(
      name = "Types.Grass",
      defaultWidth = 24.dp,
      defaultHeight = 24.dp,
      viewportWidth = 24f,
      viewportHeight = 24f,
    ).apply {
      group {
        path(
          fill = SolidColor(Color(0xFF000000)),
          fillAlpha = 1.0f,
          stroke = null,
          strokeAlpha = 1.0f,
          strokeLineWidth = 1.0f,
          strokeLineCap = StrokeCap.Butt,
          strokeLineJoin = StrokeJoin.Miter,
          strokeLineMiter = 1.0f,
          pathFillType = PathFillType.NonZero,
        ) {
          moveTo(15.5f, 1.65067f)
          curveTo(19.9405f, 7.2251f, 21.821f, 11.5362f, 21.1415f, 14.5838f)
          curveTo(20.4621f, 17.6315f, 17.2479f, 19.3028f, 11.4989f, 19.5978f)
          lineTo(11.791f, 22.3281f)
          lineTo(9.06788f, 21.8765f)
          lineTo(8.43184f, 19.1203f)
          curveTo(3.5741f, 17.4816f, 1.9302f, 14.6521f, 3.5003f, 10.6317f)
          curveTo(5.0704f, 6.6114f, 9.0703f, 3.6177f, 15.5f, 1.6507f)
          close()
          moveTo(13.939f, 4.27524f)
          curveTo(13.2275f, 5.2287f, 12.7154f, 5.9519f, 12.4028f, 6.445f)
          curveTo(12.0902f, 6.938f, 11.7671f, 7.5322f, 11.4335f, 8.2275f)
          curveTo(10.8307f, 7.6955f, 10.2619f, 7.3434f, 9.7273f, 7.1714f)
          curveTo(9.1926f, 6.9993f, 8.582f, 6.9769f, 7.8955f, 7.1042f)
          curveTo(8.8369f, 7.4806f, 9.4754f, 7.8124f, 9.8111f, 8.0996f)
          curveTo(10.1468f, 8.3867f, 10.4718f, 8.8547f, 10.7861f, 9.5038f)
          lineTo(10.2226f, 10.996f)
          curveTo(9.5072f, 10.3841f, 8.7816f, 10.028f, 8.0459f, 9.9277f)
          curveTo(7.3102f, 9.8274f, 6.5436f, 9.904f, 5.7462f, 10.1575f)
          curveTo(6.7421f, 10.2627f, 7.5939f, 10.5061f, 8.3015f, 10.8878f)
          curveTo(9.0091f, 11.2696f, 9.4843f, 11.7724f, 9.7273f, 12.3963f)
          lineTo(9.36285f, 14.4286f)
          curveTo(8.4347f, 13.883f, 7.5984f, 13.5501f, 6.8539f, 13.4299f)
          curveTo(6.1094f, 13.3098f, 5.3977f, 13.405f, 4.7189f, 13.7156f)
          curveTo(5.6212f, 13.7521f, 6.4877f, 13.9897f, 7.3185f, 14.4286f)
          curveTo(8.1493f, 14.8675f, 8.7814f, 15.4677f, 9.215f, 16.2293f)
          curveTo(9.2061f, 17.2229f, 9.2553f, 17.9519f, 9.3628f, 18.4162f)
          curveTo(9.4703f, 18.8805f, 9.6949f, 19.3342f, 10.0365f, 19.7773f)
          curveTo(10.0688f, 19.2545f, 10.0969f, 18.8008f, 10.1208f, 18.4162f)
          curveTo(10.1448f, 18.0316f, 10.1969f, 17.5099f, 10.2773f, 16.851f)
          curveTo(11.2273f, 16.0107f, 12.2808f, 15.4098f, 13.4376f, 15.0483f)
          curveTo(14.5944f, 14.6868f, 16.0617f, 14.6234f, 17.8395f, 14.8581f)
          curveTo(16.6238f, 14.3107f, 15.4327f, 14.0668f, 14.2663f, 14.1262f)
          curveTo(13.0999f, 14.1856f, 11.9012f, 14.4658f, 10.6703f, 14.9668f)
          lineTo(10.8985f, 12.7821f)
          curveTo(11.9357f, 11.8738f, 13.1284f, 11.3491f, 14.4767f, 11.2079f)
          curveTo(15.8251f, 11.0666f, 17.1165f, 11.1961f, 18.351f, 11.5963f)
          curveTo(17.2447f, 10.8048f, 16.1298f, 10.391f, 15.0061f, 10.3549f)
          curveTo(13.8823f, 10.3187f, 12.7253f, 10.5734f, 11.535f, 11.1189f)
          lineTo(11.7904f, 9.73555f)
          curveTo(12.725f, 9.0389f, 13.5012f, 8.5889f, 14.1191f, 8.3854f)
          curveTo(14.7369f, 8.1819f, 15.8931f, 8.0712f, 17.5875f, 8.0534f)
          curveTo(16.5556f, 7.6295f, 15.6328f, 7.458f, 14.819f, 7.539f)
          curveTo(14.0052f, 7.6199f, 13.2169f, 7.8494f, 12.4539f, 8.2275f)
          curveTo(12.5952f, 7.7748f, 12.768f, 7.2763f, 12.9725f, 6.7322f)
          curveTo(13.177f, 6.1881f, 13.4991f, 5.3691f, 13.939f, 4.2752f)
          close()
        }
      }
    }.build()
    return _grass!!
  }
