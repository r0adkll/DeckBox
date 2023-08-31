package app.deckbox.ui.decks.builder.composables

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import app.deckbox.common.compose.icons.EvolutionLink
import app.deckbox.core.model.Card
import app.deckbox.core.model.Evolution
import app.deckbox.core.model.Stacked

private val DefaultEvolutionStageWidth = 16.dp
private val SiblingCardSpacing = 1.dp
private val LinkPaddingVertical = 6.dp

@Composable
internal fun PokemonEvolution(
  evolution: Evolution,
  modifier: Modifier = Modifier,
  cardWidth: Dp = 100.dp,
  stageWidth: Dp = DefaultEvolutionStageWidth,
  contentPaddingHorizontal: Dp = 0.dp,
  cardContent: @Composable (card: Stacked<Card>) -> Unit,
) {
  val imagePainter = rememberVectorPainter(EvolutionLink)

  val transition = rememberInfiniteTransition()
  val translationX by transition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(5000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart,
    ),
  )

  Row(
    modifier = modifier
      .horizontalScroll(rememberScrollState())
      .drawWithCache {
        val availableHeight = (size.height - (2 * LinkPaddingVertical.roundToPx()))

        val imageRatio = imagePainter.intrinsicSize.width / imagePainter.intrinsicSize.height
        val linkWidth = availableHeight * imageRatio

        // Compute link offsets
        val segments = evolution.nodes.map {
          it.cards.size * cardWidth + ((it.cards.size - 1).coerceAtLeast(0) * SiblingCardSpacing)
        }

        val links = segments.mapIndexedNotNull { index, segmentWidth ->
          if (index + 1 < segments.size) {
            segments.subList(0, index).sumOf { it.toPx().toDouble() }.toFloat() + segmentWidth.toPx() +
              ((index - 1).coerceAtLeast(0) * stageWidth.toPx()) // Link spacing
          } else {
            null
          }
        }

        onDrawBehind {
          if (links.isNotEmpty()) {
            links.forEach { positionX ->
              // Okay we need to render a Link
              val offsetX = translationX * linkWidth

              // Draw first
              translate(
                left = positionX + offsetX,
                top = LinkPaddingVertical.toPx(),
              ) {
                with(imagePainter) {
                  draw(
                    size = Size(
                      width = linkWidth,
                      height = availableHeight,
                    ),
                  )
                }
              }

              // Draw second
              translate(
                left = positionX - (linkWidth - offsetX),
                top = LinkPaddingVertical.toPx(),
              ) {
                with(imagePainter) {
                  draw(
                    size = Size(
                      width = linkWidth,
                      height = availableHeight,
                    ),
                  )
                }
              }

              clipRect(
                left = positionX,
                top = LinkPaddingVertical.toPx(),
                right = positionX + stageWidth.toPx(),
                bottom = size.height - LinkPaddingVertical.toPx(),
              ) {}
            }
          }
        }
      }
      .padding(horizontal = contentPaddingHorizontal),
    horizontalArrangement = Arrangement.spacedBy(stageWidth),
  ) {
    evolution.nodes.forEach { node ->
      key(node.name) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(1.dp),
        ) {
          node.cards.forEach { card ->
            key(card.card.id) {
              cardContent(card)
            }
          }
        }
      }
    }
  }
}
