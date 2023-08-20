package app.deckbox.common.compose.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.Platform
import app.deckbox.common.compose.currentPlatform
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.icons.PokeBall
import cafe.adriel.lyricist.LocalStrings
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

enum class CardBackType(val resourceName: String) {
  English("pokemon_back_en.webp"),
  Japanese("pokemon_back_jap.webp"),
  Legacy("pokemon_back_legacy.webp"),
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PokemonCardBack(
  modifier: Modifier = Modifier,
  type: CardBackType = CardBackType.English,
) {
  // compose.component.resources is currently borked on iOS targets until compose-mp 1.5.0+
  // So we just check and omit if that is the case
  if (currentPlatform == Platform.ANDROID) {
    Image(
      painter = painterResource(type.resourceName),
      contentDescription = LocalStrings.current.cardPlaceholderContentDescription,
      modifier = modifier,
    )
  } else {
    val shape = RoundedCornerShape(8.dp)
    Box(
      modifier = modifier
        .background(Color(0xFF2D467C))
        .clip(RoundedCornerShape(8.dp))
        .border(8.dp, Color(0xFF1C2650), shape),
      contentAlignment = Alignment.Center,
    ) {
      Image(
        DeckBoxIcons.PokeBall,
        contentDescription = null,
      )
    }
  }
}
