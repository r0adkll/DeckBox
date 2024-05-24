package app.deckbox.core.settings

enum class PokemonGridStyle(
  override val storageKey: String,
) : EnumSetting {
  Large("large"),
  Small("small"),
  Compact("compact"),
  ;

  companion object : EnumSettingProvider<PokemonGridStyle> {
    val All: List<PokemonGridStyle> by lazy { values().toList() }

    override fun fromStorageKey(key: String?): PokemonGridStyle {
      return entries.find { it.storageKey == key } ?: Small
    }
  }
}

fun PokemonGridStyle.columnsForStyles(isLarge: Boolean = false): Int {
  return if (isLarge) {
    when (this) {
      PokemonGridStyle.Large -> 5
      PokemonGridStyle.Small -> 6
      PokemonGridStyle.Compact -> 8
    }
  } else {
    when (this) {
      PokemonGridStyle.Large -> 3
      PokemonGridStyle.Small -> 4
      PokemonGridStyle.Compact -> 5
    }
  }
}
