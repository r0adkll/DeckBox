package app.deckbox.features.cards.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun InfoChip(
  label: String,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit,
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
    Spacer(Modifier.height(4.dp))
    ProvideTextStyle(MaterialTheme.typography.bodyLarge) {
      content()
    }
  }
}
