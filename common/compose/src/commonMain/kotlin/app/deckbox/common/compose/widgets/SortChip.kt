package app.deckbox.common.compose.widgets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.HourglassBottom
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.SortByAlpha
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import app.deckbox.core.settings.SortOption
import cafe.adriel.lyricist.LocalStrings

@Composable
fun SortChip(
  value: SortOption,
  onValueChanged: (SortOption) -> Unit,
  modifier: Modifier = Modifier,
  sortOptions: List<SortOption> = SortOption.All,
) {
  var expanded by remember { mutableStateOf(false) }

  Box(modifier) {
    FilterChip(
      selected = true,
      onClick = { expanded = true },
      label = {
        Text(
          text = sortOptionTitle(value),
          modifier = Modifier.animateContentSize(),
        )
      },
      leadingIcon = {
        Icon(
          Icons.AutoMirrored.Rounded.Sort,
          contentDescription = null,
          modifier = Modifier.size(FilterChipDefaults.IconSize),
        )
      },
      trailingIcon = {
        Icon(
          Icons.Rounded.ArrowDropDown,
          contentDescription = null,
          modifier = Modifier.size(FilterChipDefaults.IconSize),
        )
      },
    )

    DropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
    ) {
      sortOptions.forEach { option ->
        DropdownMenuItem(
          text = {
            Text(
              text = sortOptionTitle(option),
              fontWeight = if (option == value) FontWeight.Bold else null,
            )
          },
          leadingIcon = {
            Icon(sortOptionIcon(option), contentDescription = null)
          },
          onClick = {
            expanded = false
            onValueChanged(option)
          },
        )
      }
    }
  }
}

@Composable
private fun sortOptionTitle(option: SortOption): String {
  return when (option) {
    SortOption.UpdatedAt -> LocalStrings.current.deckSortOrderUpdatedAt
    SortOption.CreatedAt -> LocalStrings.current.deckSortOrderCreatedAt
    SortOption.Alphabetically -> LocalStrings.current.deckSortOrderAlphabetically
    SortOption.Legality -> LocalStrings.current.deckSortOrderLegality
  }
}

@Composable
private fun sortOptionIcon(option: SortOption): ImageVector {
  return when (option) {
    SortOption.UpdatedAt -> Icons.Rounded.HourglassTop
    SortOption.CreatedAt -> Icons.Rounded.HourglassBottom
    SortOption.Alphabetically -> Icons.Rounded.SortByAlpha
    SortOption.Legality -> Icons.Rounded.Policy
  }
}
