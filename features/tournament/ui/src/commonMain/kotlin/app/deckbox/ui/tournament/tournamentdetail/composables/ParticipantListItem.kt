package app.deckbox.ui.tournament.tournamentdetail.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.deckbox.core.extensions.formattedPlace
import app.deckbox.tournament.api.model.Participant
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter

@Composable
internal fun ParticipantListItem(
  participant: Participant,
  modifier: Modifier = Modifier,
) {
  ListItem(
    modifier = modifier,
    leadingContent = {
      Text(
        text = participant.place.formattedPlace(),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.width(36.dp),
      )
    },
    headlineContent = { Text(participant.name) },
    supportingContent = { Text(participant.archetype.name) },
    trailingContent = { ArchetypeRow(participant.archetype.symbols) },
  )
}

@Composable
private fun ArchetypeRow(
  images: List<String>,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    images.forEach { imageUrl ->
      val action by key(imageUrl) {
        rememberImageAction(imageUrl)
      }

      Image(
        painter = rememberImageActionPainter(action),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier.width(24.dp),
      )
    }
  }
}
