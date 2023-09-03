package app.deckbox.core.settings

import app.deckbox.core.model.BoosterPack
import app.deckbox.core.model.Deck

enum class SortOption(
  override val storageKey: String,
) : EnumSetting {
  UpdatedAt("updated-at"),
  CreatedAt("created-at"),
  Alphabetically("alphabetically"),
  Legality("legality"),
  ;

  companion object : EnumSettingProvider<SortOption> {
    val All: List<SortOption> by lazy {
      values().toList()
    }

    override fun fromStorageKey(key: String?): SortOption {
      return values().find { it.storageKey == key } ?: UpdatedAt
    }
  }
}

fun List<Deck>.orderDecksBy(order: SortOption): List<Deck> {
  return when (order) {
    SortOption.UpdatedAt -> sortedByDescending { it.updatedAt }
    SortOption.CreatedAt -> sortedBy { it.createdAt }
    SortOption.Alphabetically -> sortedBy { it.name }
    SortOption.Legality -> sortedBy { it.legalities }
  }
}

fun List<BoosterPack>.orderBoosterPacksBy(order: SortOption): List<BoosterPack> {
  return when (order) {
    SortOption.UpdatedAt -> sortedByDescending { it.updatedAt }
    SortOption.CreatedAt -> sortedBy { it.createdAt }
    SortOption.Alphabetically -> sortedBy { it.name }
    SortOption.Legality -> sortedBy { it.legalities }
  }
}
