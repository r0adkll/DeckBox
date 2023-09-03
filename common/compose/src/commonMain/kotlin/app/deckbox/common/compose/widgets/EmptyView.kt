package app.deckbox.common.compose.widgets

import Psyduck
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.icons.Snorlax
import cafe.adriel.lyricist.LocalStrings

private val DefaultPaddingSize = 48.dp
private val IconSpacing = 24.dp
private val ActionSpacing = 24.dp
val DefaultIconSize = 96.dp

@Composable
fun EmptyView(
  label: @Composable () -> Unit,
  image: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  action: (@Composable () -> Unit)? = null,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(DefaultPaddingSize),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    image()

    Spacer(Modifier.height(IconSpacing))

    CompositionLocalProvider(
      LocalTextStyle provides MaterialTheme.typography.bodyLarge.copy(
        textAlign = TextAlign.Center,
      ),
    ) {
      label()
    }

    if (action != null) {
      Spacer(Modifier.height(ActionSpacing))
      action()
    }
  }
}

@Composable
fun DefaultEmptyView(
  modifier: Modifier = Modifier,
  action: (@Composable () -> Unit)? = null,
) {
  EmptyView(
    label = { Text(LocalStrings.current.genericEmptyCardsMessage) },
    image = {
      Image(
        DeckBoxIcons.Snorlax,
        contentDescription = null,
        modifier = Modifier.size(DefaultIconSize),
      )
    },
    modifier = modifier,
    action = action,
  )
}

@Composable
fun SearchEmptyView(
  query: String?,
  modifier: Modifier = Modifier,
) {
  EmptyView(
    label = { Text(LocalStrings.current.genericSearchEmpty(query)) },
    image = {
      Image(
        DeckBoxIcons.Psyduck,
        contentDescription = null,
        modifier = Modifier.size(DefaultIconSize),
      )
    },
    modifier = modifier,
  )
}
