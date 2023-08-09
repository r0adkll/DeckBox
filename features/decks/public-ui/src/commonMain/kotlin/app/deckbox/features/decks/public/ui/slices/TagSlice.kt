package app.deckbox.features.decks.public.ui.slices

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.widgets.Tag
import app.deckbox.common.compose.widgets.TagGroup
import app.deckbox.common.compose.widgets.TagStyle
import app.deckbox.common.compose.widgets.Tags
import app.deckbox.core.model.Deck
import app.deckbox.core.model.Legality
import app.deckbox.core.settings.DeckCardSlice
import app.deckbox.features.decks.public.ui.events.DeckCardEvent
import cafe.adriel.lyricist.LocalStrings

class TagSlice : ComposeSlice {
  override val config: DeckCardSlice = DeckCardSlice.Tags

  @Composable
  override fun ColumnScope.Content(
    deck: Deck,
    eventSink: (DeckCardEvent) -> Unit,
  ) {
    val strings = LocalStrings.current
    val tags = remember {
      buildList {
        if (deck.legalities.standard == Legality.LEGAL) {
          add(Tag(strings.standardLegality))
        } else if (deck.legalities.expanded == Legality.LEGAL) {
          add(Tag(strings.expandedLegality))
        } else if (deck.legalities.unlimited == Legality.LEGAL) {
          add(Tag(strings.unlimitedLegality))
        }
        addAll(deck.tags.map { Tag(it, TagStyle.Outline) })
      }
    }

    TagGroup(
      tags = tags,
      modifier = Modifier
        .fillMaxWidth()
        .padding(
          horizontal = 16.dp,
          vertical = 8.dp,
        ),
    )
  }
}
