package app.deckbox.common.compose.widgets.builder.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BuilderTextField(
  modifier: Modifier = Modifier,
  icon: @Composable () -> Unit,
  textField: @Composable () -> Unit,
) {
  Row(
    modifier = modifier
      .padding(end = 16.dp),
  ) {
    Box(
      modifier = Modifier.padding(16.dp),
    ) {
      icon()
    }
    textField()
  }
}
