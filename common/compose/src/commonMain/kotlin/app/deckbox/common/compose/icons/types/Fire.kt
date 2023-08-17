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

private var _fire: ImageVector? = null

val DeckBoxIcons.Types.Fire: ImageVector
  get() {
    if (_fire != null) {
      return _fire!!
    }
    _fire = ImageVector.Builder(
      name = "Types.Fire",
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
          moveTo(10.6226f, 1.7933f)
          curveTo(12.2929f, 2.2476f, 13.5399f, 3.3178f, 14.3637f, 5.004f)
          curveTo(15.1874f, 6.6901f, 15.4672f, 8.8706f, 15.2032f, 11.5454f)
          curveTo(16.5432f, 11.4231f, 17.489f, 11.1959f, 18.0405f, 10.8639f)
          curveTo(18.592f, 10.5318f, 19.1976f, 9.8249f, 19.8572f, 8.7431f)
          curveTo(20.5745f, 9.8067f, 20.7515f, 10.8923f, 20.3882f, 12f)
          curveTo(20.0248f, 13.1077f, 18.8899f, 14.3217f, 16.9834f, 15.642f)
          curveTo(18.1027f, 15.5418f, 18.9581f, 15.3584f, 19.5497f, 15.0919f)
          curveTo(20.1413f, 14.8253f, 20.9426f, 14.2622f, 21.9535f, 13.4027f)
          curveTo(21.1761f, 16.2021f, 19.9049f, 18.3479f, 18.1398f, 19.8401f)
          curveTo(16.3748f, 21.3322f, 14.3282f, 22.1019f, 12f, 22.1492f)
          curveTo(9.7767f, 22.1492f, 7.7867f, 21.5636f, 6.0298f, 20.3924f)
          curveTo(4.273f, 19.2213f, 3.0503f, 17.4131f, 2.3619f, 14.9678f)
          curveTo(3.0751f, 15.5122f, 3.6125f, 15.8945f, 3.9741f, 16.1146f)
          curveTo(4.3357f, 16.3347f, 4.8374f, 16.5849f, 5.4793f, 16.8652f)
          curveTo(4.0716f, 14.963f, 3.4235f, 13.1897f, 3.535f, 11.5454f)
          curveTo(3.6464f, 9.9011f, 4.5836f, 8.4298f, 6.3466f, 7.1314f)
          curveTo(5.6119f, 8.4622f, 5.4097f, 9.5828f, 5.7402f, 10.493f)
          curveTo(6.0706f, 11.4033f, 6.653f, 12.1074f, 7.4874f, 12.6052f)
          curveTo(6.8541f, 11.5594f, 6.7297f, 10.571f, 7.114f, 9.6401f)
          curveTo(8.2995f, 6.7688f, 10.6607f, 6.1742f, 11.4674f, 5.004f)
          curveTo(12.0051f, 4.2239f, 11.7235f, 3.1536f, 10.6226f, 1.7933f)
          close()
          moveTo(12.5197f, 12.5f)
          curveTo(11.561f, 12.691f, 10.9132f, 12.9019f, 10.5763f, 13.1327f)
          curveTo(10.071f, 13.479f, 9.4585f, 13.923f, 9.4585f, 14.6176f)
          curveTo(9.4585f, 15.3121f, 10.6832f, 15.9328f, 10.5763f, 16.6359f)
          curveTo(10.5051f, 17.1046f, 10.277f, 17.4991f, 9.8921f, 17.8195f)
          curveTo(9.8188f, 17.3219f, 9.6053f, 16.9607f, 9.2514f, 16.7358f)
          curveTo(8.8975f, 16.511f, 8.4092f, 16.4014f, 7.7864f, 16.407f)
          curveTo(8.5092f, 16.7687f, 8.8131f, 17.1663f, 8.6982f, 17.6f)
          curveTo(8.5257f, 18.2505f, 7.7864f, 18.6354f, 7.7864f, 19.3147f)
          curveTo(7.7864f, 19.994f, 8.7983f, 21.4307f, 12f, 21.4307f)
          curveTo(15.2017f, 21.4307f, 15.4839f, 20.3639f, 15.4839f, 19.6628f)
          curveTo(15.4839f, 18.9617f, 14.7053f, 18.353f, 14.7053f, 17.3233f)
          curveTo(14.7053f, 16.6368f, 15.0292f, 16.0551f, 15.677f, 15.5782f)
          curveTo(15.1134f, 15.5844f, 14.6224f, 15.7027f, 14.2039f, 15.9328f)
          curveTo(13.7854f, 16.1629f, 13.4137f, 16.543f, 13.0889f, 17.0731f)
          curveTo(13.0483f, 16.2135f, 12.9526f, 15.6539f, 12.8019f, 15.3943f)
          curveTo(12.4535f, 14.794f, 11.6283f, 14.5274f, 11.6283f, 13.9862f)
          curveTo(11.6283f, 13.6253f, 11.9254f, 13.1299f, 12.5197f, 12.5f)
          close()
        }
      }
    }.build()
    return _fire!!
  }
