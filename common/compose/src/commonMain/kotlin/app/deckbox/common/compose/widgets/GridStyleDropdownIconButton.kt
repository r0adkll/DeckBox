package app.deckbox.common.compose.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Grid3x3
import androidx.compose.material.icons.rounded.Grid4x4
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.ViewCompact
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.deckbox.core.settings.PokemonGridStyle
import cafe.adriel.lyricist.LocalStrings

@Composable
fun GridStyleDropdownIconButton(
  selected: PokemonGridStyle?,
  onOptionClick: (PokemonGridStyle) -> Unit,
  modifier: Modifier = Modifier,
) {
  DropdownIconButton(
    selected = selected,
    options = PokemonGridStyle.All,
    onOptionClick = onOptionClick,
    icon = { Icon(Icons.Rounded.GridView, contentDescription = null) },
    optionText = { option ->
      Text(
        text = when (option) {
          PokemonGridStyle.Large -> LocalStrings.current.gridStyleLarge
          PokemonGridStyle.Small -> LocalStrings.current.gridStyleSmall
          PokemonGridStyle.Compact -> LocalStrings.current.gridStyleCompact
        },
      )
    },
    optionIcon = { option ->
      Icon(
        when (option) {
          PokemonGridStyle.Large -> Icons.Rounded.Grid3x3
          PokemonGridStyle.Small -> Icons.Rounded.Grid4x4
          PokemonGridStyle.Compact -> Icons.Rounded.ViewCompact
        },
        contentDescription = null,
      )
    },
    modifier = modifier,
  )
}
