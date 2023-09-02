package app.deckbox.ui.decks.list.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
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
import app.deckbox.common.compose.icons.rounded.NewDeck
import app.deckbox.common.compose.widgets.OutlinedIconButton
import app.deckbox.common.compose.widgets.SizedIcon
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun WelcomeTips(
  onNewDeckClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.fillMaxSize(),
  ) {
    WelcomeCard(
      onNewDeckClick = onNewDeckClick,
      modifier = Modifier.padding(16.dp),
    )
  }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun WelcomeCard(
  onNewDeckClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier,
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer,
    ),
  ) {
    Image(
      painter = painterResource("icon.webp"),
      contentDescription = null,
      modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(top = 16.dp),
    )

    Spacer(Modifier.height(4.dp))

    Text(
      text = "Welcome to DeckBox!",
      style = MaterialTheme.typography.headlineSmall,
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .align(Alignment.CenterHorizontally),
    )

    Spacer(Modifier.height(16.dp))

    Text(
      text = "Try building your very first deck to get started and we'll try to give you some tips along the way.\n\n" +
        "Decide on the main strategy of your deck. Will it focus on fast attacks, energy denial, or special abilities? This choice will guide your card selection.",
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier.padding(
        start = 16.dp,
        end = 16.dp,
        bottom = 16.dp,
      ),
    )

    OutlinedIconButton(
      onClick = onNewDeckClick,
      colors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.primary
      ),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
      icon = {
        SizedIcon(
          Icons.Rounded.NewDeck,
          tint = MaterialTheme.colorScheme.primary,
          contentDescription = null,
        )
      },
      label = { Text("Build a new deck") },
      modifier = Modifier
        .fillMaxWidth()
        .padding(
          horizontal = 16.dp,
        ),
    )

    Spacer(Modifier.height(8.dp))

    OutlinedIconButton(
      onClick = onNewDeckClick,
      colors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.primary
      ),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
      icon = {
        SizedIcon(
          Icons.Rounded.Download,
          tint = MaterialTheme.colorScheme.primary,
          contentDescription = null,
        )
      },
      label = { Text("Import existing deck") },
      modifier = Modifier
        .fillMaxWidth()
        .padding(
          horizontal = 16.dp,
        ),
    )

    Spacer(Modifier.height(16.dp))
  }
}
