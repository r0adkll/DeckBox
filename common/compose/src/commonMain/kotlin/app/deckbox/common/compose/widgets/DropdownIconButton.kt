package app.deckbox.common.compose.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.extensions.alpha

@Composable
fun <T> DropdownIconButton(
  selected: T?,
  options: List<T>,
  onOptionClick: (T) -> Unit,
  icon: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  title: @Composable () -> Unit = {},
  offset: DpOffset = DpOffset(0.dp, 0.dp),
  optionText: @Composable (T) -> Unit = { Text(it.toString()) },
  optionIcon: (@Composable (T) -> Unit)? = null,
) {
  var isExpanded by remember { mutableStateOf(false) }
  Box(modifier) {
    IconButton(
      onClick = { isExpanded = true },
      content = icon,
    )

    DropdownMenu(
      expanded = isExpanded,
      offset = offset,
      onDismissRequest = { isExpanded = false },
    ) {
      title()
      options.forEach { option ->
        val isSelected = option == selected
        DropdownMenuItem(
          text = {
            ProvideTextStyle(
              MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (isSelected) FontWeight.Bold else null,
              ),
            ) {
              optionText(option)
            }
          },
          leadingIcon = optionIcon?.let { { it(option) } },
          trailingIcon = if (isSelected) {
            {
              Icon(
                Icons.Rounded.Check,
                contentDescription = null,
              )
            }
          } else {
            null
          },
          onClick = {
            isExpanded = false
            onOptionClick(option)
          },
        )
      }
    }
  }
}

@Composable
fun <T> DropdownIconButton(
  options: List<T>,
  onOptionClick: (T) -> Unit,
  icon: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  title: @Composable () -> Unit = {},
  offset: DpOffset = DpOffset(0.dp, 0.dp),
  optionText: @Composable (T) -> Unit = { Text(it.toString()) },
  optionIcon: (@Composable (T) -> Unit)? = null,
) {
  var isExpanded by remember { mutableStateOf(false) }
  Box(modifier) {
    IconButton(
      onClick = { isExpanded = true },
      content = icon,
    )

    DropdownMenu(
      expanded = isExpanded,
      offset = offset,
      onDismissRequest = { isExpanded = false },
    ) {
      title()
      options.forEach { option ->
        DropdownMenuItem(
          text = {
            ProvideTextStyle(
              MaterialTheme.typography.labelLarge,
            ) {
              optionText(option)
            }
          },
          leadingIcon = optionIcon?.let { { it(option) } },
          onClick = {
            isExpanded = false
            onOptionClick(option)
          },
        )
      }
    }
  }
}

@Composable
fun DropdownHeaderText(
  text: String,
  modifier: Modifier = Modifier,
) {
  Text(
    text = text,
    style = MaterialTheme.typography.labelMedium.alpha(0.5f),
    modifier = Modifier.padding(
      horizontal = 16.dp,
      vertical = 8.dp,
    ),
  )
}
