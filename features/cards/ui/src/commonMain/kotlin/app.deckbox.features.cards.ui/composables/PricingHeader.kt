package app.deckbox.features.cards.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings

@Composable
internal fun PricingHeader(
  title: String,
  lastUpdated: String,
  onBuyClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Column(
      modifier = Modifier
        .weight(1f)
        .padding(16.dp),
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
      )
      Text(
        text = lastUpdated,
        style = MaterialTheme.typography.labelSmall,
      )
    }

    Button(
      onClick = onBuyClick,
      modifier = Modifier.padding(end = 16.dp),
    ) {
      Text(LocalStrings.current.actionBuy)
    }
  }
}
