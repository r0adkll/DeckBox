package app.deckbox.features.cards.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
internal fun PriceUnit(
  label: String,
  price: @Composable () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
  ) {
    Text(
      text = label.uppercase(),
      style = MaterialTheme.typography.labelSmall.copy(
        fontWeight = FontWeight.SemiBold,
      ),
    )
    ProvideTextStyle(MaterialTheme.typography.bodyLarge) {
      price()
    }
  }
}
