package app.deckbox.core.model

data class Legalities(
  val unlimited: Legality? = null,
  val standard: Legality? = null,
  val expanded: Legality? = null,
) : Comparable<Legalities> {

  /**
   * Compares this object with the specified object for order. Returns zero if this object is equal
   * to the specified [other] object, a negative number if it's less than [other], or a positive number
   * if it's greater than [other].
   */
  override fun compareTo(other: Legalities): Int {
    return when {
      standard.isLegal() && other.standard.isLegal() -> 0
      standard.isLegal() && !other.standard.isLegal() -> 1
      !standard.isLegal() && other.standard.isLegal() -> -1

      expanded.isLegal() && other.expanded.isLegal() -> 0
      expanded.isLegal() && !other.expanded.isLegal() -> 1
      !expanded.isLegal() && other.expanded.isLegal() -> -1

      unlimited.isLegal() && other.unlimited.isLegal() -> 0
      unlimited.isLegal() && !other.unlimited.isLegal() -> 1
      !unlimited.isLegal() && other.unlimited.isLegal() -> -1

      else -> 0
    }
  }

  private fun Legality?.isLegal(): Boolean {
    return this == Legality.LEGAL
  }
}

enum class Legality {
  LEGAL,
  ILLEGAL,
  BANNED,
  ;

  companion object {

    fun from(value: String?): Legality? {
      return values().find {
        it.name.equals(value, ignoreCase = true)
      }
    }
  }
}
