package app.deckbox.common.compose.widgets

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.lyricist.LocalStrings
import deckbox.common.compose.generated.resources.Res
import deckbox.common.compose.generated.resources.pokemon_back_en
import deckbox.common.compose.generated.resources.pokemon_back_jap
import deckbox.common.compose.generated.resources.pokemon_back_legacy
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

enum class CardBackType(val resource: DrawableResource) {
  English(Res.drawable.pokemon_back_en),
  Japanese(Res.drawable.pokemon_back_jap),
  Legacy(Res.drawable.pokemon_back_legacy),
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PokemonCardBack(
  modifier: Modifier = Modifier,
  type: CardBackType = CardBackType.English,
) {
  Image(
    painter = painterResource(type.resource),
    contentDescription = LocalStrings.current.cardPlaceholderContentDescription,
    modifier = modifier,
  )
}
