package app.deckbox.playtest.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Healing
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.types.OverlappingTypeRow
import app.deckbox.common.compose.icons.types.asImageVector
import app.deckbox.common.compose.theme.PokemonTypeColor.toColor
import app.deckbox.common.compose.theme.PokemonTypeColor.toContentColor
import app.deckbox.common.compose.widgets.PokemonCard
import app.deckbox.core.model.Card
import app.deckbox.core.model.SubType
import app.deckbox.core.model.Type
import app.deckbox.core.model.energyTypeFromCardName
import app.deckbox.playtest.ui.model.PlayedCard
import app.deckbox.playtest.ui.util.CardWindowUtil
import com.seiko.imageloader.rememberImagePainter
import kotlin.math.roundToInt
import kotlinx.collections.immutable.ImmutableList

private val StackedCardOffset = 2.dp
private const val StackedZIndexOffset = 0.01f
private const val CardRatio = 1.3969986f

@Composable
internal fun InPlayCard(
  card: PlayedCard,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val arenaCardWidth = LocalArenaCardWidth.current

  Layout(
    modifier = modifier
      .width(arenaCardWidth),
    content = {
      card.pokemons.forEachIndexed { index, card ->
        PokemonCard(
          card = card,
          onClick = onClick,
          modifier = Modifier
            .layoutId(CardId(card.id))
            .offset {
              IntOffset(0, index * StackedCardOffset.roundToPx())
            },
        )
      }

      card.damage.takeIf { it > 0 }?.let { dmg ->
        DamageIndicator(
          damage = dmg,
          modifier = Modifier.layoutId(Damage),
        )
      }

      if (card.energy.isNotEmpty()) {
        EnergyRow(
          energy = card.energy,
          modifier = Modifier.layoutId(Energy),
        )
      }

      if (card.tools.isNotEmpty()) {
        ToolsColumn(
          tools = card.tools,
          modifier = Modifier.layoutId(Tools),
        )
      }

      if (card.isBurned || card.isPoisoned) {
        BurnPoisonMarker(
          isBurned = card.isBurned,
          isPoisoned = card.isPoisoned,
          modifier = Modifier.layoutId(StatusMarker),
        )
      }
    },
  ) { measurables, constraints ->
    // Take the width as the source of truth
    val adjustedHeight = (constraints.maxWidth * CardRatio).roundToInt()
    val cardConstraints = constraints.copy(
      maxHeight = adjustedHeight,
    )
    val looseCardConstraints = cardConstraints.copy(
      minWidth = 0,
    )

    val pokemons = measurables.filter {
      it.layoutId is CardId
    }.map { it to it.measure(cardConstraints) }

    val damage = measurables.find { it.layoutId is Damage }
    val damagePlaceable = damage?.measure(looseCardConstraints)

    val energy = measurables.find { it.layoutId is Energy }
    val energyPlaceable = energy?.measure(constraints)

    val tools = measurables.find { it.layoutId is Tools }
    val toolsPlaceable = tools?.measure(cardConstraints)

    val statusMarker = measurables.find { it.layoutId is StatusMarker }
    val statusMarkerPlaceable = statusMarker?.measure(looseCardConstraints)

    layout(constraints.maxWidth, adjustedHeight) {
      pokemons.forEachIndexed { index, (_, placeable) ->
        placeable.place(0, 0, zIndex = (index * StackedZIndexOffset))
      }

      /**
       * _____________
       * |    [ • 20 ]
       * |           |
       * |           |
       * |           |
       * |           |
       * |           |
       * |___________|
       */
      damagePlaceable?.apply {
        place(
          x = constraints.maxWidth - width,
          y = 0,
          zIndex = 1f,
        )
      }

      /**
       * _____________
       * |           |
       * |           |
       * |           |
       * |           |
       * |           |
       * |           |
       * |-----OOOOO-|
       */
      energyPlaceable?.apply {
        place(
          x = constraints.maxWidth - width,
          y = adjustedHeight - (height / 2),
          zIndex = 1f,
        )
      }

      /**
       * The Composable will be responsible for laying out multiple
       * tool attachments and should be a column spanning the height
       * of the card
       * _____________
       * |           |
       * |           |
       * [````]      |
       * [____]      |
       * |           |
       * |___________|
       */
      toolsPlaceable?.apply {
        place(
          x = -(width / 4),
          y = 0,
          zIndex = -1f,
        )
      }

      /**
       * _____________
       * |           |
       * |           |
       * |    [O]    |
       * |           |
       * |           |
       * |___________|
       */
      statusMarkerPlaceable?.apply {
        place(
          x = constraints.maxWidth / 2 - width / 2,
          y = adjustedHeight / 2 - height / 2,
          zIndex = 1f,
        )
      }
    }
  }
}

@Composable
internal fun DamageIndicator(
  damage: Int,
  modifier: Modifier = Modifier,
) {
  Text(
    text = (-damage * 10).toString(),
    style = MaterialTheme.typography.labelMedium,
    fontWeight = FontWeight.SemiBold,
    modifier = modifier
      .background(
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = RoundedCornerShape(8.dp),
      )
      .padding(
        horizontal = 4.dp,
        vertical = 2.dp,
      ),
  )
}

@Composable
internal fun BurnPoisonMarker(
  isBurned: Boolean,
  isPoisoned: Boolean,
  modifier: Modifier = Modifier,
  size: Dp = 24.dp,
) {
  OverlappingTypeRow(
    modifier = modifier,
  ) {
    if (isBurned) {
      Box(
        modifier = Modifier
          .size(size)
          .background(
            color = Color.Red,
            shape = CircleShape,
          )
          .border(
            width = 1.dp,
            color = Color.White.copy(.5f),
            shape = CircleShape,
          ),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          Icons.Rounded.LocalFireDepartment,
          contentDescription = null,
          modifier = Modifier.size(18.dp),
          tint = Color.White,
        )
      }
    }
    if (isPoisoned) {
      Box(
        modifier = Modifier
          .size(size)
          .background(
            color = Color.Green,
            shape = CircleShape,
          )
          .border(
            width = 1.dp,
            color = Color.White.copy(.5f),
            shape = CircleShape,
          ),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          Icons.Rounded.Healing,
          contentDescription = null,
          modifier = Modifier.size(18.dp),
          tint = Color.White,
        )
      }
    }
  }
}


@Composable
internal fun EnergyRow(
  energy: ImmutableList<Card>,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(2.dp),
  ) {
    energy.forEach { card ->
      if (card.subtypes.contains(SubType.Basic)) {
        val energyType = energyTypeFromCardName(card.name)
        if (energyType != Type.COLORLESS && energyType != Type.UNKNOWN) {
          EnergyTypeBubble(energyType)
        } else {
          EnergyImageBubble(card.image.small)
        }
      }
    }
  }
}

@Composable
private fun EnergyTypeBubble(
  type: Type,
  modifier: Modifier = Modifier,
  size: Dp = 16.dp,
) {
  Image(
    type.asImageVector(),
    contentDescription = type.displayName,
    modifier = modifier
      .size(size)
      .background(
        color = type.toColor(),
        shape = CircleShape,
      )
      .border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.38f),
        shape = CircleShape,
      )
      .padding(2.dp),
    colorFilter = ColorFilter.tint(type.toContentColor(true)),
  )
}

@Composable
private fun EnergyImageBubble(
  imageUrl: String,
  modifier: Modifier = Modifier,
  size: Dp = 16.dp,
) {
  val painter = key(imageUrl) { rememberImagePainter(imageUrl) }
  Box(
    modifier = modifier
      .size(size)
      .border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.38f),
        shape = CircleShape,
      )
      .drawBehind {
        // Compute offsets based on scaled image rectangle zone of a card image
        // then offset image so that the rect is center scaled in the canvas zone.
        if (
          painter.intrinsicSize != Size.Unspecified &&
          !painter.intrinsicSize.width.isNaN() &&
          !painter.intrinsicSize.height.isNaN()
        ) {
          // Image is loaded, compute rectal-window
          with(painter) {
            // We want to scale the image so that its card image window matches the height dimension of this
            // port view.
            val scale = CardWindowUtil.windowScale(painter.intrinsicSize.height, size.toPx())
            val scaledWindow = CardWindowUtil.scaledImageWindow(painter.intrinsicSize, scale)
            val additionalOffsetX = (scaledWindow.width - size.toPx()) / 2

            translate(
              left = -(scaledWindow.left + additionalOffsetX),
              top = -scaledWindow.top,
            ) {
              draw(scaledWindow.size)
            }
          }
        }
      }
      .clip(CircleShape),
  )
}

@Composable
internal fun ToolsColumn(
  tools: ImmutableList<Card>,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
  ) {
    tools.forEach {
    }
  }
}

sealed interface InPlayId

data class CardId(val id: String) : InPlayId
data object Damage : InPlayId
data object Energy : InPlayId
data object Tools : InPlayId
data object StatusMarker : InPlayId
