package app.deckbox.features.boosterpacks.api

import kotlinx.coroutines.flow.Flow

interface BoosterPackRepository {

  fun observeBoosterPacks(): Flow<List<BoosterPack>>
}
