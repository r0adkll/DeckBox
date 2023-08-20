package app.deckbox.ui.filter.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.types.asImageVector
import app.deckbox.common.compose.theme.PokemonTypeColor.toBackgroundColor
import app.deckbox.common.compose.theme.PokemonTypeColor.toColor
import app.deckbox.common.compose.theme.PokemonTypeColor.toContentColor
import app.deckbox.core.model.Type

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TypePicker(
  values: Set<Type>,
  onSelected: (Type) -> Unit,
  onUnselected: (Type) -> Unit,
  modifier: Modifier = Modifier,
) {
  FlowRow(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier,
  ) {
    Type.values().filter { it != Type.UNKNOWN }.forEach { type ->
      val isSelected = values.contains(type)
      TypeChip(
        type = type,
        isSelected = isSelected,
        onClick = {
          if (!isSelected) {
            onSelected(type)
          } else {
            onUnselected(type)
          }
        },
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TypeChip(
  type: Type,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val typeColor = type.toColor()
  val backgroundColor = if (isSelected) {
    typeColor
  } else {
    type.toBackgroundColor()
  }
  val contentColor = type.toContentColor(isSelected)

  Surface(
    modifier = modifier,
    color = backgroundColor,
    contentColor = contentColor,
    shape = RoundedCornerShape(8.dp),
    border = BorderStroke(
      width = 1.dp,
      color = typeColor,
    ),
    shadowElevation = if (isSelected) 2.dp else 0.dp,
    onClick = onClick,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Image(
        imageVector = type.asImageVector(),
        colorFilter = ColorFilter.tint(contentColor),
        contentDescription = null,
        modifier = Modifier
          .padding(8.dp)
          .size(18.dp),
      )
      Text(
        text = type.name.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
        style = MaterialTheme.typography.labelLarge.copy(
          color = contentColor,
        ),
        modifier = Modifier.padding(end = 16.dp),
      )
    }
  }
}
