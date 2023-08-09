package app.deckbox.core.settings

import app.deckbox.core.settings.DeckCardSlice.*

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
          Actions.Full
        )
      )
    }
  }
}
