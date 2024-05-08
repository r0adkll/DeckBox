package app.deckbox.ui.tournament.decklist.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.deckbox.common.resources.strings.CurrencyFormatter
import app.deckbox.core.CurrencyType

@Composable
internal fun PriceChip(
  price: Double,
  currencyType: CurrencyType,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  color: Color = MaterialTheme.colorScheme.secondary,
) {
  Row(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .border(
        width = 1.dp,
        color = color,
        shape = RoundedCornerShape(8.dp),
      )
      .clickable(onClick = onClick)
      .padding(
        horizontal = 12.dp,
        vertical = 8.dp,
      ),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = CurrencyFormatter.format(price, currencyType),
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.SemiBold,
      fontFamily = FontFamily.Monospace,
      color = color,
    )
    Spacer(Modifier.width(8.dp))
    Icon(
      Icons.AutoMirrored.Rounded.OpenInNew,
      contentDescription = null,
      modifier = Modifier.size(18.dp),
      tint = color,
    )
  }
}
