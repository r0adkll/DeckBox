package app.deckbox.expansions.ui

import app.deckbox.core.settings.EnumSetting
import app.deckbox.core.settings.EnumSettingProvider

enum class ExpansionCardStyle(
  override val storageKey: String,
) : EnumSetting {
  Large("large"),
  Small("small"),
  Compact("compact"),
  ;

  companion object : EnumSettingProvider<ExpansionCardStyle> {
    override fun fromStorageKey(key: String?): ExpansionCardStyle {
      return values().find { it.storageKey == key } ?: Large
    }
  }
}
