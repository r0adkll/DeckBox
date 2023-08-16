package app.deckbox.features.cards.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.common.resources.strings.CurrencyFormatter
import app.deckbox.core.extensions.readableFormat
import app.deckbox.core.model.Card
import cafe.adriel.lyricist.LocalStrings

@Composable
fun TcgPlayerPriceCard(
  tcgPlayer: Card.TcgPlayer,
  modifier: Modifier = Modifier,
) {
  OutlinedCard(
    modifier = modifier,
  ) {
    PricingHeader(
      title = "TCG Player",
      lastUpdated = "Last updated @ ${tcgPlayer.updatedAt.readableFormat}",
    )

    tcgPlayer.prices?.let { prices ->
      prices.normal?.let { normal  ->
        PriceRow(
          label = LocalStrings.current.tcgPlayerNormal,
          price = normal,
        )
      }

      prices.holofoil?.let { holofoil  ->
        PriceRow(
          label = LocalStrings.current.tcgPlayerHolofoil,
          price = holofoil,
        )
      }

      prices.reverseHolofoil?.let { reverseHolofoil  ->
        PriceRow(
          label = LocalStrings.current.tcgPlayerReverseHolofoil,
          price = reverseHolofoil,
        )
      }

      prices.firstEditionNormal?.let { normal  ->
        PriceRow(
          label = LocalStrings.current.tcgPlayerFirstEditionNormal,
          price = normal,
        )
      }

      prices.firstEditionHolofoil?.let { holofoil  ->
        PriceRow(
          label = LocalStrings.current.tcgPlayerFirstEditionHolofoil,
          price = holofoil,
        )
      }
    }
  }
}

@Composable
private fun PriceRow(
  label: String,
  price: Card.TcgPlayer.Price,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .padding(
        horizontal = 16.dp,
      ),
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelLarge,
      color = MaterialTheme.colorScheme.secondary,
    )
    Spacer(Modifier.height(4.dp))
    Row {
      price.market?.let { market ->
        PriceUnit(
          label = LocalStrings.current.priceMarket,
          price = { Text(CurrencyFormatter.format(market)) },
          modifier = Modifier.weight(1f),
        )
      }
      price.low?.let { low ->
        PriceUnit(
          label = LocalStrings.current.priceLow,
          price = { Text(CurrencyFormatter.format(low)) },
          modifier = Modifier.weight(1f),
        )
      }
      price.mid?.let { mid ->
        PriceUnit(
          label = LocalStrings.current.priceMid,
          price = { Text(CurrencyFormatter.format(mid)) },
          modifier = Modifier.weight(1f),
        )
      }
      price.high?.let { high ->
        PriceUnit(
          label = LocalStrings.current.priceHigh,
          price = { Text(CurrencyFormatter.format(high)) },
          modifier = Modifier.weight(1f),
        )
      }
    }
    Spacer(Modifier.height(16.dp))
  }
}
