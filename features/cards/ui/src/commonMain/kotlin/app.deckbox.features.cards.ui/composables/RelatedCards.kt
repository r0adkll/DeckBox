package app.deckbox.features.cards.ui.composables

import Psyduck
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.widgets.PokemonCard
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.model.Card

private val DefaultCardHeight = 200.dp

@Composable
internal fun RelatedCards(
  loadState: LoadState<out List<Card>>,
  title: @Composable () -> Unit,
  errorLabel: @Composable () -> Unit,
  emptyLabel: @Composable () -> Unit,
  emptyImage: ImageVector,
  onCardClick: (Card) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
    ) {
      ProvideTextStyle(
        MaterialTheme.typography.labelLarge,
      ) {
        title()
      }
    }
    Spacer(Modifier.height(16.dp))
    Box(
      contentAlignment = Alignment.Center,
    ) {
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .height(DefaultCardHeight),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
      ) {
        loadState.dataOrNull?.let { cards ->
          items(
            items = cards,
            key = { it.id },
          ) { card ->
            PokemonCard(
              card = card,
              onClick = { onCardClick(card) },
            )
          }
        }
      }

      if (loadState is LoadState.Loading) {
        PlaceholderBox {
          SpinningPokeballLoadingIndicator()
        }
      } else if (loadState is LoadState.Error) {
        PlaceholderBox {
          Empty(
            image = DeckBoxIcons.Psyduck,
            label = errorLabel,
          )
        }
      } else if (loadState is LoadState.Loaded && loadState.data.isEmpty()) {
        PlaceholderBox {
          Empty(
            image = emptyImage,
            label = emptyLabel,
          )
        }
      }
    }
  }
}

@Composable
private fun PlaceholderBox(
  modifier: Modifier = Modifier,
  content: @Composable BoxScope.() -> Unit,
) {
  Card(
    modifier = modifier
      .padding(horizontal = 16.dp)
      .fillMaxWidth()
      .height(DefaultCardHeight),
  ) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center,
      content = content,
    )
  }
}

@Composable
private fun Empty(
  image: ImageVector,
  label: @Composable () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Image(
      image,
      contentDescription = null,
      modifier = Modifier.size(56.dp),
    )

    Spacer(Modifier.height(8.dp))

    ProvideTextStyle(
      MaterialTheme.typography.bodyLarge.copy(
        textAlign = TextAlign.Center,
      ),
    ) {
      label()
    }
  }
}
