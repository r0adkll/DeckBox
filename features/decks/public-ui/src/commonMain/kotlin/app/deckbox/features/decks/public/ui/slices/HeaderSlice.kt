package app.deckbox.features.decks.public.ui.slices

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.extensions.timeAgo
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.icons.outline.Decks
import app.deckbox.common.compose.icons.outline.Export
import app.deckbox.common.compose.widgets.CardHeader
import app.deckbox.common.compose.widgets.DropdownHeaderText
import app.deckbox.common.compose.widgets.DropdownIconButton
import app.deckbox.common.compose.widgets.TonalIcon
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.DeckCardSlice
import app.deckbox.features.decks.public.ui.DeckExportOption
import app.deckbox.features.decks.public.ui.events.DeckCardEvent
import cafe.adriel.lyricist.LocalStrings
import deckbox.features.decks.public_ui.generated.resources.Res
import deckbox.features.decks.public_ui.generated.resources.export_deck_menu_header
import org.jetbrains.compose.resources.stringResource

class ExportHeaderSlice : ComposeSlice {
  override val config: DeckCardSlice = DeckCardSlice.Header.Export

  @Composable
  override fun ColumnScope.Content(
    deck: Deck,
    eventSink: (DeckCardEvent) -> Unit,
  ) {
    CardHeader(
      leading = {
        TonalIcon(
          DeckBoxIcons.Outline.Decks,
          contentDescription = null,
          modifier = Modifier.padding(top = 4.dp),
        )
      },
      title = {
        Text(
          text = deck.name.ifBlank { LocalStrings.current.deckDefaultNoName },
          style = MaterialTheme.typography.titleLarge,
        )
      },
      subtitle = {
        Text(deck.updatedAt.timeAgo)
      },
      trailing = {
        ExportDropdownButton(eventSink)
      },
    )
  }
}

class ThumbnailHeaderSlice : ComposeSlice {
  override val config: DeckCardSlice = DeckCardSlice.Header.Thumbnail

  @Composable
  override fun ColumnScope.Content(
    deck: Deck,
    eventSink: (DeckCardEvent) -> Unit,
  ) {
    CardHeader(
      title = {
        Text(
          text = deck.name.ifBlank { LocalStrings.current.deckDefaultNoName },
          style = MaterialTheme.typography.titleLarge,
        )
      },
      subtitle = {
        Text(deck.updatedAt.timeAgo)
      },
      trailing = {
        ExportDropdownButton(eventSink)
      },
    )
  }
}

@Composable
private fun ExportDropdownButton(
  eventSink: (DeckCardEvent) -> Unit,
  modifier: Modifier = Modifier,
) {
  DropdownIconButton(
    modifier = modifier,
    options = DeckExportOption.entries,
    offset = DpOffset(0.dp, (-56).dp),
    onOptionClick = { option ->
      eventSink(DeckCardEvent.Export(option))
    },
    optionIcon = { option ->
      Icon(
        when (option) {
          DeckExportOption.Text -> Icons.Rounded.TextFields
          DeckExportOption.Image -> Icons.Rounded.Image
        },
        contentDescription = null,
      )
    },
    optionText = { Text(it.text()) },
    title = {
      DropdownHeaderText(
        text = stringResource(Res.string.export_deck_menu_header),
      )
    },
    icon = {
      Icon(DeckBoxIcons.Outline.Export, contentDescription = null)
    },
  )
}
