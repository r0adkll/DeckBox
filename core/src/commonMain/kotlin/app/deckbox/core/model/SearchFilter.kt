package app.deckbox.core.model

data class SearchFilter(
  val field: SearchField = SearchField.NAME,
  val types: Set<Type> = emptySet(),
  val superTypes: Set<SuperType> = emptySet(),
  val subTypes: Set<String> = emptySet(),
  val expansions: Set<String> = emptySet(),
  val rarity: Set<String> = emptySet(),
  val retreatCost: RangeValue? = null,
  val attackCost: RangeValue? = null,
  val attackDamage: RangeValue? = null,
  val hp: RangeValue? = null,
  val evolvesFrom: String? = null,
  val evolvesTo: String? = null,
  val weaknesses: Set<Type> = emptySet(),
  val resistances: Set<Type> = emptySet(),
) {

  val isEmpty: Boolean
    get() {
      return types.isEmpty() && subTypes.isEmpty() && expansions.isEmpty() &&
        rarity.isEmpty() && retreatCost == null && attackCost.isNullOrEmpty() &&
        attackDamage.isNullOrEmpty() && hp.isNullOrEmpty() && weaknesses.isEmpty() &&
        evolvesFrom.isNullOrBlank() && resistances.isEmpty() && superTypes.isEmpty()
    }

  /**
   * Return whether or not a [Card] matches this given filter
   * @param card the [Card] to check against
   * @return true if the card is matched by this filter, false otherwise
   */
  fun matches(card: Card): Boolean {
    // Check types
    if (types.isNotEmpty()) {
      if (card.types.isNullOrEmpty()) return false
      if (card.types.none { types.contains(it) }) return false
    }

    if (superTypes.isNotEmpty() && !superTypes.contains(card.supertype)) return false

    if (subTypes.isNotEmpty()) {
      if (card.subtypes.isEmpty()) return false
      if (card.subtypes.none { subTypes.contains(it) }) return false
    }

    if (expansions.isNotEmpty() && !expansions.contains(card.expansion.id)) return false

    if (rarity.isNotEmpty()) {
      if (card.rarity == null) return false
      if (!rarity.contains(card.rarity)) return false
    }

    if (retreatCost?.matches(card.convertedRetreatCost ?: 0) == false) return false

    if (attackCost != null) {
      if (card.attacks.isNullOrEmpty()) return false
      if (card.attacks.none { attackCost.matches(it.convertedEnergyCost) }) return false
    }

    if (attackDamage != null) {
      if (card.attacks.isNullOrEmpty()) return false
      if (card.attacks.none { attackDamage.matches(it.damageAmount ?: 0) }) return false
    }

    if (hp?.matches(card.hp ?: 0) == false) return false

    if (weaknesses.isNotEmpty()) {
      if (card.weaknesses.isNullOrEmpty()) return false
      if (card.weaknesses.none { weaknesses.contains(it.type) }) return false
    }

    if (resistances.isNotEmpty()) {
      if (card.resistances.isNullOrEmpty()) return false
      if (card.resistances.none { resistances.contains(it.type) }) return false
    }

    return true
  }
}

data class RangeValue(
  val value: Int,
  val modifier: Modifier = Modifier.NONE,
) {
  val isEmpty: Boolean get() = value == 0 && modifier == Modifier.NONE

  fun matches(amount: Int): Boolean {
    return when (modifier) {
      Modifier.LT -> amount < value
      Modifier.LTE -> amount <= value
      Modifier.GT -> amount > value
      Modifier.GTE -> amount >= value
      Modifier.NONE -> amount == value
    }
  }

  enum class Modifier { LT, LTE, GT, GTE, NONE }

  companion object {
    fun fromSaverValue(value: Any?): RangeValue? {
      val strVal = value as? String ?: return null
      if (strVal == "null") return null
      val parts = strVal.split("|")
      if (parts.size != 2) return null
      val rangeValue = parts[0].toIntOrNull() ?: return null
      val rangeModifier = Modifier.values().find { it.name == parts[1] } ?: return null
      return RangeValue(rangeValue, rangeModifier)
    }
  }
}

fun RangeValue?.isNullOrEmpty(): Boolean = this == null || this.isEmpty

fun RangeValue?.saverValue(): String =
  this?.let { "$value|${modifier.name}" } ?: "null"

private val attackDamageRegex = "\\d+".toRegex()

internal val Card.Attack.damageAmount: Int?
  get() = damage?.let { dmg ->
    attackDamageRegex.find(dmg)?.value?.toIntOrNull()
  }
