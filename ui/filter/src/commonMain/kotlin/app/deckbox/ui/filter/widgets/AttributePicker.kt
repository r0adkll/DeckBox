package app.deckbox.ui.filter.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.deckbox.ui.filter.spec.Attribute

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AttributePicker(
  values: Set<Attribute>,
  selected: Set<Attribute>,
  onSelected: (Attribute) -> Unit,
  onUnselected: (Attribute) -> Unit,
  modifier: Modifier = Modifier,
) {
  FlowRow(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier,
  ) {
    values.forEach { attribute ->
      val isSelected = selected.contains(attribute)

      Chip(
        isSelected = isSelected,
        onClick = {
          if (isSelected) {
            onUnselected(attribute)
          } else {
            onSelected(attribute)
          }
        }
      ) {
        Text(
          text = attribute.displayText,
          textAlign = TextAlign.Center,
          modifier = Modifier.defaultMinSize(minWidth = 32.dp),
        )
      }
    }
  }
}
