package app.deckbox.common.compose.widgets.builder.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import app.deckbox.core.model.Legalities
import app.deckbox.core.model.Legality
import cafe.adriel.lyricist.LocalStrings

val SheetHeaderHeight = 56.dp

@Composable
internal fun SheetHeader(
  isValid: Boolean,
  totalCount: Int,
  pokemonCount: Int,
  trainerCount: Int,
  energyCount: Int,
  legalities: Legalities,
  onHeaderClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(SheetHeaderHeight)
      .clickable(onClick = onHeaderClick),
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.align(Alignment.CenterStart),
    ) {
      Spacer(Modifier.width(16.dp))
      if (!isValid) {
        Icon(
          Icons.Rounded.ErrorOutline,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.error,
        )
        Spacer(Modifier.width(8.dp))
      }
      Text(
        text = when {
          legalities.standard == Legality.LEGAL -> LocalStrings.current.standardLegality
          legalities.expanded == Legality.LEGAL -> LocalStrings.current.expandedLegality
          else -> LocalStrings.current.unlimitedLegality
        }.uppercase(),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
      )
    }
    CardCounter(
      total = totalCount,
      pokemon = pokemonCount,
      trainer = trainerCount,
      energy = energyCount,
      modifier = modifier
        .align(Alignment.CenterEnd)
        .padding(end = 16.dp)
        .zIndex(1f),
    )
  }
}
