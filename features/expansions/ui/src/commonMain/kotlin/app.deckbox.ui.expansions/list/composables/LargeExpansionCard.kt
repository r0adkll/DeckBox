package app.deckbox.ui.expansions.list.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FileDownload
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.widgets.CardHeader
import app.deckbox.common.compose.widgets.ImageAvatar
import app.deckbox.common.compose.widgets.TagChip
import app.deckbox.common.compose.widgets.TagStyle
import app.deckbox.common.compose.widgets.Tags
import app.deckbox.core.extensions.readableFormat
import app.deckbox.core.model.Expansion
import app.deckbox.core.model.Legality
import cafe.adriel.lyricist.LocalStrings
import com.seiko.imageloader.rememberImagePainter
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LargeExpansionCard(
  expansion: Expansion,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    onClick = onClick,
  ) {
    CardHeader(
      leading = {
        ImageAvatar(
          url = expansion.images.symbol,
          modifier = Modifier
            .background(
              color = MaterialTheme.colorScheme.primaryContainer,
              shape = CircleShape,
            ),
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
    Logo(
      url = expansion.images.logo,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp),
    )
//    Tags(
//      tags = buildList {
//        if (expansion.legalities?.standard == Legality.LEGAL) {
//          add(LocalStrings.current.standardLegality)
//        }
//        if (expansion.legalities?.expanded == Legality.LEGAL) {
//          add(LocalStrings.current.expandedLegality)
//        }
//      },
//      modifier = Modifier
//        .padding(
//          horizontal = 16.dp,
//          vertical = 8.dp,
//        ),
//    )
    CollectionCounter(
      printedTotal = expansion.printedTotal,
    )

    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(
        start = 16.dp,
      )
    ) {
      if (expansion.legalities?.standard == Legality.LEGAL) {
        Text(
          text = LocalStrings.current.standardLegality,
          style = MaterialTheme.typography.labelMedium,
        )
      } else if (expansion.legalities?.expanded == Legality.LEGAL) {
        Text(
          text = LocalStrings.current.expandedLegality,
          style = MaterialTheme.typography.labelMedium,
        )
      } else if (expansion.legalities?.unlimited == Legality.LEGAL) {
        Text(
          text = LocalStrings.current.unlimitedLegality,
          style = MaterialTheme.typography.labelMedium,
        )
      }

      Text(
        text = LocalStrings.current.expansionReleaseDate(expansion.releaseDate.readableFormat),
        textAlign = TextAlign.End,
        style = MaterialTheme.typography.labelSmall.copy(
          color = MaterialTheme.colorScheme.outline,
        ),
        modifier = Modifier
          .weight(1f)
          .padding(16.dp),
      )
    }
  }
}

@Composable
private fun Logo(
  url: String,
  modifier: Modifier = Modifier,
) {
  val logoPainter = key(url) { rememberImagePainter(url) }
  Image(
    painter = logoPainter,
    contentDescription = "Logo", // TODO Lyracist
    contentScale = ContentScale.Fit,
    modifier = modifier.height(150.dp),
  )
}

@Composable
private fun CollectionCounter(
  printedTotal: Int,
  modifier: Modifier = Modifier,
) {
  val count = remember {
    Random.nextInt(printedTotal) // TODO: Collection Implementation
  }
  Column(
    modifier
      .padding(top = 8.dp)
      .padding(horizontal = 16.dp),
  ) {
    Row {
      Text(
        text = LocalStrings.current.collection,
        style = MaterialTheme.typography.labelMedium,
      )
      Text(
        text = LocalStrings.current.collectionCountOfTotal(count, printedTotal),
        style = MaterialTheme.typography.labelMedium.copy(textAlign = TextAlign.End),

        modifier = Modifier.weight(1f),
      )
    }

    CollectionBar(
      modifier = Modifier.padding(top = 4.dp),
      count = count,
      printedTotal = printedTotal,
    )
  }
}
