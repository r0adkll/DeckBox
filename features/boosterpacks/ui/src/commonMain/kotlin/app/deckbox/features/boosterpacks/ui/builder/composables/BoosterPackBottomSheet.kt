package app.deckbox.features.boosterpacks.ui.builder.composables

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.features.boosterpacks.ui.builder.BoosterPackBuilderUiState
import app.deckbox.features.boosterpacks.ui.builder.PackPriceState

@Composable
internal fun ColumnScope.BoosterPackBottomSheet(
  state: BoosterPackBuilderUiState,
) {
  HorizontalDivider()
  PackPrices(
    prices = state.price.dataOrNull ?: PackPriceState(),
  )

  Spacer(Modifier.height(16.dp))
}
