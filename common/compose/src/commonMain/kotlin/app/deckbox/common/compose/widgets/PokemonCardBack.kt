package app.deckbox.common.compose.widgets

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
  Image(
    painter = painterResource(type.resourceName),
    contentDescription = LocalStrings.current.cardPlaceholderContentDescription,
    modifier = modifier,
  )
}
