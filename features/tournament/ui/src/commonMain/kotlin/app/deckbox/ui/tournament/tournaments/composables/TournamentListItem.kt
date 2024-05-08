package app.deckbox.ui.tournament.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import app.deckbox.core.extensions.readableFormat
import app.deckbox.tournament.api.model.Tournament
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter

@Composable
internal fun TournamentListItem(
  tournament: Tournament,
  modifier: Modifier = Modifier,
) {
  ListItem(
    modifier = modifier,
    overlineContent = {
      Text(tournament.date.readableFormat)
    },
    headlineContent = {
      Text(tournament.name)
    },
    supportingContent = {
      Text(tournament.winner.name)
    },
    leadingContent = {
      CountryFlagImage(tournament.country)
    },
  )
}

@Composable
fun CountryFlagImage(
  countryCode: String,
  modifier: Modifier = Modifier,
) {
  val url = countryFlagImageUrl(countryCode)
  val action by rememberImageAction(url)

  Image(
    painter = rememberImageActionPainter(action),
    contentDescription = "Country $countryCode",
    contentScale = ContentScale.FillWidth,
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .width(36.dp),
  )
}

private fun countryFlagImageUrl(countryCode: String): String {
  return "https://flagsapi.com/$countryCode/flat/64.png"
}
