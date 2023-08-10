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

private var _fairy: ImageVector? = null

val DeckBoxIcons.Types.Fairy: ImageVector
  get() {
    if (_fairy != null) {
      return _fairy!!
    }
    _fairy = ImageVector.Builder(
      name = "Types.Fairy",
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
          moveTo(5.00379f, 5.00349f)
          curveTo(3.379f, 9.5221f, 4.066f, 12.5106f, 7.0647f, 13.969f)
          curveTo(10.0634f, 15.4274f, 11.627f, 17.8665f, 11.7555f, 21.2863f)
          curveTo(8.8369f, 20.6618f, 6.5507f, 19.5239f, 4.897f, 17.8727f)
          curveTo(3.2434f, 16.2215f, 2.3548f, 14.1603f, 2.2315f, 11.6889f)
          curveTo(2.1929f, 10.3656f, 2.3937f, 9.1091f, 2.8339f, 7.9192f)
          curveTo(3.274f, 6.7294f, 3.9973f, 5.7575f, 5.0038f, 5.0035f)
          close()
          moveTo(19.1854f, 5.06024f)
          curveTo(20.1919f, 5.8142f, 20.9152f, 6.7862f, 21.3553f, 7.976f)
          curveTo(21.7955f, 9.1658f, 21.9963f, 10.4224f, 21.9577f, 11.7457f)
          curveTo(21.8344f, 14.217f, 20.9458f, 16.2783f, 19.2922f, 17.9295f)
          curveTo(17.6385f, 19.5806f, 15.3523f, 20.7185f, 12.4337f, 21.3431f)
          curveTo(12.5622f, 17.9232f, 14.1258f, 15.4841f, 17.1245f, 14.0258f)
          curveTo(20.1232f, 12.5674f, 20.8102f, 9.5789f, 19.1854f, 5.0602f)
          close()
          moveTo(12.0798f, 2.51987f)
          curveTo(12.6516f, 4.2024f, 13.2372f, 5.2716f, 13.8366f, 5.7275f)
          curveTo(14.436f, 6.1834f, 15.7273f, 6.3395f, 17.7105f, 6.1958f)
          curveTo(15.6129f, 7.7018f, 14.3091f, 8.8698f, 13.7992f, 9.6999f)
          curveTo(13.2892f, 10.5301f, 12.699f, 12.4543f, 12.0286f, 15.4727f)
          curveTo(11.4053f, 12.3939f, 10.7526f, 10.379f, 10.0706f, 9.428f)
          curveTo(9.3886f, 8.4769f, 8.1473f, 7.3857f, 6.3466f, 6.1543f)
          curveTo(8.2872f, 6.2968f, 9.5702f, 6.169f, 10.1958f, 5.771f)
          curveTo(10.8213f, 5.373f, 11.4493f, 4.2893f, 12.0798f, 2.5199f)
          close()
        }
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
          moveTo(24f, 0f)
          horizontalLineTo(0f)
          verticalLineTo(24f)
          horizontalLineTo(24f)
          verticalLineTo(0f)
          close()
        }
      }
    }.build()
    return _fairy!!
  }
