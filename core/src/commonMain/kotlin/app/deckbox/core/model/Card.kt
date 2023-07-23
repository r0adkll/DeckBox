package app.deckbox.core.model

data class Card(
  val id: String,
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

  data class TcgPlayer(
    val url: String,
    val updatedAt: String?,
    val prices: Prices?,
  ) {

    data class Prices(
      val low: Double? = null,
      val mid: Double? = null,
      val high: Double? = null,
      val market: Double? = null,
      val directLow: Double? = null,
    )
  }

  data class CardMarket(
    val url: String,
    val updatedAt: String?,
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
