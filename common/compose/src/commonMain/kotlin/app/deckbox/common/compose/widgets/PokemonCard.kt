package app.deckbox.common.compose.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import app.deckbox.core.model.Card
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.orEmpty
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.rememberImageBitmap
import org.jetbrains.compose.resources.resource

private val CardCornerRadius = 8.dp
private const val CardAspectRatio = 0.7167969f

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PokemonCard(
  card: Card,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val imageAction by key(card.id) {
    rememberImageAction(card.image.small)
  }

  TradingCard(
    onClick = onClick,
    modifier = modifier,
  ) {
    Image(
      painter = rememberImageActionPainter(imageAction),
      contentDescription = card.name,
      modifier = Modifier.fillMaxSize(),
    )

    if (imageAction is ImageEvent) {
      Image(
        painter = painterResource("pokemon_back_en.webp"),
        contentDescription = "Card placeholder",
        modifier = Modifier.fillMaxSize(),
      )

//      CircularProgressIndicator(
//        modifier = Modifier.align(Alignment.Center),
//        color = MaterialTheme.colorScheme.secondaryContainer,
//      )

      SpinningPokeballLoadingIndicator(
        modifier = Modifier.align(Alignment.Center),
        size = 52.dp,
      )
    }
  }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PlaceHolderPokemonCard(
  modifier: Modifier = Modifier,
) {
  val bitmap = resource("pokemon_back_en.webp").rememberImageBitmap().orEmpty()
  TradingCard(
    enabled = false,
    modifier = modifier,
  ) {
    Image(
      bitmap = bitmap,
      contentDescription = "Card placeholder",
    )
  }
}

@Composable
private fun TradingCard(
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  onClick: () -> Unit = {},
  content: @Composable BoxScope.() -> Unit,
) {
  val shape = RoundedCornerShape(CardCornerRadius)
  Box(
    modifier = modifier
      .aspectRatio(CardAspectRatio)
      .wrapContentSize()
      .clip(shape)
      .clickable(
        role = Role.Image,
        enabled = enabled,
        onClick = onClick,
      ),
  ) {
    content()
  }
}
