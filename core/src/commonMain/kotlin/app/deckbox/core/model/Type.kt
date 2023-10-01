package app.deckbox.core.model

enum class Type {
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
  UNKNOWN,
  ;

  val displayName: String
    get() = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

  companion object {
    private val VALUES by lazy { values() }

    fun find(text: String): Type {
      return VALUES.find { it.name.equals(text, true) } ?: UNKNOWN
    }
  }
}

fun energyTypeFromCardName(name: String): Type {
  val energyName = BasicEnergyRegex.find(name)
    ?.groups
    ?.get(2)
    ?.value

  return if (energyName != null) {
    Type.find(energyName)
  } else {
    Type.COLORLESS
  }
}

private val BasicEnergyRegex = """(Basic )?(.+) Energy""".toRegex()
