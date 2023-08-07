package com.valentinilk.shimmer

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color

internal actual object ShimmerEffectFactory {
  actual fun create(
    animationSpec: AnimationSpec<Float>,
    blendMode: BlendMode,
    rotation: Float,
    shaderColors: List<Color>,
    shaderColorStops: List<Float>?,
    shimmerWidth: Float,
  ): ShimmerEffect {
    return DefaultShimmerEffect(
      animationSpec = animationSpec,
      blendMode = blendMode,
      rotation = rotation,
      shaderColors = shaderColors,
      shaderColorStops = shaderColorStops,
      shimmerWidth = shimmerWidth,
    )
  }
}
