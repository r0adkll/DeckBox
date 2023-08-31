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


private var _vector: ImageVector? = null

val DeckBoxIcons.TiledTypeIcons: ImageVector
  get() {
    if (_vector != null) {
      return _vector!!
    }
    _vector = ImageVector.Builder(
      name = "vector",
      defaultWidth = 384.dp,
      defaultHeight = 200.dp,
      viewportWidth = 384f,
      viewportHeight = 200f
    ).apply {
      group {
        group {
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(248f, 136f)
              horizontalLineTo(200f)
              verticalLineTo(184f)
              horizontalLineTo(248f)
              verticalLineTo(136f)
              close()
            }
          }
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(120f, 8f)
              horizontalLineTo(72f)
              verticalLineTo(56f)
              horizontalLineTo(120f)
              verticalLineTo(8f)
              close()
            }
          }
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(120f, 136f)
              horizontalLineTo(72f)
              verticalLineTo(184f)
              horizontalLineTo(120f)
              verticalLineTo(136f)
              close()
            }
          }
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(376f, 136f)
              horizontalLineTo(328f)
              verticalLineTo(184f)
              horizontalLineTo(376f)
              verticalLineTo(136f)
              close()
            }
          }
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(376f, 8f)
              horizontalLineTo(328f)
              verticalLineTo(56f)
              horizontalLineTo(376f)
              verticalLineTo(8f)
              close()
            }
          }
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(216f, 72f)
              horizontalLineTo(168f)
              verticalLineTo(120f)
              horizontalLineTo(216f)
              verticalLineTo(72f)
              close()
            }
          }
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(248f, 8f)
              horizontalLineTo(200f)
              verticalLineTo(56f)
              horizontalLineTo(248f)
              verticalLineTo(8f)
              close()
            }
          }
          path(
            fill = SolidColor(Color(0xFF000000)),
            fillAlpha = 0.149809f,
            stroke = null,
            strokeAlpha = 1.0f,
            strokeLineWidth = 1.0f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
          ) {
            moveTo(71f, 75.3014f)
            curveTo(79.881f, 86.4503f, 83.642f, 95.0724f, 82.2831f, 101.168f)
            curveTo(80.9242f, 107.263f, 74.4957f, 110.606f, 62.9977f, 111.196f)
            lineTo(63.5821f, 116.656f)
            lineTo(58.1358f, 115.753f)
            lineTo(56.8637f, 110.241f)
            curveTo(47.1481f, 106.963f, 43.8604f, 101.304f, 47.0007f, 93.2636f)
            curveTo(50.1409f, 85.2229f, 58.1407f, 79.2355f, 71f, 75.3014f)
            close()
            moveTo(67.878f, 80.5505f)
            curveTo(66.455f, 82.4574f, 65.4308f, 83.9038f, 64.8057f, 84.89f)
            curveTo(64.1805f, 85.8761f, 63.5343f, 87.0645f, 62.867f, 88.4551f)
            curveTo(61.6613f, 87.391f, 60.5239f, 86.687f, 59.4545f, 86.3428f)
            curveTo(58.3852f, 85.9987f, 57.164f, 85.9539f, 55.7909f, 86.2084f)
            curveTo(57.6738f, 86.9614f, 58.9509f, 87.6249f, 59.6223f, 88.1992f)
            curveTo(60.2937f, 88.7734f, 60.9436f, 89.7096f, 61.5721f, 91.0076f)
            lineTo(60.4452f, 93.9921f)
            curveTo(59.0144f, 92.7683f, 57.5632f, 92.0561f, 56.0917f, 91.8555f)
            curveTo(54.6203f, 91.6549f, 53.0872f, 91.8081f, 51.4924f, 92.3151f)
            curveTo(53.4842f, 92.5254f, 55.1877f, 93.0123f, 56.6029f, 93.7757f)
            curveTo(58.0181f, 94.5392f, 58.9686f, 95.5448f, 59.4545f, 96.7927f)
            lineTo(58.7257f, 100.857f)
            curveTo(56.8695f, 99.766f, 55.1969f, 99.1002f, 53.7079f, 98.8599f)
            curveTo(52.2188f, 98.6196f, 50.7955f, 98.8101f, 49.4378f, 99.4312f)
            curveTo(51.2424f, 99.5042f, 52.9755f, 99.9795f, 54.637f, 100.857f)
            curveTo(56.2986f, 101.735f, 57.5629f, 102.936f, 58.4301f, 104.459f)
            curveTo(58.4121f, 106.446f, 58.5107f, 107.904f, 58.7257f, 108.832f)
            curveTo(58.9407f, 109.761f, 59.3898f, 110.668f, 60.073f, 111.555f)
            curveTo(60.1375f, 110.509f, 60.1938f, 109.602f, 60.2416f, 108.832f)
            curveTo(60.2895f, 108.063f, 60.3938f, 107.02f, 60.5545f, 105.702f)
            curveTo(62.4547f, 104.021f, 64.5616f, 102.82f, 66.8752f, 102.097f)
            curveTo(69.1888f, 101.374f, 72.1234f, 101.247f, 75.679f, 101.716f)
            curveTo(73.2476f, 100.622f, 70.8655f, 100.134f, 68.5327f, 100.252f)
            curveTo(66.1998f, 100.371f, 63.8025f, 100.932f, 61.3405f, 101.934f)
            lineTo(61.797f, 97.5643f)
            curveTo(63.8713f, 95.7477f, 66.2568f, 94.6982f, 68.9535f, 94.4158f)
            curveTo(71.6501f, 94.1333f, 74.233f, 94.3923f, 76.702f, 95.1927f)
            curveTo(74.4895f, 93.6097f, 72.2595f, 92.7821f, 70.0121f, 92.7098f)
            curveTo(67.7647f, 92.6375f, 65.4506f, 93.1469f, 63.0699f, 94.238f)
            lineTo(63.5807f, 91.4712f)
            curveTo(65.45f, 90.078f, 67.0025f, 89.1779f, 68.2382f, 88.7709f)
            curveTo(69.4739f, 88.3639f, 71.7862f, 88.1425f, 75.175f, 88.1068f)
            curveTo(73.1112f, 87.259f, 71.2655f, 86.9161f, 69.638f, 87.078f)
            curveTo(68.0104f, 87.2399f, 66.4337f, 87.6989f, 64.9078f, 88.4551f)
            curveTo(65.1903f, 87.5496f, 65.5361f, 86.5528f, 65.945f, 85.4645f)
            curveTo(66.3539f, 84.3763f, 66.9983f, 82.7383f, 67.878f, 80.5505f)
            close()
          }
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(88f, 72.0001f)
              horizontalLineTo(40f)
              verticalLineTo(120f)
              horizontalLineTo(88f)
              verticalLineTo(72.0001f)
              close()
            }
          }
          path(
            fill = SolidColor(Color(0xFF000000)),
            fillAlpha = 0.149809f,
            stroke = null,
            strokeAlpha = 1.0f,
            strokeLineWidth = 1.0f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
          ) {
            moveTo(398.371f, 82.1205f)
            curveTo(400.384f, 83.6285f, 401.83f, 85.5723f, 402.711f, 87.952f)
            curveTo(403.591f, 90.3317f, 403.993f, 92.8448f, 403.915f, 95.4914f)
            curveTo(403.669f, 100.434f, 401.892f, 104.557f, 398.584f, 107.859f)
            curveTo(395.277f, 111.161f, 390.705f, 113.437f, 384.867f, 114.686f)
            curveTo(385.124f, 107.846f, 388.252f, 102.968f, 394.249f, 100.052f)
            curveTo(400.246f, 97.1348f, 401.62f, 91.1578f, 398.371f, 82.1205f)
            close()
            moveTo(370.008f, 82.007f)
            curveTo(366.758f, 91.0442f, 368.132f, 97.0213f, 374.129f, 99.938f)
            curveTo(380.127f, 102.855f, 383.254f, 107.733f, 383.511f, 114.573f)
            curveTo(377.674f, 113.324f, 373.101f, 111.048f, 369.794f, 107.745f)
            curveTo(366.487f, 104.443f, 364.71f, 100.321f, 364.463f, 95.3779f)
            curveTo(364.386f, 92.7313f, 364.788f, 90.2182f, 365.668f, 87.8385f)
            curveTo(366.548f, 85.4588f, 367.995f, 83.515f, 370.008f, 82.007f)
            close()
            moveTo(384.16f, 77.0397f)
            curveTo(385.303f, 80.4047f, 386.475f, 82.5431f, 387.673f, 83.4549f)
            curveTo(388.872f, 84.3668f, 391.455f, 84.679f, 395.421f, 84.3916f)
            curveTo(391.226f, 87.4035f, 388.618f, 89.7396f, 387.598f, 91.3999f)
            curveTo(386.578f, 93.0602f, 385.398f, 96.9087f, 384.057f, 102.945f)
            curveTo(382.811f, 96.7878f, 381.505f, 92.758f, 380.141f, 90.8559f)
            curveTo(378.777f, 88.9538f, 376.295f, 86.7714f, 372.693f, 84.3087f)
            curveTo(376.574f, 84.5936f, 379.141f, 84.338f, 380.392f, 83.542f)
            curveTo(381.643f, 82.7459f, 382.899f, 80.5785f, 384.16f, 77.0397f)
            close()
          }
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(408f, 72f)
              horizontalLineTo(360f)
              verticalLineTo(120f)
              horizontalLineTo(408f)
              verticalLineTo(72f)
              close()
            }
          }
          path(
            fill = SolidColor(Color(0xFF000000)),
            fillAlpha = 0.149809f,
            stroke = null,
            strokeAlpha = 1.0f,
            strokeLineWidth = 1.0f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
          ) {
            moveTo(14.3709f, 82.1205f)
            curveTo(16.3837f, 83.6285f, 17.8303f, 85.5723f, 18.7106f, 87.952f)
            curveTo(19.591f, 90.3317f, 19.9926f, 92.8448f, 19.9155f, 95.4914f)
            curveTo(19.6688f, 100.434f, 17.8917f, 104.557f, 14.5844f, 107.859f)
            curveTo(11.277f, 111.161f, 6.7047f, 113.437f, 0.8675f, 114.686f)
            curveTo(1.1245f, 107.846f, 4.2517f, 102.968f, 10.2491f, 100.052f)
            curveTo(16.2465f, 97.1348f, 17.6204f, 91.1578f, 14.3709f, 82.1205f)
            close()
            moveTo(-13.9924f, 82.007f)
            curveTo(-17.2419f, 91.0442f, -15.868f, 97.0213f, -9.8706f, 99.938f)
            curveTo(-3.8732f, 102.855f, -0.746f, 107.733f, -0.489f, 114.573f)
            curveTo(-6.3262f, 113.324f, -10.8985f, 111.048f, -14.2059f, 107.745f)
            curveTo(-17.5132f, 104.443f, -19.2903f, 100.321f, -19.537f, 95.3779f)
            curveTo(-19.6141f, 92.7313f, -19.2125f, 90.2182f, -18.3322f, 87.8385f)
            curveTo(-17.4519f, 85.4588f, -16.0053f, 83.515f, -13.9924f, 82.007f)
            close()
            moveTo(0.159712f, 77.0397f)
            curveTo(1.3033f, 80.4047f, 2.4745f, 82.5431f, 3.6733f, 83.4549f)
            curveTo(4.872f, 84.3668f, 7.4546f, 84.679f, 11.4211f, 84.3916f)
            curveTo(7.2258f, 87.4035f, 4.6182f, 89.7396f, 3.5984f, 91.3999f)
            curveTo(2.5785f, 93.0602f, 1.3981f, 96.9087f, 0.0572f, 102.945f)
            curveTo(-1.1894f, 96.7878f, -2.4947f, 92.758f, -3.8587f, 90.8559f)
            curveTo(-5.2227f, 88.9538f, -7.7054f, 86.7714f, -11.3067f, 84.3087f)
            curveTo(-7.4256f, 84.5936f, -4.8595f, 84.338f, -3.6084f, 83.542f)
            curveTo(-2.3574f, 82.7459f, -1.1013f, 80.5785f, 0.1597f, 77.0397f)
            close()
          }
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(24f, 72f)
              horizontalLineTo(-24f)
              verticalLineTo(120f)
              horizontalLineTo(24f)
              verticalLineTo(72f)
              close()
            }
          }
          path(
            fill = SolidColor(Color(0xFF000000)),
            fillAlpha = 0.149809f,
            stroke = null,
            strokeAlpha = 1.0f,
            strokeLineWidth = 1.0f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
          ) {
            moveTo(29.2453f, 11.5866f)
            curveTo(32.5859f, 12.4952f, 35.0799f, 14.6357f, 36.7274f, 18.008f)
            curveTo(38.3748f, 21.3803f, 38.9345f, 25.7412f, 38.4064f, 31.0908f)
            curveTo(41.0864f, 30.8462f, 42.978f, 30.3919f, 44.081f, 29.7277f)
            curveTo(45.184f, 29.0636f, 46.3951f, 27.6498f, 47.7144f, 25.4863f)
            curveTo(49.1491f, 27.6133f, 49.503f, 29.7846f, 48.7763f, 32f)
            curveTo(48.0497f, 34.2155f, 45.7798f, 36.6435f, 41.9669f, 39.284f)
            curveTo(44.2054f, 39.0836f, 45.9163f, 38.7168f, 47.0995f, 38.1837f)
            curveTo(48.2827f, 37.6506f, 49.8852f, 36.5245f, 51.907f, 34.8053f)
            curveTo(50.3522f, 40.4043f, 47.8098f, 44.6959f, 44.2797f, 47.6801f)
            curveTo(40.7496f, 50.6644f, 36.6564f, 52.2038f, 32f, 52.2983f)
            curveTo(27.5535f, 52.2983f, 23.5734f, 51.1272f, 20.0597f, 48.7849f)
            curveTo(16.546f, 46.4426f, 14.1007f, 42.8262f, 12.7239f, 37.9356f)
            curveTo(14.1502f, 39.0245f, 15.2249f, 39.789f, 15.9482f, 40.2292f)
            curveTo(16.6714f, 40.6694f, 17.6749f, 41.1698f, 18.9586f, 41.7303f)
            curveTo(16.1433f, 37.9259f, 14.8471f, 34.3794f, 15.0699f, 31.0908f)
            curveTo(15.2928f, 27.8021f, 17.1672f, 24.8595f, 20.6933f, 22.2628f)
            curveTo(19.2237f, 24.9245f, 18.8194f, 27.1655f, 19.4804f, 28.9861f)
            curveTo(20.1413f, 30.8066f, 21.3061f, 32.2147f, 22.9748f, 33.2105f)
            curveTo(21.7083f, 31.1188f, 21.4594f, 29.1421f, 22.2281f, 27.2802f)
            curveTo(24.599f, 21.5376f, 29.3215f, 20.3483f, 30.9347f, 18.008f)
            curveTo(32.0103f, 16.4478f, 31.4471f, 14.3073f, 29.2453f, 11.5866f)
            close()
            moveTo(33.0394f, 33f)
            curveTo(31.1221f, 33.3819f, 29.8265f, 33.8038f, 29.1527f, 34.2655f)
            curveTo(28.142f, 34.9581f, 26.917f, 35.8461f, 26.917f, 37.2352f)
            curveTo(26.917f, 38.6243f, 29.3664f, 39.8656f, 29.1527f, 41.2718f)
            curveTo(29.0102f, 42.2092f, 28.554f, 42.9983f, 27.7842f, 43.639f)
            curveTo(27.6377f, 42.6438f, 27.2106f, 41.9213f, 26.5028f, 41.4716f)
            curveTo(25.7951f, 41.0219f, 24.8184f, 40.8027f, 23.5727f, 40.814f)
            curveTo(25.0185f, 41.5374f, 25.6263f, 42.3327f, 25.3963f, 43.2f)
            curveTo(25.0513f, 44.5009f, 23.5727f, 45.2708f, 23.5727f, 46.6294f)
            curveTo(23.5727f, 47.9881f, 25.5966f, 50.8613f, 32f, 50.8613f)
            curveTo(38.4035f, 50.8613f, 38.9679f, 48.7278f, 38.9679f, 47.3256f)
            curveTo(38.9679f, 45.9234f, 37.4106f, 44.7061f, 37.4106f, 42.6466f)
            curveTo(37.4106f, 41.2736f, 38.0584f, 40.1102f, 39.3541f, 39.1563f)
            curveTo(38.2269f, 39.1689f, 37.2448f, 39.4053f, 36.4078f, 39.8656f)
            curveTo(35.5709f, 40.3259f, 34.8275f, 41.0861f, 34.1777f, 42.1462f)
            curveTo(34.0965f, 40.4269f, 33.9052f, 39.3077f, 33.6039f, 38.7885f)
            curveTo(32.9071f, 37.588f, 31.2566f, 37.0549f, 31.2566f, 35.9723f)
            curveTo(31.2566f, 35.2507f, 31.8509f, 34.2599f, 33.0394f, 33f)
            close()
          }
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(56.0001f, 8f)
              horizontalLineTo(8.00006f)
              verticalLineTo(56f)
              horizontalLineTo(56.0001f)
              verticalLineTo(8f)
              close()
            }
          }
          path(
            fill = SolidColor(Color(0xFF000000)),
            fillAlpha = 0.149809f,
            stroke = null,
            strokeAlpha = 1.0f,
            strokeLineWidth = 1.0f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
          ) {
            moveTo(303.981f, 144.114f)
            curveTo(296.934f, 146.012f, 293.055f, 148.773f, 292.343f, 152.397f)
            curveTo(291.409f, 157.155f, 296.053f, 160.068f, 299.316f, 165.145f)
            curveTo(300.134f, 166.417f, 300.646f, 168.099f, 300.852f, 170.189f)
            curveTo(301.057f, 173.455f, 299.249f, 175.955f, 295.428f, 177.691f)
            curveTo(291.606f, 179.426f, 286.377f, 179.188f, 279.742f, 176.975f)
            curveTo(276.792f, 175.658f, 274.48f, 173.957f, 272.806f, 171.872f)
            curveTo(271.132f, 169.786f, 270.245f, 167.16f, 270.147f, 163.993f)
            curveTo(270.182f, 158.372f, 273.132f, 153.805f, 278.998f, 150.293f)
            curveTo(284.865f, 146.78f, 293.192f, 144.721f, 303.981f, 144.114f)
            close()
            moveTo(277.928f, 171.701f)
            curveTo(277.502f, 173.313f, 280.635f, 175.195f, 282.337f, 175.778f)
            curveTo(284.039f, 176.362f, 287.768f, 177.232f, 288.24f, 175.778f)
            curveTo(288.711f, 174.325f, 286.196f, 172.381f, 284.278f, 171.701f)
            curveTo(282.359f, 171.021f, 278.353f, 170.09f, 277.928f, 171.701f)
            close()
          }
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(312f, 136f)
              horizontalLineTo(264f)
              verticalLineTo(184f)
              horizontalLineTo(312f)
              verticalLineTo(136f)
              close()
            }
          }
          path(
            fill = SolidColor(Color(0xFF000000)),
            fillAlpha = 0.149809f,
            stroke = null,
            strokeAlpha = 1.0f,
            strokeLineWidth = 1.0f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
          ) {
            moveTo(35.652f, 139.03f)
            lineTo(16.8282f, 165.63f)
            lineTo(32.6699f, 161.672f)
            lineTo(28.2209f, 181.62f)
            lineTo(46.2978f, 150.931f)
            lineTo(32f, 154.329f)
            lineTo(35.652f, 139.03f)
            close()
          }
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(56.0001f, 136f)
              horizontalLineTo(8.00006f)
              verticalLineTo(184f)
              horizontalLineTo(56.0001f)
              verticalLineTo(136f)
              close()
            }
          }
          path(
            fill = SolidColor(Color(0xFF000000)),
            fillAlpha = 0.149809f,
            stroke = null,
            strokeAlpha = 1.0f,
            strokeLineWidth = 1.0f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
          ) {
            moveTo(256.276f, 75.6255f)
            curveTo(257.304f, 82.3798f, 258.741f, 86.3605f, 260.585f, 87.5676f)
            curveTo(262.429f, 88.7747f, 266.938f, 88.1617f, 274.113f, 85.7287f)
            curveTo(268.042f, 90.8016f, 265.006f, 94.2254f, 265.006f, 96f)
            curveTo(265.006f, 97.7747f, 268.042f, 101.054f, 274.113f, 105.838f)
            curveTo(266.812f, 103.547f, 262.302f, 102.94f, 260.585f, 104.017f)
            curveTo(258.867f, 105.094f, 257.431f, 109.045f, 256.276f, 115.87f)
            curveTo(255.338f, 109.111f, 253.852f, 105.16f, 251.816f, 104.017f)
            curveTo(249.78f, 102.874f, 245.308f, 103.481f, 238.402f, 105.838f)
            curveTo(244.521f, 101.055f, 247.581f, 97.7755f, 247.581f, 96f)
            curveTo(247.581f, 94.2245f, 244.521f, 90.8008f, 238.402f, 85.7287f)
            curveTo(245.918f, 88.1805f, 250.487f, 88.7935f, 252.107f, 87.5676f)
            curveTo(253.727f, 86.3417f, 255.117f, 82.361f, 256.276f, 75.6255f)
            close()
          }
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(280f, 72.0001f)
              horizontalLineTo(232f)
              verticalLineTo(120f)
              horizontalLineTo(280f)
              verticalLineTo(72.0001f)
              close()
            }
          }
          path(
            fill = SolidColor(Color(0xFF000000)),
            fillAlpha = 0.149809f,
            stroke = null,
            strokeAlpha = 1.0f,
            strokeLineWidth = 1.0f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
          ) {
            moveTo(327.104f, 106.287f)
            lineTo(329.651f, 110.243f)
            lineTo(325.866f, 115.837f)
            horizontalLineTo(314.262f)
            lineTo(310.671f, 110.243f)
            lineTo(313.205f, 106.287f)
            lineTo(316.655f, 111.965f)
            horizontalLineTo(323.496f)
            lineTo(327.104f, 106.287f)
            close()
            moveTo(329.651f, 89.7862f)
            lineTo(320f, 106.758f)
            lineTo(310.102f, 89.7862f)
            horizontalLineTo(329.651f)
            close()
            moveTo(312.543f, 80.92f)
            lineTo(315.147f, 85.3158f)
            horizontalLineTo(308.183f)
            lineTo(304.769f, 91.5866f)
            lineTo(307.673f, 97.1306f)
            horizontalLineTo(303.023f)
            lineTo(300.2f, 91.5866f)
            lineTo(306.078f, 81.5478f)
            lineTo(312.543f, 80.92f)
            close()
            moveTo(327.827f, 80.9133f)
            lineTo(334.292f, 81.541f)
            lineTo(340.17f, 91.5799f)
            lineTo(337.347f, 97.1238f)
            horizontalLineTo(332.697f)
            lineTo(335.601f, 91.5799f)
            lineTo(332.187f, 85.3091f)
            horizontalLineTo(325.223f)
            lineTo(327.827f, 80.9133f)
            close()
          }
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(344f, 72f)
              horizontalLineTo(296f)
              verticalLineTo(120f)
              horizontalLineTo(344f)
              verticalLineTo(72f)
              close()
            }
          }
          path(
            fill = SolidColor(Color(0xFF000000)),
            fillAlpha = 0.149809f,
            stroke = null,
            strokeAlpha = 1.0f,
            strokeLineWidth = 1.0f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
          ) {
            moveTo(288f, 18.1043f)
            curveTo(296.936f, 18.2714f, 303.97f, 23.6721f, 309.102f, 34.3067f)
            curveTo(303.761f, 43.8825f, 296.726f, 48.6705f, 288f, 48.6705f)
            curveTo(279.274f, 48.6705f, 272.198f, 43.8825f, 266.774f, 34.3067f)
            curveTo(271.989f, 23.3381f, 279.064f, 17.9373f, 288f, 18.1043f)
            close()
            moveTo(291.688f, 22.443f)
            lineTo(291.84f, 22.5055f)
            curveTo(295.519f, 24.0615f, 298.1f, 27.7043f, 298.1f, 31.95f)
            curveTo(298.1f, 37.6109f, 293.511f, 42.2f, 287.85f, 42.2f)
            curveTo(282.189f, 42.2f, 277.6f, 37.6109f, 277.6f, 31.95f)
            curveTo(277.6f, 27.847f, 280.011f, 24.3071f, 283.493f, 22.6694f)
            curveTo(278.797f, 24.1152f, 274.661f, 27.9046f, 271.086f, 34.0375f)
            curveTo(275.764f, 41.1202f, 281.403f, 44.6616f, 288f, 44.6616f)
            curveTo(294.598f, 44.6616f, 300.205f, 41.1202f, 304.822f, 34.0375f)
            curveTo(301.091f, 27.5519f, 296.712f, 23.6871f, 291.688f, 22.443f)
            close()
            moveTo(287.7f, 26.7f)
            curveTo(287.255f, 26.7f, 286.823f, 26.7583f, 286.412f, 26.8676f)
            curveTo(287.368f, 27.4924f, 288f, 28.5724f, 288f, 29.8f)
            curveTo(288f, 31.733f, 286.433f, 33.3f, 284.5f, 33.3f)
            curveTo(283.9f, 33.3f, 283.335f, 33.1489f, 282.841f, 32.8826f)
            curveTo(283.372f, 35.0734f, 285.346f, 36.7f, 287.7f, 36.7f)
            curveTo(290.461f, 36.7f, 292.7f, 34.4614f, 292.7f, 31.7f)
            curveTo(292.7f, 28.9386f, 290.461f, 26.7f, 287.7f, 26.7f)
            close()
          }
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(312f, 8f)
              horizontalLineTo(264f)
              verticalLineTo(56f)
              horizontalLineTo(312f)
              verticalLineTo(8f)
              close()
            }
          }
          path(
            fill = SolidColor(Color(0xFF000000)),
            fillAlpha = 0.149809f,
            stroke = null,
            strokeAlpha = 1.0f,
            strokeLineWidth = 1.0f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
          ) {
            moveTo(174.371f, 18.1205f)
            curveTo(176.384f, 19.6285f, 177.83f, 21.5723f, 178.711f, 23.952f)
            curveTo(179.591f, 26.3317f, 179.993f, 28.8448f, 179.915f, 31.4914f)
            curveTo(179.669f, 36.434f, 177.892f, 40.5566f, 174.584f, 43.8589f)
            curveTo(171.277f, 47.1613f, 166.705f, 49.437f, 160.867f, 50.6861f)
            curveTo(161.124f, 43.8465f, 164.252f, 38.9682f, 170.249f, 36.0515f)
            curveTo(176.246f, 33.1348f, 177.62f, 27.1578f, 174.371f, 18.1205f)
            close()
            moveTo(146.008f, 18.007f)
            curveTo(142.758f, 27.0442f, 144.132f, 33.0213f, 150.129f, 35.938f)
            curveTo(156.127f, 38.8547f, 159.254f, 43.7329f, 159.511f, 50.5726f)
            curveTo(153.674f, 49.3235f, 149.101f, 47.0478f, 145.794f, 43.7454f)
            curveTo(142.487f, 40.443f, 140.71f, 36.3205f, 140.463f, 31.3779f)
            curveTo(140.386f, 28.7313f, 140.788f, 26.2182f, 141.668f, 23.8385f)
            curveTo(142.548f, 21.4588f, 143.995f, 19.515f, 146.008f, 18.007f)
            close()
            moveTo(160.16f, 13.0397f)
            curveTo(161.303f, 16.4047f, 162.475f, 18.5431f, 163.673f, 19.4549f)
            curveTo(164.872f, 20.3668f, 167.455f, 20.679f, 171.421f, 20.3916f)
            curveTo(167.226f, 23.4035f, 164.618f, 25.7396f, 163.598f, 27.3999f)
            curveTo(162.578f, 29.0602f, 161.398f, 32.9087f, 160.057f, 38.9454f)
            curveTo(158.811f, 32.7878f, 157.505f, 28.758f, 156.141f, 26.8559f)
            curveTo(154.777f, 24.9538f, 152.295f, 22.7714f, 148.693f, 20.3087f)
            curveTo(152.574f, 20.5936f, 155.141f, 20.338f, 156.392f, 19.542f)
            curveTo(157.643f, 18.7459f, 158.899f, 16.5785f, 160.16f, 13.0397f)
            close()
          }
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(184f, 8f)
              horizontalLineTo(136f)
              verticalLineTo(56f)
              horizontalLineTo(184f)
              verticalLineTo(8f)
              close()
            }
          }
          path(
            fill = SolidColor(Color(0xFF000000)),
            fillAlpha = 0.149809f,
            stroke = null,
            strokeAlpha = 1.0f,
            strokeLineWidth = 1.0f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
          ) {
            moveTo(162.994f, 142.662f)
            curveTo(165.555f, 143.896f, 167.574f, 145.504f, 169.051f, 147.484f)
            curveTo(162.583f, 144.572f, 157.78f, 143.957f, 154.644f, 145.641f)
            curveTo(151.508f, 147.325f, 149.951f, 149.741f, 149.974f, 152.888f)
            curveTo(149.974f, 155.999f, 151.016f, 158.809f, 153.1f, 161.32f)
            curveTo(155.184f, 163.831f, 157.267f, 165.358f, 159.349f, 165.901f)
            lineTo(155.549f, 151f)
            lineTo(177.898f, 171.423f)
            curveTo(176.15f, 173.693f, 174.359f, 175.427f, 172.522f, 176.626f)
            curveTo(170.686f, 177.824f, 168.13f, 178.539f, 164.855f, 178.771f)
            curveTo(158.309f, 178.563f, 152.682f, 175.79f, 147.973f, 170.452f)
            curveTo(143.264f, 165.113f, 140.873f, 159.649f, 140.8f, 154.06f)
            curveTo(140.728f, 150.45f, 141.993f, 147.402f, 144.595f, 144.916f)
            curveTo(147.196f, 142.43f, 150.677f, 141.107f, 155.037f, 140.945f)
            curveTo(157.781f, 140.855f, 160.433f, 141.428f, 162.994f, 142.662f)
            close()
            moveTo(162.466f, 162.595f)
            lineTo(163.451f, 166.692f)
            lineTo(168.26f, 168.001f)
            lineTo(162.466f, 162.595f)
            close()
          }
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(184f, 136f)
              horizontalLineTo(136f)
              verticalLineTo(184f)
              horizontalLineTo(184f)
              verticalLineTo(136f)
              close()
            }
          }
          path(
            fill = SolidColor(Color(0xFF000000)),
            fillAlpha = 0.149809f,
            stroke = null,
            strokeAlpha = 1.0f,
            strokeLineWidth = 1.0f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
          ) {
            moveTo(132.675f, 80.7845f)
            curveTo(140.699f, 82.714f, 146.6f, 89.1982f, 146.6f, 96.9f)
            curveTo(146.6f, 106.123f, 138.138f, 113.6f, 127.7f, 113.6f)
            curveTo(117.262f, 113.6f, 108.8f, 106.123f, 108.8f, 96.9f)
            curveTo(108.8f, 89.1982f, 114.701f, 82.714f, 122.725f, 80.7845f)
            curveTo(120.94f, 82.2521f, 119.8f, 84.4912f, 119.8f, 87f)
            curveTo(119.8f, 91.4183f, 123.337f, 95f, 127.7f, 95f)
            curveTo(132.063f, 95f, 135.6f, 91.4183f, 135.6f, 87f)
            curveTo(135.6f, 84.4991f, 134.467f, 82.2662f, 132.692f, 80.7992f)
            lineTo(132.675f, 80.7845f)
            close()
          }
          group {
            path(
              fill = SolidColor(Color(0xFFFFFFFF)),
              fillAlpha = 0.149809f,
              stroke = null,
              strokeAlpha = 1.0f,
              strokeLineWidth = 1.0f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Miter,
              strokeLineMiter = 1.0f,
              pathFillType = PathFillType.NonZero
            ) {
              moveTo(152f, 72f)
              horizontalLineTo(104f)
              verticalLineTo(120f)
              horizontalLineTo(152f)
              verticalLineTo(72f)
              close()
            }
          }
        }
      }
    }.build()
    return _vector!!
  }

