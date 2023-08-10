package app.deckbox.core.settings

import app.deckbox.core.settings.DeckCardSlice.Actions
import app.deckbox.core.settings.DeckCardSlice.Description
import app.deckbox.core.settings.DeckCardSlice.Header
import app.deckbox.core.settings.DeckCardSlice.Images
import app.deckbox.core.settings.DeckCardSlice.Tags

data class DeckCardConfig(
  val slices: List<DeckCardSlice>,
) {

  companion object {
    val DEFAULT by lazy {
      DeckCardConfig(
        listOf(
          Header.Export,
          Images.Fanned,
          Tags,
          Description,
          Actions.Full,
        ),
      )
    }
  }
}
