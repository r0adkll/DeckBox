package app.deckbox.core.model

enum class SuperType(val text: String) {
  ENERGY("Energy"),
  POKEMON("Pokémon"),
  TRAINER("Trainer"),
  UNKNOWN(""),
  ;

  val displayName: String
    get() = text

  companion object {
    private val VALUES by lazy { values() }

    fun find(text: String?): SuperType {
      return VALUES.find { it.text.equals(text, true) } ?: UNKNOWN
    }
  }
}
