package app.deckbox.features.decks.public.ui.slices

import app.deckbox.core.settings.DeckCardSlice

/**
 * The central place were we register our DeckCard Compose rendering
 * slices against their config counter parts so that the DeckCard Composable
 * can easlily pull the slice they need to render in a compose way
 */
object SliceRegistry {

  private val slices = mutableMapOf<DeckCardSlice, ComposeSlice>()

  // TODO: Maybe do some custom KSP stuff for this?
  init {
    register(ActionSlice())
    register(DescriptionSlice())
    register(ExportHeaderSlice())
    register(ThumbnailHeaderSlice())
    register(FannedImageSlice())
    register(GridImageSlice())
    register(TagSlice())
  }

  fun slice(config: DeckCardSlice): ComposeSlice {
    return slices[config]
      ?: throw IllegalStateException("ComposeSlice not registered for $config")
  }

  private fun register(slice: ComposeSlice) {
    slices[slice.config] = slice
  }
}
