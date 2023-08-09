package app.deckbox.features.decks.public.ui.slices

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable

interface ComposeSlice {
  val name: String

  @Composable
  fun ColumnScope.Content()
}
