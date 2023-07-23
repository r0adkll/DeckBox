package app.deckbox.core.model

enum class SuperType(internal var text: String? = null) {
  ENERGY("Energy"),
  POKEMON("Pokémon"),
  TRAINER("Trainer"),
  UNKNOWN;

  val displayName: String
    get() = text ?: name.toLowerCase().capitalize()

  companion object {
    private val VALUES by lazy { values() }

    fun find(text: String?): SuperType {
      val supertype = VALUES.find { it.text.equals(text, true) } ?: UNKNOWN
      if (supertype == UNKNOWN) {
        supertype.text = text
      }
      return supertype
    }
  }
}
