package app.deckbox.ui.decks.picker.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.rounded.AddDeck
import app.deckbox.common.compose.icons.rounded.NewDeck
import cafe.adriel.lyricist.LocalStrings

@Composable
internal fun NewDeckPackItem(
  modifier: Modifier = Modifier,
) {
  ListItem(
    headlineContent = {
      Text(
        text = LocalStrings.current.fabActionNewDeckButton,
        fontWeight = FontWeight.SemiBold,
      )
    },
    leadingContent = {
      Box(
        modifier = Modifier
          .size(56.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
      ) {
        Icon(Icons.Rounded.NewDeck, contentDescription = null)
      }
    },
    trailingContent = {
      Icon(Icons.Rounded.AddDeck, contentDescription = null)
    },
    modifier = modifier,
  )
}
