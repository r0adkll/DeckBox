package app.deckbox.features.cards.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.common.resources.strings.CurrencyFormatter
import app.deckbox.common.resources.strings.CurrencyType
import app.deckbox.core.extensions.readableFormat
import app.deckbox.core.model.Card
import cafe.adriel.lyricist.LocalStrings

@Composable
fun CardMarketPriceCard(
  cardMarket: Card.CardMarket,
  modifier: Modifier = Modifier,
) {
  OutlinedCard(
    modifier = modifier,
  ) {
    PricingHeader(
      title = "Cardmarket",
      lastUpdated = "Last updated @ ${cardMarket.updatedAt.readableFormat}",
    )

    cardMarket.prices?.let { prices ->
      Row(
        modifier = Modifier
          .padding(
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp,
          )
      ) {
        prices.trendPrice?.let { trend ->
          PriceUnit(
            label = LocalStrings.current.priceTrend,
            price = { Text(CurrencyFormatter.format(trend, CurrencyType.EUR)) },
            modifier = Modifier.weight(1f),
          )
        }
        prices.avg1?.let { avg1 ->
          PriceUnit(
            label = LocalStrings.current.oneDayAvg,
            price = { Text(CurrencyFormatter.format(avg1, CurrencyType.EUR)) },
            modifier = Modifier.weight(1f),
          )
        }
        prices.avg7?.let { avg7 ->
          PriceUnit(
            label = LocalStrings.current.sevenDayAvg,
            price = { Text(CurrencyFormatter.format(avg7, CurrencyType.EUR)) },
            modifier = Modifier.weight(1f),
          )
        }
        prices.avg30?.let { avg30 ->
          PriceUnit(
            label = LocalStrings.current.thirtyDayAvg,
            price = { Text(CurrencyFormatter.format(avg30, CurrencyType.EUR)) },
            modifier = Modifier.weight(1f),
          )
        }
      }
    }
  }
}
