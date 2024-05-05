package app.deckbox.common.compose.widgets

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter
import com.valentinilk.shimmer.shimmer

val CardCornerRadius = 8.dp
const val CardAspectRatio = 0.7167969f

@Composable
fun PokemonCard(
  url: String,
  name: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  onLongClick: () -> Unit = {},
  count: Int? = null,
  collected: Int? = null,
) {
  PokemonCard(
    onClick = onClick,
    onLongClick = onLongClick,
    modifier = modifier,
    counter = {
      PokemonCardCounter(
        count = count,
        collected = collected,
        modifier = Modifier.align(Alignment.BottomStart),
      )
    },
  ) {
    val imageAction by key(url) {
      rememberImageAction(url)
    }

    Image(
      painter = rememberImageActionPainter(imageAction),
      contentDescription = name,
      modifier = Modifier.fillMaxSize(),
    )

    PokemonCardLoadingContent(
      isLoading = imageAction is ImageEvent,
    )
  }
}

@Composable
fun PokemonCard(
  card: Stacked<Card>,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  onLongClick: () -> Unit = {},
) {
  PokemonCard(
    card = card.card,
    onClick = onClick,
    onLongClick = onLongClick,
    count = card.count,
    collected = card.collected,
    modifier = modifier,
  )
}

@Composable
fun PokemonCard(
  card: Card?,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  onLongClick: () -> Unit = {},
  count: Int? = null,
  collected: Int? = null,
) {
  PokemonCard(
    onClick = onClick,
    onLongClick = onLongClick,
    modifier = modifier,
    counter = {
      PokemonCardCounter(
        count = count,
        collected = collected,
        modifier = Modifier.align(Alignment.BottomStart),
      )
    },
  ) {
    val isLoading = if (card != null) {
      val imageAction by key(card.id) {
        rememberImageAction(card.image.small)
      }

      Image(
        painter = rememberImageActionPainter(imageAction),
        contentDescription = card.name,
        modifier = Modifier.fillMaxSize(),
      )

      imageAction is ImageEvent
    } else {
      true
    }

    PokemonCardLoadingContent(
      isLoading = isLoading,
    )
  }
}

@Composable
internal fun PokemonCard(
  modifier: Modifier = Modifier,
  onClick: () -> Unit = {},
  onLongClick: () -> Unit = {},
  counter: @Composable BoxScope.() -> Unit,
  content: @Composable BoxScope.() -> Unit,
) {
  TradingCard(
    onClick = onClick,
    onLongClick = onLongClick,
    modifier = modifier,
  ) {
    content()
    counter()
  }
}

@Composable
private fun PokemonCardCounter(
  count: Int?,
  collected: Int?,
  modifier: Modifier = Modifier,
) {
  if (count != null || collected != null) {
    CardCounter(
      count = count,
      collected = collected,
      modifier = modifier,
    )
  }
}

@Composable
private fun PokemonCardLoadingContent(
  isLoading: Boolean,
  modifier: Modifier = Modifier,
) {
  if (isLoading) {
    val transition = rememberInfiniteTransition()

    val alpha by transition.animateFloat(
      initialValue = 0.5f,
      targetValue = 0.75f,
      animationSpec = infiniteRepeatable(
        animation = tween(500),
        repeatMode = RepeatMode.Reverse,
      ),
    )

    PokemonCardBack(
      modifier = modifier
        .fillMaxSize()
        .alpha(alpha),
    )
  }
}

@Composable
fun ShimmerPokemonCard(
  modifier: Modifier = Modifier,
) {
  TradingCard(
    enabled = false,
    modifier = modifier,
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray)
        .shimmer(),
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TradingCard(
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  onClick: () -> Unit = {},
  onLongClick: () -> Unit = {},
  content: @Composable BoxScope.() -> Unit,
) {
  val shape = RoundedCornerShape(CardCornerRadius)
  Box(
    modifier = modifier
      .aspectRatio(CardAspectRatio)
      .wrapContentSize()
      .clip(shape)
      .combinedClickable(
        role = Role.Image,
        enabled = enabled,
        onClick = onClick,
        onLongClick = onLongClick,
      ),
  ) {
    content()
  }
}

@Composable
private fun CardCounter(
  count: Int?,
  collected: Int?,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .clip(
        RoundedCornerShape(
          topEnd = CardCornerRadius,
        ),
      ),
  ) {
    if (count != null) {
      Text(
        text = count.toString(),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
          .background(MaterialTheme.colorScheme.primary)
          .padding(
            horizontal = 8.dp,
            vertical = 6.dp,
          ),
      )
    }
    if (collected != null) {
      Text(
        text = collected.toString(),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onTertiary,
        modifier = Modifier
          .background(MaterialTheme.colorScheme.tertiary)
          .padding(
            horizontal = 8.dp,
            vertical = 6.dp,
          ),
      )
    }
  }
}
