package app.deckbox.features.cards.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.types.TypeIcon
import app.deckbox.common.compose.icons.types.asImageVector
import app.deckbox.common.compose.theme.PokemonTypeColor.toBackgroundColor
import app.deckbox.common.compose.theme.PokemonTypeColor.toColor
import app.deckbox.common.compose.widgets.ImageAvatar
import app.deckbox.core.extensions.readableFormat
import app.deckbox.core.model.Card
import app.deckbox.core.model.Type
import app.deckbox.features.cards.ui.pokemonCard
import cafe.adriel.lyricist.LocalStrings
import com.seiko.imageloader.rememberImageActionPainter
import com.seiko.imageloader.rememberImagePainter

@Composable
internal fun InfoCard(
  name: String,
  card: Card?,
  modifier: Modifier = Modifier,
) {
  val backgroundColor by animateColorAsState(
    card?.types?.firstOrNull()?.toBackgroundColor()
      ?: MaterialTheme.colorScheme.surfaceVariant,
  )

  val borderColor by animateColorAsState(
    card?.types?.firstOrNull()?.toColor()
      ?: MaterialTheme.colorScheme.onSurfaceVariant
  )

  OutlinedCard(
    colors = CardDefaults.outlinedCardColors(
      containerColor = backgroundColor,
    ),
    border = BorderStroke(
      width = 1.dp,
      color = borderColor,
    ),
    modifier = modifier,
  ) {
    // Header
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(
        start = 16.dp,
        end = 16.dp,
        top = 16.dp,
      ),
    ) {
      Text(
        text = name,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.weight(1f),
      )

      val type = card?.types?.firstOrNull()
      if (type != null) {
        TypeIcon(
          type = type,
        )
      }
    }

    if (card != null) {
      Row(
        modifier = Modifier.padding(
          start = 16.dp,
          end = 16.dp,
          top = 16.dp,
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        InfoChip(
          label = "Set",
          modifier = Modifier.weight(1f),
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
          ) {
//            Image(
//              modifier = Modifier.size(24.dp),
//              painter = rememberImagePainter(card.expansion.images.symbol),
//              contentDescription = null,
//            )
            Text(
              text = card.expansion.name,
            )
          }
        }

        InfoChip(
          label = "Number",
          modifier = Modifier.weight(1f),
        ) {
          Row {
            Text(
              text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                  append(card.number)
                }
                append(" of ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                  append(card.expansion.printedTotal.toString())
                }
              },
            )
          }
        }

        InfoChip(
          label = "Artist",
          modifier = Modifier.weight(1f),
        ) {
          Text(card.artist ?: "Unknown")
        }
      }

      Row(
        modifier = Modifier.padding(
          start = 16.dp,
          end = 16.dp,
          top = 16.dp,
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        InfoChip(
          label = "Rarity",
          modifier = Modifier.weight(1f),
        ) {
          Text(card.rarity ?: "Unknown")
        }
        InfoChip(
          label = "Retreat Cost",
          modifier = Modifier.weight(1f),
        ) {
          card.convertedRetreatCost?.let {
            Row(
              horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
              repeat(it) {
                TypeIcon(
                  type = Type.COLORLESS,
                  modifier = Modifier.size(24.dp)
                )
              }
            }
          } ?: Text("None")
        }
        Spacer(Modifier.weight(1f))
      }

      Spacer(Modifier.height(16.dp))
    }
  }
}

@Composable
private fun InfoChip(
  label: String,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit,
) {
  Column(
    modifier = modifier,
  ) {
    Text(
      text = label.uppercase(),
      style = MaterialTheme.typography.labelSmall.copy(
        fontWeight = FontWeight.SemiBold,
      ),
    )
    Spacer(Modifier.height(4.dp))
    ProvideTextStyle(MaterialTheme.typography.bodyLarge) {
      content()
    }
  }
}
