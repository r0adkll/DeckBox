package app.deckbox.decks.impl

import app.deckbox.common.settings.DeckBoxSettings
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.settings.DeckCardConfig
import app.deckbox.features.decks.api.DeckCardConfigurator
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
@ContributesBinding(MergeAppScope::class)
class SettingsDeckCardConfigurator(
  private val settings: DeckBoxSettings,
) : DeckCardConfigurator {

  override fun observeConfig(): Flow<DeckCardConfig> {
    return settings.observeDeckCardConfig()
  }

  override fun update(config: DeckCardConfig) {
    settings.deckCardConfig = config
  }
}
