package app.deckbox.common.settings

import app.deckbox.common.settings.DeckBoxSettings.Theme
import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.AppScope
import app.deckbox.core.settings.DeckCardConfig
import app.deckbox.core.settings.DeckCardSlice
import app.deckbox.core.settings.ExpansionCardStyle
import app.deckbox.core.settings.asString
import app.deckbox.core.settings.fromString
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@OptIn(ExperimentalSettingsApi::class)
@AppScope
@Inject
class DeckBoxSettingsImpl(
  override val settings: ObservableSettings,
  private val dispatchers: DispatcherProvider,
) : DeckBoxSettings, AppSettings() {
  private val flowSettings by lazy { settings.toFlowSettings(dispatchers.io) }

  override var theme: Theme by enumSetting(KEY_THEME, Theme)
  override fun observeTheme(): Flow<Theme> {
    return flowSettings.getEnumFlow(KEY_THEME, Theme)
  }

  override var useDynamicColors: Boolean by booleanSetting(KEY_USE_DYNAMIC_COLORS, false)
  override fun observeUseDynamicColors(): Flow<Boolean> {
    return flowSettings.getBooleanFlow(KEY_USE_DYNAMIC_COLORS, false)
  }

  override var expansionCardStyle: ExpansionCardStyle by enumSetting(KEY_EXPANSION_CARD_STYLE, ExpansionCardStyle)
  override fun observeExpansionCardStyle(): Flow<ExpansionCardStyle> {
    return flowSettings.getEnumFlow(KEY_EXPANSION_CARD_STYLE, ExpansionCardStyle)
  }

  override var deckCardConfig: DeckCardConfig by customSetting(
    key = KEY_DECK_CARD_CONFIG,
    defaultValue = DeckCardConfig.DEFAULT,
    getter = { value ->
      DeckCardConfig(DeckCardSlice.fromString(value))
    },
    setter = { config ->
      config.slices.asString()
    },
  )

  override fun observeDeckCardConfig(): Flow<DeckCardConfig> {
    return flowSettings.getStringOrNullFlow(KEY_DECK_CARD_CONFIG)
      .map { value ->
        value?.let { DeckCardConfig(DeckCardSlice.fromString(it)) }
          ?: DeckCardConfig.DEFAULT
      }
  }
}

internal const val KEY_THEME = "pref_theme"
internal const val KEY_USE_DYNAMIC_COLORS = "pref_dynamic_colors"
internal const val KEY_EXPANSION_CARD_STYLE = "pref_expansion_card_style"
internal const val KEY_DECK_CARD_CONFIG = "pref_deck_card_config"
