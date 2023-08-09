package app.deckbox.common.compose.icons.types

import Dragon
import androidx.compose.ui.graphics.vector.ImageVector
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.core.model.Type

fun Type.asImageVector(): ImageVector = when (this) {
  Type.COLORLESS -> DeckBoxIcons.Types.Colorless
  Type.DARKNESS -> DeckBoxIcons.Types.Dark
  Type.DRAGON -> DeckBoxIcons.Types.Dragon
  Type.FAIRY -> DeckBoxIcons.Types.Fairy
  Type.FIGHTING -> DeckBoxIcons.Types.Fighting
  Type.FIRE -> DeckBoxIcons.Types.Fire
  Type.GRASS -> DeckBoxIcons.Types.Grass
  Type.LIGHTNING -> DeckBoxIcons.Types.Lightning
  Type.METAL -> DeckBoxIcons.Types.Steel
  Type.PSYCHIC -> DeckBoxIcons.Types.Psychic
  Type.WATER -> DeckBoxIcons.Types.Water
  Type.UNKNOWN -> DeckBoxIcons.Types.Colorless
}
