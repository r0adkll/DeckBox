package app.deckbox.decks.impl.export

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.deckbox.common.settings.DeckBoxSettings
import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.model.Card
import app.deckbox.core.model.Deck
import app.deckbox.core.model.Evolution
import app.deckbox.core.model.Stacked
import app.deckbox.core.model.SuperType
import app.deckbox.core.settings.ImageExportConfig
import app.deckbox.features.cards.public.CardRepository
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.SizeResolver
import com.seiko.imageloader.toPainter
import kotlin.math.ceil
import kotlin.math.min
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class ImageConverter(
  private val cardRepository: CardRepository,
  private val imageLoader: ImageLoader,
  private val settings: DeckBoxSettings,
  private val dispatcherProvider: DispatcherProvider,
) {
  suspend fun convert(deck: Deck): ImageBitmap {
    val exportConfig = settings.imageExportConfig

    val cards = loadCards(deck)
    val images = loadCardImages(exportConfig, cards)

    val columns = min(cards.size, exportConfig.maxColumns)
    val rows = ceil(cards.size.toFloat() / columns.toFloat())

    val rowHeightPx = exportConfig.widthPx.toFloat() * CardAspectRatio
    val totalWidthPx = columns * exportConfig.widthPx.toFloat() + ((columns - 1) * exportConfig.horizontalSpacing)
    val totalHeightPx = rows * rowHeightPx + ((rows - 1) * exportConfig.verticalSpacing)

    val size = Size(totalWidthPx, totalHeightPx)

    val drawScope = CanvasDrawScope()
    val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    val canvas = Canvas(bitmap)

    drawScope.draw(
      density = Density(1f),
      layoutDirection = LayoutDirection.Ltr,
      canvas = canvas,
      size = size,
    ) {
      images.forEachIndexed { index, image ->
        val spacingX = (index % columns).minus(1).coerceAtLeast(0) * exportConfig.horizontalSpacing
        val spacingY = (index / columns).minus(1).coerceAtLeast(0) * exportConfig.verticalSpacing
        val positionX = (index % columns) * exportConfig.widthPx.toFloat() + spacingX.toFloat()
        val positionY = (index / columns) * rowHeightPx + spacingY.toFloat()
        val cardSize = Size(exportConfig.widthPx.toFloat(), exportConfig.widthPx.toFloat() * CardAspectRatio)

        with (image.painter) {
          translate(positionX, positionY) {
            draw(cardSize)
            renderCardCount(
              count = image.card.count,
              size = cardSize,
              color = Color(0xFF006493),
              textSize = 128.sp,
              padding = PaddingValues(
                horizontal = 64.dp,
                vertical = 88.dp
              ),
              cornerRadius = 32.dp,
            )
          }
        }
      }
    }

    return bitmap
  }

  private suspend fun loadCards(deck: Deck): List<Stacked<Card>> {
    val cards = cardRepository.observeCardsForDeck(deck.id).first()
    val pokemon = cards.filter { it.card.supertype == SuperType.POKEMON }
    val evolutionSortedCards = Evolution.create(pokemon)
      .sortedBy { it.size }
      .flatMap { it.nodes.flatMap { it.cards } }
    val trainers = cards.filter { it.card.supertype == SuperType.TRAINER }
    val energy = cards.filter { it.card.supertype == SuperType.ENERGY }

    return evolutionSortedCards + trainers + energy
  }

  private suspend fun loadCardImages(
    config: ImageExportConfig,
    cards: List<Stacked<Card>>,
  ): List<CardImage> {
    return withContext(dispatcherProvider.io) {
      val cardHeight = config.widthPx * CardAspectRatio

      val requests = cards.map { stack ->
        val request = ImageRequest {
          data(stack.card.image.small)
          size(SizeResolver(Size(config.widthPx.toFloat(), cardHeight)))
        }

        async {
          val result = imageLoader.async(request)
            .first { it !is ImageAction.Loading }

          val painter = when (result) {
            is ImageResult.OfPainter -> result.painter
            is ImageResult.OfBitmap -> result.bitmap.toPainter()
            is ImageResult.OfImage -> result.image.toPainter()
            else -> null
          }

          stack.card.id to painter?.let { CardImage(stack, it) }
        }
      }

      requests.awaitAll()
        .sortedBy { (id, _) -> cards.indexOfFirst { it.card.id == id } }
        .mapNotNull { it.second }
    }
  }
}

data class CardImage(
  val card: Stacked<Card>,
  val painter: Painter,
)

const val CardAspectRatio = 1.3959184f
