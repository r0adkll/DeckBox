package app.deckbox.core.model

import kotlinx.datetime.LocalDate

typealias CardId = String

data class Card(
  val id: CardId,
  val name: String,
  val image: Image,
  val supertype: SuperType,
  val subtypes: List<String>,
  val level: String?,
  val hp: Int?,
  val types: List<Type>?,
  val evolvesFrom: String?,
  val evolvesTo: List<String>?,
  val rules: List<String>?,
  val ancientTrait: Ability?,
  val abilities: List<Ability>?,
  val attacks: List<Attack>?,
  val weaknesses: List<Effect>?,
  val resistances: List<Effect>?,
  val retreatCost: List<Type>?,
  val convertedRetreatCost: Int?,
  val number: String,
  val expansion: Expansion,
  val artist: String?,
  val rarity: String?,
  val flavorText: String?,
  val nationalPokedexNumbers: List<Int>?,
  val legalities: Legalities?,
  val tcgPlayer: TcgPlayer?,
  val cardMarket: CardMarket?,
) {

  data class Attack(
    val cost: List<Type>?,
    val name: String,
    val text: String?,
    val damage: String?,
    val convertedEnergyCost: Int,
  )

  data class Ability(
    val name: String,
    val text: String,
    val type: String?,
  )

  data class Effect(
    val type: Type,
    val value: String,
  )

  data class Image(
    val small: String,
    val large: String,
  )

  enum class Variant {
    Normal,
    Holofoil,
    ReverseHolofoil,
    FirstEditionNormal,
    FirstEditionHolofoil,
    ;

    fun amountIf(other: Variant, amount: Int): Int = if (this == other) amount else 0
  }

  data class TcgPlayer(
    val url: String,
    val updatedAt: LocalDate,
    val prices: Prices?,
  ) {
    data class Prices(
      val normal: Price?,
      val holofoil: Price?,
      val reverseHolofoil: Price?,
      val firstEditionHolofoil: Price?,
      val firstEditionNormal: Price?,
    ) {
      val isEmpty: Boolean
        get() = normal == null &&
          holofoil == null &&
          reverseHolofoil == null &&
          firstEditionNormal == null &&
          firstEditionHolofoil == null
    }

    data class Price(
      val low: Double? = null,
      val mid: Double? = null,
      val high: Double? = null,
      val market: Double? = null,
      val directLow: Double? = null,
    )

    fun forVariant(variant: Variant): Price? {
      return when (variant) {
        Variant.Normal -> prices?.normal
        Variant.Holofoil -> prices?.holofoil
        Variant.ReverseHolofoil -> prices?.reverseHolofoil
        Variant.FirstEditionNormal -> prices?.firstEditionNormal
        Variant.FirstEditionHolofoil -> prices?.firstEditionHolofoil
      }
    }
  }

  data class CardMarket(
    val url: String,
    val updatedAt: LocalDate,
    val prices: Prices?,
  ) {

    data class Prices(
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
}
