package app.deckbox.features.boosterpacks.ui.builder.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CatchingPokemon
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import app.deckbox.common.compose.icons.rounded.Energy
import app.deckbox.common.compose.icons.rounded.Wrench
import app.deckbox.common.compose.widgets.CardEditor
import app.deckbox.common.compose.widgets.PokemonCard
import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import app.deckbox.core.model.SuperType
import app.deckbox.features.boosterpacks.ui.builder.model.CardUiModel

private val ListSpacing = 16.dp
private val CardSpacing = 8.dp

@Composable
internal fun BoosterPackCardList(
  isEditing: Boolean,
  models: List<CardUiModel>,
  onCardClick: (Stacked<Card>) -> Unit,
  onCardLongClick: (Stacked<Card>) -> Unit,
  onAddCardClick: (Stacked<Card>) -> Unit,
  onRemoveCardClick: (Stacked<Card>) -> Unit,
  modifier: Modifier = Modifier,
  lazyGridState: LazyGridState = rememberLazyGridState(),
  columns: Int = 4,
  contentPadding: PaddingValues = PaddingValues(),
) {
  LazyVerticalGrid(
    modifier = modifier,
    state = lazyGridState,
    contentPadding = PaddingValues(
      top = contentPadding.calculateTopPadding(),
      start = ListSpacing,
      end = ListSpacing,
      bottom = contentPadding.calculateBottomPadding(),
    ),
    columns = GridCells.Fixed(columns),
    horizontalArrangement = Arrangement.spacedBy(CardSpacing),
    verticalArrangement = Arrangement.spacedBy(CardSpacing),
  ) {
    itemsIndexed(
      items = models,
      key = { _, item -> item.id },
      span = { _, model ->
        when (model) {
          is CardUiModel.SectionHeader -> GridItemSpan(maxLineSpan)
          is CardUiModel.Single -> GridItemSpan(1)
        }
      },
      contentType = { _, item -> item::class },
    ) { _, model ->
      when (model) {
        is CardUiModel.Single -> {
          CardEditor(
            count = model.card.count,
            isEditing = isEditing,
            onAddClick = { onAddCardClick(model.card) },
            onRemoveClick = { onRemoveCardClick(model.card) },
          ) {
            PokemonCard(
              card = model.card.card,
              count = model.card.count.takeIf { it > 1 },
              collected = model.card.collected,
              onClick = {
                onCardClick(model.card)
              },
              onLongClick = { onCardLongClick(model.card) },
            )
          }
        }

        is CardUiModel.SectionHeader -> Header(
          title = model.title(),
          count = model.count,
          superType = model.superType,
          modifier = Modifier.overWidth(ListSpacing * 2),
        )
      }
    }
  }
}

private fun Modifier.overWidth(overWidth: Dp): Modifier {
  return when {
    overWidth > 0.dp -> layout { measurable, constraints ->
      val placeable = measurable.measure(
        constraints.copy(maxWidth = constraints.maxWidth + overWidth.roundToPx()),
      )
      layout(placeable.width, placeable.height) {
        placeable.place(0, 0)
      }
    }

    else -> this
  }
}

@Composable
private fun Header(
  title: String,
  count: Int,
  superType: SuperType,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(
        vertical = 8.dp,
        horizontal = 16.dp,
      ),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    CompositionLocalProvider(
      LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
      Icon(
        when (superType) {
          SuperType.ENERGY -> Icons.Rounded.Energy
          SuperType.TRAINER -> Icons.Rounded.Wrench
          else -> Icons.Rounded.CatchingPokemon
        },
        contentDescription = null,
      )
      Spacer(Modifier.width(6.dp))
      Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
      )
      Spacer(Modifier.weight(1f))
      Text(
        text = count.toString(),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
      )
    }
  }
}
