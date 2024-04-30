package app.deckbox.ui.filter.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

private val TrackHeight = 32.dp
private val TrackBorderWidth = 1.dp
private val ThumbDefaultElevation = 1.dp
private val ThumbPressedElevation = 6.dp
private val ThumbSize = DpSize(20.dp, 20.dp)

class DeckBoxSliderColors(
  val thumbColor: Color,
  val activeTrackColor: Color,
  val activeTickColor: Color,
  val inactiveTrackColor: Color,
  val inactiveTickColor: Color,
  val disabledThumbColor: Color,
  val disabledActiveTrackColor: Color,
  val disabledActiveTickColor: Color,
  val disabledInactiveTrackColor: Color,
  val disabledInactiveTickColor: Color,
) {

  @Composable
  internal fun thumbColor(enabled: Boolean): State<Color> {
    return rememberUpdatedState(if (enabled) thumbColor else disabledThumbColor)
  }

  @Composable
  internal fun trackColor(enabled: Boolean, active: Boolean): State<Color> {
    return rememberUpdatedState(
      if (enabled) {
        if (active) activeTrackColor else inactiveTrackColor
      } else {
        if (active) disabledActiveTrackColor else disabledInactiveTrackColor
      },
    )
  }

  @Composable
  internal fun tickColor(enabled: Boolean, active: Boolean): State<Color> {
    return rememberUpdatedState(
      if (enabled) {
        if (active) activeTickColor else inactiveTickColor
      } else {
        if (active) disabledActiveTickColor else disabledInactiveTickColor
      },
    )
  }

  companion object {
    @Composable
    fun defaults(
      thumbColor: Color = MaterialTheme.colorScheme.primary,
      activeTrackColor: Color = MaterialTheme.colorScheme.primaryContainer,
      activeTickColor: Color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.38f),
      inactiveTrackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
      inactiveTickColor: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
      disabledThumbColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        .compositeOver(MaterialTheme.colorScheme.surface),
      disabledActiveTrackColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
      disabledActiveTickColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
      disabledInactiveTrackColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
      disabledInactiveTickColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
    ) = DeckBoxSliderColors(
      thumbColor = thumbColor,
      activeTrackColor = activeTrackColor,
      activeTickColor = activeTickColor,
      inactiveTrackColor = inactiveTrackColor,
      inactiveTickColor = inactiveTickColor,
      disabledThumbColor = disabledThumbColor,
      disabledActiveTrackColor = disabledActiveTrackColor,
      disabledActiveTickColor = disabledActiveTickColor,
      disabledInactiveTrackColor = disabledInactiveTrackColor,
      disabledInactiveTickColor = disabledInactiveTickColor,
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Track(
  sliderState: SliderState,
  modifier: Modifier = Modifier,
  colors: DeckBoxSliderColors = DeckBoxSliderColors.defaults(),
  enabled: Boolean = true,
) {
  val thumbColor = colors.thumbColor(enabled)
  val inactiveTrackColor = colors.trackColor(enabled, active = false)
  val activeTrackColor = colors.trackColor(enabled, active = true)
  Canvas(
    modifier
      .fillMaxWidth()
      .height(TrackHeight),
  ) {
    val isRtl = layoutDirection == LayoutDirection.Rtl
    val sliderLeft = Offset(0f, center.y)
    val sliderRight = Offset(size.width, center.y)
    val sliderStart = if (isRtl) sliderRight else sliderLeft
    val sliderEnd = if (isRtl) sliderLeft else sliderRight
    val trackStrokeWidth = TrackHeight.toPx()
    val trackFillWidth = (TrackHeight - TrackBorderWidth * 2).toPx()

    drawLine(
      inactiveTrackColor.value,
      sliderStart,
      sliderEnd,
      trackStrokeWidth,
      StrokeCap.Round,
    )
    val sliderValueEnd = Offset(
      sliderStart.x +
        (sliderEnd.x - sliderStart.x) * sliderState.coercedValueAsFraction,
      center.y,
    )

    val sliderValueStart = Offset(
      sliderStart.x +
        (sliderEnd.x - sliderStart.x) * 0f,
      center.y,
    )

    drawLine(
      thumbColor.value,
      sliderValueStart,
      sliderValueEnd,
      trackStrokeWidth,
      StrokeCap.Round,
    )
    drawLine(
      activeTrackColor.value,
      sliderValueStart,
      sliderValueEnd,
      trackFillWidth,
      StrokeCap.Round,
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
val SliderState.coercedValueAsFraction
  get() = calcFraction(
    valueRange.start,
    valueRange.endInclusive,
    value.coerceIn(valueRange.start, valueRange.endInclusive),
  )

private fun calcFraction(a: Float, b: Float, pos: Float) =
  (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)

@Composable
fun Thumb(
  interactionSource: MutableInteractionSource,
  modifier: Modifier = Modifier,
  colors: DeckBoxSliderColors = DeckBoxSliderColors.defaults(),
  enabled: Boolean = true,
  thumbSize: DpSize = ThumbSize,
) {
  val interactions = remember { mutableStateListOf<Interaction>() }
  LaunchedEffect(interactionSource) {
    interactionSource.interactions.collect { interaction ->
      when (interaction) {
        is PressInteraction.Press -> interactions.add(interaction)
        is PressInteraction.Release -> interactions.remove(interaction.press)
        is PressInteraction.Cancel -> interactions.remove(interaction.press)
        is DragInteraction.Start -> interactions.add(interaction)
        is DragInteraction.Stop -> interactions.remove(interaction.start)
        is DragInteraction.Cancel -> interactions.remove(interaction.start)
      }
    }
  }

  val elevation = if (interactions.isNotEmpty()) {
    ThumbPressedElevation
  } else {
    ThumbDefaultElevation
  }
  val shape = CircleShape

  Spacer(
    modifier
      .size(thumbSize)
      .indication(
        interactionSource = interactionSource,
        indication = rememberRipple(
          bounded = false,
          radius = 20.dp,
        ),
      )
      .hoverable(interactionSource = interactionSource)
      .shadow(if (enabled) elevation else 0.dp, shape, clip = false)
      .background(colors.thumbColor(enabled).value, shape),
  )
}
