package app.deckbox.features.cards.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import app.deckbox.common.compose.icons.types.OverlappingTypeRow
import app.deckbox.common.compose.icons.types.TypeIcon
import app.deckbox.common.compose.theme.PokemonTypeColor.toBackgroundColor
import app.deckbox.common.compose.theme.PokemonTypeColor.toColor
import app.deckbox.core.model.Card
import app.deckbox.core.model.Type

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
      ?: MaterialTheme.colorScheme.onSurfaceVariant,
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

      val types = card?.types
      if (!types.isNullOrEmpty()) {
        OverlappingTypeRow {
          types.forEach { type ->
            TypeIcon(type)
          }
        }
      }
    }

    if (card != null) {
      InfoChipGroup(
        modifier = Modifier
          .fillMaxWidth()
          .padding(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
          ),
      ) {
        InfoChip("Set") {
          Text(card.expansion.name)
        }

        InfoChip("Number") {
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

        card.artist?.let { artist ->
          InfoChip("Artist") {
            Text(artist)
          }
        }

        card.rarity?.let { rarity ->
          InfoChip("Rarity") {
            Text(rarity)
          }
        }

        if (card.subtypes.isNotEmpty()) {
          InfoChip("Subtypes") {
            Text(card.subtypes.joinToString())
          }
        }

        if (!card.weaknesses.isNullOrEmpty()) {
          InfoChip("Weaknesses") {
            Row(
              horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
              card.weaknesses?.forEach { effect ->
                TypeIcon(
                  type = effect.type,
                  modifier = Modifier.size(24.dp),
                )
                Text(effect.value)
              }
            }
          }
        }

        if (!card.resistances.isNullOrEmpty()) {
          InfoChip("Resistances") {
            Row(
              horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
              card.resistances?.forEach { effect ->
                TypeIcon(
                  type = effect.type,
                  modifier = Modifier.size(24.dp),
                )
                Text(effect.value)
              }
            }
          }
        }

        card.convertedRetreatCost?.let { retreatCost ->
          InfoChip("Retreat Cost") {
            Row(
              horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
              repeat(retreatCost) {
                TypeIcon(
                  type = Type.COLORLESS,
                  modifier = Modifier.size(24.dp),
                )
              }
            }
          }
        }
      }

      Spacer(Modifier.height(16.dp))
    }
  }
}
