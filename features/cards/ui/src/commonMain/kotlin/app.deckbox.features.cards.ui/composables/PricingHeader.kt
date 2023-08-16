package app.deckbox.features.cards.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings

@Composable
internal fun PricingHeader(
  title: String,
  lastUpdated: String,
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
      onClick = {},
      modifier = Modifier.padding(end = 16.dp),
    ) {
      Text(LocalStrings.current.actionBuy)
    }
  }
}
