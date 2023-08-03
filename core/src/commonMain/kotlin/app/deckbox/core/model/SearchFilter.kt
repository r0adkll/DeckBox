package app.deckbox.core.model

data class SearchFilter(
  val field: SearchField = SearchField.NAME,
  val types: Set<Type> = emptySet(),
  val superTypes: Set<SuperType> = emptySet(),
  val subTypes: Set<String> = emptySet(),
  val expansions: Set<Expansion> = emptySet(),
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
}

data class RangeValue(
  val value: Int,
  val modifier: Modifier = Modifier.NONE,
) {
  val isEmpty: Boolean get() = value == 0 && modifier == Modifier.NONE

  enum class Modifier { LT, LTE, GT, GTE, NONE }
}

fun RangeValue?.isNullOrEmpty(): Boolean = this == null || this.isEmpty
