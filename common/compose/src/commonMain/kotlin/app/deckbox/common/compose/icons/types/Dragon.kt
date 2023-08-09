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


private var _dragon: ImageVector? = null

val DeckBoxIcons.Types.Dragon: ImageVector
  get() {
    if (_dragon != null) {
      return _dragon!!
    }
    _dragon = ImageVector.Builder(
      name = "Types.Dragon",
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
          moveTo(9.77463f, 7.50001f)
          lineTo(20.949f, 17.7115f)
          curveTo(20.0752f, 18.8466f, 19.1792f, 19.7137f, 18.2612f, 20.3128f)
          curveTo(17.3432f, 20.9119f, 16.0652f, 21.2694f, 14.4273f, 21.3855f)
          curveTo(11.1546f, 21.2816f, 8.341f, 19.895f, 5.9864f, 17.2258f)
          curveTo(3.6319f, 14.5565f, 2.4363f, 11.8247f, 2.3998f, 9.0302f)
          curveTo(2.3642f, 7.2251f, 2.9967f, 5.7011f, 4.2973f, 4.4581f)
          curveTo(5.5979f, 3.2152f, 7.3383f, 2.5534f, 9.5183f, 2.4727f)
          curveTo(10.8903f, 2.4277f, 12.2164f, 2.7138f, 13.4968f, 3.331f)
          curveTo(14.7772f, 3.9482f, 15.7868f, 4.7519f, 16.5255f, 5.7421f)
          curveTo(13.2912f, 4.2859f, 10.8901f, 3.9787f, 9.322f, 4.8207f)
          curveTo(7.754f, 5.6627f, 6.9756f, 6.8705f, 6.9869f, 8.4442f)
          curveTo(6.9869f, 9.9994f, 7.5079f, 11.4047f, 8.55f, 12.6601f)
          curveTo(9.5921f, 13.9155f, 10.6335f, 14.679f, 11.6744f, 14.9506f)
          lineTo(9.77463f, 7.50001f)
          close()
          moveTo(13.2331f, 13.2976f)
          lineTo(13.7256f, 15.3461f)
          lineTo(16.13f, 16.0007f)
          lineTo(13.2331f, 13.2976f)
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
    return _dragon!!
  }

