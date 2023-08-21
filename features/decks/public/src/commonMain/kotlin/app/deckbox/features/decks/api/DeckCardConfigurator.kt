package app.deckbox.features.decks.api

import app.deckbox.core.settings.DeckCardConfig
import kotlinx.coroutines.flow.Flow

interface DeckCardConfigurator {

  fun observeConfig(): Flow<DeckCardConfig>

  fun update(config: DeckCardConfig)
}
