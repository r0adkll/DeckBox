package app.deckbox.features.boosterpacks.ui.list.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.Booster
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.icons.HyperPotion
import app.deckbox.common.compose.icons.rounded.NewBoosterPack
import app.deckbox.common.compose.widgets.OutlinedIconButton
import app.deckbox.common.compose.widgets.SizedIcon

@Composable
internal fun WelcomeTip(
  onNewClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.fillMaxSize(),
  ) {
    WelcomeCard(
      onNewClick = onNewClick,
      modifier = Modifier.padding(16.dp),
    )
  }
}

@Composable
internal fun WelcomeCard(
  onNewClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier,
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.secondaryContainer,
    ),
  ) {
    Image(
      DeckBoxIcons.Logos.Booster,
      contentDescription = null,
      modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(top = 16.dp),
    )

    Spacer(Modifier.height(4.dp))

    Text(
      text = "Booster Packs",
      style = MaterialTheme.typography.headlineSmall,
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .align(Alignment.CenterHorizontally),
    )

    Spacer(Modifier.height(16.dp))

    Text(
      text = "Booster packs are sets of re-usable cards that you can easily add to decks to make iterating even easier.\n\n" +
        "Try making a pack for staple cards for the current meta.\n\n" +
        "Create energy packs to drop your favorites into your new deck.",
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier.padding(
        start = 16.dp,
        end = 16.dp,
        bottom = 16.dp,
      ),
    )

    OutlinedIconButton(
      onClick = onNewClick,
      colors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.secondary
      ),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
      icon = {
        SizedIcon(
          Icons.Rounded.NewBoosterPack,
          tint = MaterialTheme.colorScheme.secondary,
          contentDescription = null,
        )
      },
      label = { Text("Build a new booster pack") },
      modifier = Modifier
        .fillMaxWidth()
        .padding(
          horizontal = 16.dp,
        ),
    )

    Spacer(Modifier.height(16.dp))
  }
}
