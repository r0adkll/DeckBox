package app.deckbox.playtest.ui.composables.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
internal fun MatLabel(
  text: String,
  modifier: Modifier = Modifier,
  color: Color = Color.Unspecified,
) {
  Text(
    text = text.uppercase(),
    color = color,
    style = MaterialTheme.typography.labelSmall,
    fontWeight = FontWeight.Bold,
    modifier = modifier,
  )
}
