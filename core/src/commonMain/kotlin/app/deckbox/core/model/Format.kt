package app.deckbox.core.model

enum class Format {
  STANDARD,
  EXPANDED,
  UNLIMITED,
  LEGACY,
  THEME;

  companion object {

    fun of(value: String): Format {
      return values().find { it.name.equals(value, true) } ?: UNLIMITED
    }
  }
}
