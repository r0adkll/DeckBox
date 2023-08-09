package app.deckbox.common.settings

import app.deckbox.core.settings.EnumSetting
import app.deckbox.core.settings.EnumSettingProvider
import app.deckbox.core.settings.ExpansionCardStyle
import app.deckbox.core.settings.DeckCardConfig
import kotlinx.coroutines.flow.Flow

interface DeckBoxSettings {

  var theme: Theme
  fun observeTheme(): Flow<Theme>

  var useDynamicColors: Boolean
  fun observeUseDynamicColors(): Flow<Boolean>

  var expansionCardStyle: ExpansionCardStyle
  fun observeExpansionCardStyle(): Flow<ExpansionCardStyle>

  var deckCardConfig: DeckCardConfig
  fun observeDeckCardConfig(): Flow<DeckCardConfig>

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
