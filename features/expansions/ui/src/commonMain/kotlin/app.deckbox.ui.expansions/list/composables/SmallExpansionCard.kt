package app.deckbox.ui.expansions.list.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FileDownload
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.widgets.CardHeader
import app.deckbox.common.compose.widgets.CollectionBar
import app.deckbox.core.extensions.readableFormat
import app.deckbox.core.model.Collected
import app.deckbox.core.model.Expansion
import cafe.adriel.lyricist.LocalStrings
import com.seiko.imageloader.rememberImagePainter

@Composable
internal fun SmallExpansionCard(
  collectedExpansion: Collected<Expansion>,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val expansion = collectedExpansion.item

  Card(
    modifier = modifier.fillMaxWidth(),
    onClick = onClick,
  ) {
    CardHeader(
      leading = {
        val logoPainter = rememberImagePainter(expansion.images.logo)
        Image(
          painter = logoPainter,
          contentDescription = "${expansion.name} Logo",
          modifier = Modifier
            .size(width = 100.dp, height = 48.dp),
        )
      },
      title = {
        Text(expansion.name)
      },
      subtitle = {
        Text(expansion.series)
      },
      trailing = {
        IconButton(onClick = { /*TODO*/ }) {
          Icon(Icons.Rounded.FileDownload, contentDescription = null)
        }
      },
    )

    CollectionBar(
      modifier = Modifier.padding(horizontal = 16.dp),
      count = collectedExpansion.count,
      total = expansion.printedTotal,
    )

    Row(
      Modifier.padding(
        start = 16.dp,
        end = 16.dp,
        top = 8.dp,
        bottom = 16.dp,
      ),
    ) {
      Text(
        text = LocalStrings.current.collectionCountOfTotal(collectedExpansion.count, expansion.printedTotal),
        style = MaterialTheme.typography.labelMedium.copy(
          textAlign = TextAlign.Start,
        ),
        modifier = Modifier.weight(1f),
      )
      Text(
        text = LocalStrings.current.expansionReleaseDate(expansion.releaseDate.readableFormat),
        style = MaterialTheme.typography.labelMedium.copy(
          color = MaterialTheme.colorScheme.outline,
        ),
      )
    }
  }
}
