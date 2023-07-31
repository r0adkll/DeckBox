package app.deckbox.common.settings

import app.deckbox.core.settings.EnumSetting
import app.deckbox.core.settings.EnumSettingProvider
import app.deckbox.expansions.ui.ExpansionCardStyle
import kotlinx.coroutines.flow.Flow

interface DeckBoxSettings {

  var theme: Theme
  fun observeTheme(): Flow<Theme>

  var useDynamicColors: Boolean
  fun observeUseDynamicColors(): Flow<Boolean>

  var expansionCardStyle: ExpansionCardStyle
  fun observeExpansionCardStyle(): Flow<ExpansionCardStyle>

  enum class Theme(override val storageKey: String) : EnumSetting {
    LIGHT("light"),
    DARK("dark"),
    SYSTEM("system"),
    ;

    companion object : EnumSettingProvider<Theme> {
      override fun fromStorageKey(key: String?): Theme {
        return values().find { it.storageKey == key } ?: SYSTEM
      }
    }
  }
}
