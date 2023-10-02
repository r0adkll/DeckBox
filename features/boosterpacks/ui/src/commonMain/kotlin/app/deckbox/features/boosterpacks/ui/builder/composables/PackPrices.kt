package app.deckbox.features.boosterpacks.ui.builder.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.common.resources.strings.CurrencyFormatter
import app.deckbox.common.resources.strings.CurrencyType
import app.deckbox.features.boosterpacks.ui.builder.PackPrice
import app.deckbox.features.boosterpacks.ui.builder.PackPriceState
import cafe.adriel.lyricist.LocalStrings

@Composable
internal fun PackPrices(
  prices: PackPriceState,
  modifier: Modifier = Modifier,
) {
  if (prices.tcgPlayer == null && prices.cardMarket == null) return
  Column(
    modifier = modifier,
  ) {
    prices.tcgPlayer?.let { price ->
      PriceRow(
        label = LocalStrings.current.tcgPlayer,
        price = price,
        currencyType = CurrencyType.USD,
      )
    }

    prices.cardMarket?.let { price ->
      PriceRow(
        label = LocalStrings.current.cardMarket,
        price = price,
        currencyType = CurrencyType.EUR,
      )
    }
  }
}

@Composable
private fun PriceRow(
  label: String,
  price: PackPrice,
  currencyType: CurrencyType,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .padding(
        horizontal = 16.dp,
        vertical = 8.dp,
      ),
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Column(
        modifier = Modifier.weight(1f),
      ) {
        Text(
          text = label,
          style = MaterialTheme.typography.titleSmall,
        )
        Text(
          text = LocalStrings.current.deckLastUpdated(price.lastUpdated),
          style = MaterialTheme.typography.labelSmall,
        )
      }
      Spacer(Modifier.width(16.dp))
      Text(
        text = CurrencyFormatter.format(price.market, currencyType),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier
          .background(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(8.dp),
          )
          .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(8.dp),
          )
          .padding(
            horizontal = 8.dp,
            vertical = 8.dp,
          ),
      )
    }
    Spacer(Modifier.height(8.dp))
    FilledTonalButton(
      onClick = {},
      modifier = Modifier.fillMaxWidth(),
    ) {
      Text("Purchase @ $label")
    }
  }
}
