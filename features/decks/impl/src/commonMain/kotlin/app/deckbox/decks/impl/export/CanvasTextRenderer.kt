package app.deckbox.decks.impl.export

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope

expect fun DrawScope.renderCardCount(
  count: Int,
  size: Size,
)
