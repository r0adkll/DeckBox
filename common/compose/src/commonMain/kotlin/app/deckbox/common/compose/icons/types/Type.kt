package app.deckbox.common.compose.icons.types

import Dragon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.theme.PokemonTypeColor.toColor
import app.deckbox.common.compose.theme.PokemonTypeColor.toContentColor
import app.deckbox.core.model.Type
import kotlin.math.roundToInt

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

@Composable
fun TypeIcon(
  type: Type,
  modifier: Modifier = Modifier,
) {
  Image(
    type.asImageVector(),
    contentDescription = type.displayName,
    modifier = modifier
      .background(
        color = type.toColor(),
        shape = CircleShape,
      )
      .padding(4.dp),
    colorFilter = ColorFilter.tint(type.toContentColor(true)),
  )
}

@Composable
fun OverlappingTypeRow(
  modifier: Modifier = Modifier,
  overlap: Float = DefaultTypeOverlap,
  content: @Composable () -> Unit,
) {
  Layout(
    modifier = modifier,
    content = content,
  ) { measurables, constraints ->
    val placeables = measurables.map { it.measure(constraints) }

    val width = placeables.foldIndexed(
      initial = 0,
      operation = { index, acc, placeable ->
        if (index == 0) {
          acc + placeable.width
        } else {
          acc + (placeable.width * (1f - overlap)).roundToInt()
        }
      },
    )
    val height = placeables.maxBy { it.height }.height

    layout(width, height) {
      var positionX = 0
      placeables.forEachIndexed { index, placeable ->
        placeable.place(positionX, 0, (placeables.size - index).toFloat())
        positionX += (placeable.width * (1f - overlap)).roundToInt()
      }
    }
  }
}

private const val DefaultTypeOverlap = 0.33333334f // 1/3
