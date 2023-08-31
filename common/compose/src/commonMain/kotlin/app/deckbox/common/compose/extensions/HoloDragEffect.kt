package app.deckbox.common.compose.extensions

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private val DefaultCornerRadius = 16.dp
private val DragAnimationSpec = tween<Float>(durationMillis = 500)
private const val MaxDragRotationInDegrees = 180

private val GradientColorStops = listOf(
  0.2f to Color.Transparent,
  0.36f to Color(0xFF9C6276),
  0.43f to Color(0xFF856F3C),
  0.5f to Color(0xFF4B9B69),
  0.57f to Color(0xFF5C8191),
  0.64f to Color(0xFF755389),
  0.8f to Color.Transparent,
)

fun Modifier.applyHoloAndDragEffect(
  shape: Shape = RoundedCornerShape(DefaultCornerRadius),
): Modifier = composed {
  var isDragging by remember { mutableStateOf(false) }
  var size by remember { mutableStateOf(IntSize(1, 1)) }

  var dragX by remember { mutableStateOf(0f) }
  var dragY by remember { mutableStateOf(0f) }

  LaunchedEffect(isDragging) {
    if (!isDragging) {
      launch {
        animate(dragX, 0f, animationSpec = DragAnimationSpec) { value, _ -> dragX = value }
      }
      launch {
        animate(dragY, 0f, animationSpec = DragAnimationSpec) { value, _ -> dragY = value }
      }
    }
  }

  val holoAlpha by animateFloatAsState(
    targetValue = if (isDragging) 0.8f else 0.2f,
    animationSpec = DragAnimationSpec,
  )
  val brush = AngledLinearGradient(
    colorStops = GradientColorStops,
    angleInDegrees = 115f,
    useAsCssAngle = true,
    dragOffset = Offset(dragX, dragY),
  )

  this
    .onSizeChanged { size = it }
    .pointerInput(Unit) {
      detectDragGestures(
        onDragStart = { isDragging = true },
        onDragEnd = { isDragging = false },
        onDragCancel = { isDragging = false },
        onDrag = { change, dragAmount ->
          if (change.positionChange() != Offset.Zero) {
            change.consume()
          }
          dragX += dragAmount.x
          dragY += dragAmount.y
        },
      )
    }
    .graphicsLayer {
      rotationX = dragY / size.height * MaxDragRotationInDegrees * 0.1f
      rotationY = -dragX / size.width * MaxDragRotationInDegrees * 0.1f
    }
    .clip(shape)
    .drawWithContent {
      drawContent()
      scale(1.5f) {
        drawRect(
          brush = brush,
          alpha = holoAlpha,
          blendMode = HoloBlendMode,
        )
      }
    }
}

expect val HoloBlendMode: BlendMode
