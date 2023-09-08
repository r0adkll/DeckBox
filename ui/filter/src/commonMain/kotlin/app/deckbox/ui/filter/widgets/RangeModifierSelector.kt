package app.deckbox.ui.filter.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.rounded.GreaterThan
import app.deckbox.common.compose.icons.rounded.GreaterThanEqual
import app.deckbox.common.compose.icons.rounded.LessThan
import app.deckbox.common.compose.icons.rounded.LessThanEqual
import app.deckbox.core.model.RangeValue

@Composable
fun RangeModifierSelector(
  value: RangeValue.Modifier?,
  onValueSelected: (RangeValue.Modifier) -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .height(40.dp)
      .border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline,
        shape = RoundedCornerShape(50),
      )
      .clip(RoundedCornerShape(50)),
  ) {
    RangeModifierButton(
      currentValue = value,
      onClick = onValueSelected,
      rangeModifier = RangeValue.Modifier.LT,
      modifier = Modifier.weight(1f),
    )
    VerticalDivider()
    RangeModifierButton(
      currentValue = value,
      onClick = onValueSelected,
      rangeModifier = RangeValue.Modifier.LTE,
      modifier = Modifier.weight(1f),
    )
    VerticalDivider()
    RangeModifierButton(
      currentValue = value,
      onClick = onValueSelected,
      rangeModifier = RangeValue.Modifier.GT,
      modifier = Modifier.weight(1f),
    )
    VerticalDivider()
    RangeModifierButton(
      currentValue = value,
      onClick = onValueSelected,
      rangeModifier = RangeValue.Modifier.GTE,
      modifier = Modifier.weight(1f),
    )
  }
}

@Composable
private fun VerticalDivider(
  modifier: Modifier = Modifier,
) {
  Box(
    modifier
      .fillMaxHeight()
      .width(1.dp)
      .background(color = MaterialTheme.colorScheme.outline)
  )
}


@Composable
fun RangeModifierButton(
  currentValue: RangeValue.Modifier?,
  rangeModifier: RangeValue.Modifier,
  onClick: (RangeValue.Modifier) -> Unit,
  modifier: Modifier = Modifier,
) {
  val isSelected = rangeModifier == currentValue
  IconButton(
    modifier = modifier
      .background(
        color = if (isSelected) {
          MaterialTheme.colorScheme.secondaryContainer
        } else {
          Color.Transparent
        }
      ),
    onClick = {
      if (isSelected) {
        onClick(RangeValue.Modifier.NONE)
      } else {
        onClick(rangeModifier)
      }
    },
  ) {
    Icon(
      when (rangeModifier) {
        RangeValue.Modifier.LT -> Icons.Rounded.LessThan
        RangeValue.Modifier.LTE -> Icons.Rounded.LessThanEqual
        RangeValue.Modifier.GT -> Icons.Rounded.GreaterThan
        RangeValue.Modifier.GTE -> Icons.Rounded.GreaterThanEqual
        RangeValue.Modifier.NONE -> throw IllegalStateException("Can't use that modifier for a button")
      },
      contentDescription = null,
      tint = if (isSelected) {
        MaterialTheme.colorScheme.secondary
      } else {
        LocalContentColor.current.copy(alpha = 0.38f)
      }
    )
  }
}
