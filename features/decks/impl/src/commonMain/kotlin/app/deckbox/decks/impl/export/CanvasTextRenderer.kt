package app.deckbox.decks.impl.export

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

expect fun DrawScope.renderCardCount(
  textSize: TextUnit,
  padding: PaddingValues,
  cornerRadius: Dp,
  count: Int,
  size: Size,
  color: androidx.compose.ui.graphics.Color,
)
