package app.deckbox.features.cards.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.UnfoldLess
import androidx.compose.material.icons.rounded.UnfoldMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.widgets.CollectionCounterListItem
import app.deckbox.core.model.Card
import app.deckbox.core.model.CollectionCount
import cafe.adriel.lyricist.LocalStrings

@Composable
fun CollectionCard(
  count: CollectionCount,
  tcgPlayer: Card.TcgPlayer?,
  onIncrementClick: (Card.Variant) -> Unit,
  onDecrementClick: (Card.Variant) -> Unit,
  modifier: Modifier = Modifier,
) {
  var isExpanded by remember { mutableStateOf(false) }

  OutlinedCard(
    modifier = modifier,
  ) {
    CollectionHeader(
      title = LocalStrings.current.inCollection,
      totalCount = count.totalCount,
      isExpanded = isExpanded,
      onToggleCollapseClick = { isExpanded = !isExpanded },
    )

    AnimatedVisibility(isExpanded) {
      val counts = Card.Variant.entries.mapNotNull { variant ->
        val variantPrice = tcgPlayer?.forVariant(variant)
        val variantCount = count.forVariant(variant)
        if (variantPrice != null || variantCount > 0) {
          VariantCount(variant, variantCount)
        } else if (tcgPlayer?.prices?.isEmpty == true) {
          // If there is explicitly no TCG pricing information then at least show the
          // "Normal" variant.
          VariantCount(Card.Variant.Normal, variantCount)
        } else {
          null
        }
      }

      CounterColumn(
        counts = counts,
        onIncrementClick = onIncrementClick,
        onDecrementClick = onDecrementClick,
      )
    }
  }
}

@Composable
private fun CollectionHeader(
  title: String,
  totalCount: Int,
  isExpanded: Boolean,
  onToggleCollapseClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier.clickable { onToggleCollapseClick() },
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Row(
      modifier = Modifier
        .weight(1f)
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      val shape = RoundedCornerShape(50)
      Text(
        text = totalCount.toString(),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
          .background(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = shape,
          )
          .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.secondary,
            shape = shape,
          )
          .padding(
            horizontal = 4.dp,
            vertical = 2.dp,
          ),
      )
      Spacer(Modifier.width(8.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
      )
    }

    IconButton(
      onClick = onToggleCollapseClick,
      modifier = Modifier.padding(end = 16.dp),
    ) {
      Icon(
        if (isExpanded) Icons.Rounded.UnfoldLess else Icons.Rounded.UnfoldMore,
        contentDescription = null,
      )
    }
  }
}

@Immutable
data class VariantCount(
  val variant: Card.Variant,
  val count: Int,
)

@Composable
private fun CounterColumn(
  counts: List<VariantCount>,
  onIncrementClick: (Card.Variant) -> Unit,
  onDecrementClick: (Card.Variant) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(modifier) {
    counts.forEach { count ->
      CollectionCounterListItem(
        title = when (count.variant) {
          Card.Variant.Normal -> LocalStrings.current.tcgPlayerNormal
          Card.Variant.Holofoil -> LocalStrings.current.tcgPlayerHolofoil
          Card.Variant.ReverseHolofoil -> LocalStrings.current.tcgPlayerReverseHolofoil
          Card.Variant.FirstEditionNormal -> LocalStrings.current.tcgPlayerFirstEditionNormal
          Card.Variant.FirstEditionHolofoil -> LocalStrings.current.tcgPlayerFirstEditionHolofoil
        },
        count = count.count,
        onIncrementClick = { onIncrementClick(count.variant) },
        onDecrementClick = { onDecrementClick(count.variant) },
      )
    }
  }
}
