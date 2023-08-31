package app.deckbox.ui.decks.builder.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.filled.Cards
import app.deckbox.core.model.Legalities
import app.deckbox.core.model.Legality
import cafe.adriel.lyricist.LocalStrings

val SheetHeaderHeight = 56.dp

@Composable
internal fun SheetHeader(
  totalCount: Int,
  legalities: Legalities,
  onHeaderClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .height(SheetHeaderHeight)
      .clickable(onClick = onHeaderClick),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Spacer(Modifier.width(16.dp))
    Text(
      text = when {
        legalities.standard == Legality.LEGAL -> LocalStrings.current.standardLegality
        legalities.expanded == Legality.LEGAL -> LocalStrings.current.expandedLegality
        else -> LocalStrings.current.unlimitedLegality
      },
      style = MaterialTheme.typography.titleSmall,
      fontWeight = FontWeight.SemiBold,
    )
    Spacer(Modifier.weight(1f))
    Text(
      text = totalCount.toString(),
      style = MaterialTheme.typography.titleSmall,
      fontWeight = FontWeight.SemiBold,
    )
    Spacer(Modifier.width(8.dp))
    Icon(
      Icons.Filled.Cards,
      contentDescription = null,
    )
    Spacer(Modifier.width(16.dp))
  }
}
