package app.deckbox.network.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class CardResponse(
  @SerialName("data") val cards: List<CardModel>,
  val page: Int,
  val pageSize: Int,
  val count: Int,
  val totalCount: Int,
)

@Serializable
internal class CardSetResponse(
  @SerialName("data") val sets: List<CardSetModel>,
  val page: Int,
  val pageSize: Int,
  val count: Int,
  val totalCount: Int,
)

@Serializable
internal class SimpleResponse(val data: List<String>)

@Serializable
internal class CardModel(
  val id: String,
  val name: String,
  val supertype: String,
  val subtypes: List<String>? = null,
  val level: String? = null,
  val hp: Int? = null,
  val types: List<String>? = null,
  val evolvesFrom: String? = null,
  val evolvesTo: List<String>? = null,
  val rules: List<String>? = null,
  val ancientTrait: AbilityModel? = null,
  val abilities: List<AbilityModel>? = null,
  val attacks: List<AttackModel>? = null,
  val weaknesses: List<EffectModel>? = null,
  val resistances: List<EffectModel>? = null,
  val retreatCost: List<String>? = null,
  val convertedRetreatCost: Int? = null,
  val number: String,
  val set: CardSetModel,
  val artist: String? = null,
  val rarity: String? = null,
  val flavorText: String? = null,
  val nationalPokedexNumbers: List<Int>? = null,
  val legalities: LegalitiesModel? = null,
  val images: CardImageModel,
  val tcgplayer: TcgPlayerModel? = null,
  val cardmarket: CardMarketModel? = null,
)

@Serializable
internal class CardSetModel(
  val id: String,
  val name: String,
  val series: String,
  val printedTotal: Int,
  val total: Int,
  val legalities: LegalitiesModel,
  val ptcgoCode: String? = null,
  val releaseDate: String,
  val updatedAt: String,
  val images: CardSetImagesModel,
)

@Serializable
internal class CardSetImagesModel(
  val symbol: String,
  val logo: String,
)

@Serializable
internal class CardImageModel(
  val small: String,
  val large: String,
)

@Serializable
internal class LegalitiesModel(
  val unlimited: String? = null,
  val standard: String? = null,
  val expanded: String? = null,
)

@Serializable
internal class AbilityModel(
  val name: String,
  val text: String,
  val type: String? = null,
)

@Serializable
internal class AttackModel(
  val cost: List<String>? = null,
  val name: String,
  val text: String? = null,
  val damage: String? = null,
  val convertedEnergyCost: Int,
)

@Serializable
internal class EffectModel(
  val type: String,
  val value: String,
)

@Serializable
internal class TcgPlayerModel(
  val url: String,
  val updated: String? = null,
  val prices: PricesModel? = null,
) {

  @Serializable
  internal class PricesModel(
    val normal: PriceModel? = null,
    val holofoil: PriceModel? = null,
    val reverseHolofoil: PriceModel? = null,
    @SerialName("1stEditionHolofoil") val firstEditionHolofoil: PriceModel? = null,
    @SerialName("1stEditionNormal") val firstEditionNormal: PriceModel? = null,
  )

  @Serializable
  internal class PriceModel(
    val low: Double? = null,
    val mid: Double? = null,
    val high: Double? = null,
    val market: Double? = null,
    val directLow: Double? = null,
  )
}

@Serializable
internal class CardMarketModel(
  val url: String,
  val updatedAt: String? = null,
  val prices: PricesModel? = null,
) {

  @Serializable
  internal class PricesModel(
    val averageSellPrice: Double? = null,
    val lowPrice: Double? = null,
    val trendPrice: Double? = null,
    val germanProLow: Double? = null,
    val suggestedPrice: Double? = null,
    val reverseHoloSell: Double? = null,
    val reverseHoloLow: Double? = null,
    val reverseHoloTrend: Double? = null,
    val lowPriceExPlus: Double? = null,
    val avg1: Double? = null,
    val avg7: Double? = null,
    val avg30: Double? = null,
    val reverseHoloAvg1: Double? = null,
    val reverseHoloAvg7: Double? = null,
    val reverseHoloAvg30: Double? = null,
  )
}
