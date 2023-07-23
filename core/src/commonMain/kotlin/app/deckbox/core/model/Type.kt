package app.deckbox.core.model

enum class Type(internal var text: String? = null) {
  COLORLESS,
  DARKNESS,
  DRAGON,
  FAIRY,
  FIGHTING,
  FIRE,
  GRASS,
  LIGHTNING,
  METAL,
  PSYCHIC,
  WATER,
  UNKNOWN;

  val displayName: String
    get() = text ?: name.capitalize()

  companion object {
    private val VALUES by lazy { values() }

    fun find(text: String): Type {
      val type = VALUES.find { it.name.equals(text, true) } ?: UNKNOWN
      if (type == UNKNOWN) {
        type.text = text
      }
      return type
    }
  }
}
