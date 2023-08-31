package app.deckbox.ui.decks.list.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.icons.PokemonEgg
import cafe.adriel.lyricist.LocalStrings

@Composable
internal fun EmptyState(
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(48.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Image(
      imageVector = DeckBoxIcons.PokemonEgg,
      contentDescription = null,
      modifier = Modifier.size(96.dp),
    )
    Spacer(Modifier.height(16.dp))
    Text(
      text = LocalStrings.current.decksEmptyStateMessage,
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.bodyLarge,
    )
  }
}
