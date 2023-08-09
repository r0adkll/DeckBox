package app.deckbox.common.compose.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import dev.sasikanth.material.color.utilities.dynamiccolor.MaterialDynamicColors
import dev.sasikanth.material.color.utilities.hct.Hct
import dev.sasikanth.material.color.utilities.quantize.QuantizerCelebi
import dev.sasikanth.material.color.utilities.scheme.SchemeContent
import dev.sasikanth.material.color.utilities.score.Score
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class Palette(
  val primary: Int,
  val secondary: Int,
)

suspend fun generateColorPalette(
  imageBitmap: ImageBitmap,
  dispatcher: CoroutineDispatcher = Dispatchers.Default,
): Palette = withContext(dispatcher) {
  val seedColor = Score.score(QuantizerCelebi.quantize(imageBitmap.toPixelMap().buffer, 3))[0]

  val scheme = SchemeContent(
    sourceColorHct = Hct.fromInt(seedColor),
    isDark = true,
    contrastLevel = 0.0,
  )

  val dynamicColors = MaterialDynamicColors()
  Palette(
    primary = dynamicColors.primary().getArgb(scheme),
    secondary = dynamicColors.secondary().getArgb(scheme),
  )
}
