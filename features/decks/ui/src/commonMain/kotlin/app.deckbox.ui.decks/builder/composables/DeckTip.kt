package app.deckbox.ui.decks.builder.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CatchingPokemon
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.rounded.Energy
import app.deckbox.common.compose.icons.rounded.Wrench
import app.deckbox.ui.decks.builder.model.CardUiModel

@Composable
internal fun DeckTip(
  tip: CardUiModel.Tip,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier
      .padding(
        horizontal = 16.dp,
      ),
    colors = cardColors(tip),
  ) {

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .padding(horizontal = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = tipTitle(tip),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.weight(1f),
      )
      Spacer(Modifier.width(16.dp))
      Icon(
        tipIcon(tip),
        contentDescription = null,
      )
    }

    tips(tip).forEach { tipText ->
      Row(
        modifier = Modifier.padding(
          vertical = 2.dp,
        )
      ) {
        Spacer(Modifier.width(16.dp))
        Text(
          text = "•",
          modifier = Modifier,
        )
        Spacer(Modifier.width(8.dp))
        Text(
          text = tipText,
        )
        Spacer(Modifier.width(16.dp))
      }
    }

    Spacer(Modifier.height(16.dp))

    OutlinedButton(
      colors = ButtonDefaults.outlinedButtonColors(contentColor = actionColors(tip)),
      border = BorderStroke(1.dp, actionColors(tip)),
      onClick = onClick,
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth(),
    ) {
      Text(tipAction(tip))
    }

    Spacer(Modifier.height(16.dp))
  }
}

@Composable
private fun cardColors(tip: CardUiModel.Tip): CardColors = when (tip) {
  CardUiModel.Tip.Pokemon -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
  CardUiModel.Tip.Trainer -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
  CardUiModel.Tip.Energy -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
}

@Composable
private fun tipIcon(tip: CardUiModel.Tip): ImageVector = when (tip) {
  CardUiModel.Tip.Pokemon -> Icons.Rounded.CatchingPokemon
  CardUiModel.Tip.Energy -> Icons.Rounded.Energy
  CardUiModel.Tip.Trainer -> Icons.Rounded.Wrench
}

@Composable
private fun tipTitle(tip: CardUiModel.Tip): String = when (tip) {
  CardUiModel.Tip.Pokemon -> "Adding Pokémon Cards"
  CardUiModel.Tip.Trainer -> "Adding Trainer Cards"
  CardUiModel.Tip.Energy -> "Adding Energy Cards"
}

@Composable
private fun tipAction(tip: CardUiModel.Tip): String = when (tip) {
  CardUiModel.Tip.Pokemon -> "Search for Pokémon cards"
  CardUiModel.Tip.Trainer -> "Search for Trainer cards"
  CardUiModel.Tip.Energy -> "Search for Energy cards"
}

@Composable
private fun actionColors(tip: CardUiModel.Tip) = when (tip) {
  CardUiModel.Tip.Pokemon -> MaterialTheme.colorScheme.primary
  CardUiModel.Tip.Trainer -> MaterialTheme.colorScheme.secondary
  CardUiModel.Tip.Energy -> MaterialTheme.colorScheme.tertiary
}

private fun tips(tip: CardUiModel.Tip): List<String> = when (tip) {
  CardUiModel.Tip.Pokemon -> listOf(
    "Choose Pokémon that align with your strategy, considering types and abilities.",
    "Include a mix of Basic and Evolution Pokémon.",
    "Balance HP, attacks, and resistances to fit your strategy.",
    "Select Pokémon that work well together in terms of synergy.",
    "Aim for a balanced count of each Pokémon card in your deck.",
  )

  CardUiModel.Tip.Trainer -> listOf(
    "Include Item cards for immediate effects (draw, search, healing).",
    "Add Supporter cards for powerful one-time effects (draw, search, disruption).",
    "Choose Stadium cards that benefit your strategy and hinder opponents.",
    "Prioritize draw and search cards for consistent gameplay.",
    "Consider Trainer cards that accelerate energy, heal, or control the field.",
    "Include cards to counter common strategies or adapt to metagame trends.",
  )

  CardUiModel.Tip.Energy -> listOf(
    "Include 10-15 energy cards matching your Pokémon types.",
    "Consider special energy cards that provide extra benefits.",
    "Balance energy distribution based on your Pokémon's attack requirements.",
    "Adapt energy count to your strategy's speed and energy consumption.",
  )
}
