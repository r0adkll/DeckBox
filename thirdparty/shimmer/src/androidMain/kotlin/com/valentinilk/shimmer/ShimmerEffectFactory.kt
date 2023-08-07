package com.valentinilk.shimmer

import android.graphics.Matrix
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
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

internal actual object ShimmerEffectFactory {
  actual fun create(
    animationSpec: AnimationSpec<Float>,
    blendMode: BlendMode,
    rotation: Float,
    shaderColors: List<Color>,
    shaderColorStops: List<Float>?,
    shimmerWidth: Float,
  ): ShimmerEffect {
    return AndroidShimmerEffect(
      animationSpec = animationSpec,
      blendMode = blendMode,
      rotation = rotation,
      shaderColors = shaderColors,
      shaderColorStops = shaderColorStops,
      shimmerWidth = shimmerWidth,
    )
  }
}

internal data class AndroidShimmerEffect(
  override val animationSpec: AnimationSpec<Float>,
  override val blendMode: BlendMode,
  override val rotation: Float,
  override val shaderColors: List<Color>,
  override val shaderColorStops: List<Float>?,
  override val shimmerWidth: Float,
) : ShimmerEffect {

  private val animatedState = Animatable(0f)

  private val transformationMatrix = Matrix()

  private val shader = LinearGradientShader(
    from = Offset(-shimmerWidth / 2, 0f),
    to = Offset(shimmerWidth / 2, 0f),
    colors = shaderColors,
    colorStops = shaderColorStops,
  )

  private val paint = Paint().apply {
    isAntiAlias = true
    style = PaintingStyle.Fill
    blendMode = this@AndroidShimmerEffect.blendMode
    shader = this@AndroidShimmerEffect.shader
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

    transformationMatrix.apply {
      reset()
      postTranslate(traversal, 0f)
      postRotate(rotation, pivotPoint.x, pivotPoint.y)
    }
    shader.setLocalMatrix(transformationMatrix)

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
