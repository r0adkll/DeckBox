package app.deckbox.core.model

data class Legalities(
  val unlimited: Legality?,
  val standard: Legality?,
  val expanded: Legality?,
)

enum class Legality {
  LEGAL,
  ILLEGAL,
  BANNED;

  companion object {

    fun from(value: String?): Legality? {
      return values().find {
        it.name.equals(value, ignoreCase = true)
      }
    }
  }
}
