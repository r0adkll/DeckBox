package app.deckbox.ui.decks.picker.composables

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.extensions.timeAgo
import app.deckbox.common.compose.icons.outline.BoosterPack
import app.deckbox.common.compose.icons.rounded.AddDeck
import app.deckbox.core.model.Deck
import cafe.adriel.lyricist.LocalStrings
import com.seiko.imageloader.rememberImagePainter

@Composable
fun DeckPickerItem(
  deck: Deck,
  modifier: Modifier = Modifier,
) {
  ListItem(
    headlineContent = { Text(deckTitle(deck)) },
    supportingContent = { Text(LocalStrings.current.deckLastUpdated(deck.updatedAt.timeAgo)) },
    leadingContent = {
      Thumbnail(deck)
    },
    trailingContent = {
      Icon(Icons.Rounded.AddDeck, contentDescription = null)
    },
    modifier = modifier,
  )
}

@Composable
fun deckTitle(deck: Deck): AnnotatedString {
  return if (deck.name.isBlank()) {
    AnnotatedString(
      text = LocalStrings.current.deckNoName,
      spanStyle = SpanStyle(
        fontStyle = FontStyle.Italic,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), // This is the ListItem disabled headline color
      ),
    )
  } else {
    AnnotatedString(deck.name)
  }
}

@Composable
private fun Thumbnail(
  deck: Deck,
  modifier: Modifier = Modifier,
) {
  val packImage = remember {
    deck.cardImages.firstOrNull()
  }

  val shape = RoundedCornerShape(8.dp)
  if (packImage != null) {
    val painter = key(packImage) { rememberImagePainter(packImage) }
    Image(
      painter = painter,
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = modifier
        .size(56.dp)
        .clip(shape)
        .background(MaterialTheme.colorScheme.secondaryContainer),
    )
  } else {
    Box(
      modifier = modifier
        .size(56.dp)
        .clip(shape)
        .background(MaterialTheme.colorScheme.secondaryContainer),
      contentAlignment = Alignment.Center,
    ) {
      Icon(
        Icons.Outlined.BoosterPack,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSecondaryContainer,
      )
    }
  }
}
