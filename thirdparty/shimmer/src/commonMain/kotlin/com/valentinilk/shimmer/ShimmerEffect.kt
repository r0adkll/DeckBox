package com.valentinilk.shimmer

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.platform.LocalDensity

@Composable
internal fun rememberShimmerEffect(theme: ShimmerTheme): ShimmerEffect {
  val shimmerWidth = with(LocalDensity.current) { theme.shimmerWidth.toPx() }
  val shimmerEffect = remember(theme) {
    ShimmerEffectFactory.create(
      animationSpec = theme.animationSpec,
      blendMode = theme.blendMode,
      rotation = theme.rotation,
      shaderColors = theme.shaderColors,
      shaderColorStops = theme.shaderColorStops,
      shimmerWidth = shimmerWidth,
    )
  }

  LaunchedEffect(shimmerEffect) {
    shimmerEffect.startAnimation()
  }
  return shimmerEffect
}

internal expect object ShimmerEffectFactory {
  fun create(
    animationSpec: AnimationSpec<Float>,
    blendMode: BlendMode,
    rotation: Float,
    shaderColors: List<Color>,
    shaderColorStops: List<Float>?,
    shimmerWidth: Float,
  ) : ShimmerEffect
}

internal interface ShimmerEffect {
  val animationSpec: AnimationSpec<Float>
  val blendMode: BlendMode
  val rotation: Float
  val shaderColors: List<Color>
  val shaderColorStops: List<Float>?
  val shimmerWidth: Float

  suspend fun startAnimation()
  fun ContentDrawScope.draw(shimmerArea: ShimmerArea)
}

internal data class DefaultShimmerEffect(
  override val animationSpec: AnimationSpec<Float>,
  override val blendMode: BlendMode,
  override val rotation: Float,
  override val shaderColors: List<Color>,
  override val shaderColorStops: List<Float>?,
  override val shimmerWidth: Float,
) : ShimmerEffect {

  private val animatedState = Animatable(0f)

  private val paint = Paint().apply {
    isAntiAlias = true
    style = PaintingStyle.Fill
    blendMode = this@DefaultShimmerEffect.blendMode
  }

  override suspend fun startAnimation() {
    animatedState.animateTo(
      targetValue = 1f,
      animationSpec = animationSpec,
    )
  }

  private val emptyPaint = Paint()

  override fun ContentDrawScope.draw(shimmerArea: ShimmerArea) = with(shimmerArea) {
    if (shimmerBounds.isEmpty || viewBounds.isEmpty) return

    val progress = animatedState.value
    val traversal = -translationDistance / 2 + translationDistance * progress + pivotPoint.x

    paint.shader = LinearGradientShader(
      from = Offset(-shimmerWidth / 2, 0f) + Offset(traversal, 0f),
      to = Offset(shimmerWidth / 2, 0f) + Offset(traversal, 0f),
      colors = shaderColors,
      colorStops = shaderColorStops,
    )

    val drawArea = size.toRect()
    drawIntoCanvas { canvas ->
      canvas.withSaveLayer(
        bounds = drawArea,
        emptyPaint,
      ) {
        drawContent()
        canvas.drawRect(drawArea, paint)
      }
    }
  }
}
