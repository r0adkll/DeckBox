package app.deckbox.features.boosterpacks.impl

import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.AppScope
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.BoosterPack
import app.deckbox.core.time.FatherTime
import app.deckbox.features.boosterpacks.api.BoosterPackRepository
import app.deckbox.features.boosterpacks.impl.db.BoosterDao
import app.deckbox.features.boosterpacks.impl.ids.BoosterPackIdGenerator
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@AppScope
@ContributesBinding(MergeAppScope::class)
@Inject
class DatabaseBoosterPackRepository(
  private val boosterPackDao: BoosterDao,
  private val boosterPackIdGenerator: BoosterPackIdGenerator,
  private val fatherTime: FatherTime,
  private val dispatcherProvider: DispatcherProvider,
) : BoosterPackRepository {

  private val scope = CoroutineScope(SupervisorJob() + dispatcherProvider.databaseWrite)

  private val draftBoosterPacks = mutableMapOf<String, BoosterPack>()

  override fun createSession(): String {
    val newId = boosterPackIdGenerator.generate()
    draftBoosterPacks[newId] = createDraft(newId)
    return newId
  }

  private fun createDraft(id: String): BoosterPack {
    return BoosterPack(
      id = id,
      name = "",
      cardImages = emptyList(),
      createdAt = fatherTime.now(),
      updatedAt = fatherTime.now(),
    )
  }

  override fun observeBoosterPacks(): Flow<List<BoosterPack>> {
    return boosterPackDao.observeAll()
  }

  override fun observeBoosterPack(id: String): Flow<BoosterPack> {
    return boosterPackDao.observe(id)
      .onStart { draftBoosterPacks[id]?.let { emit(it) } }
  }

  override fun editName(id: String, name: String) {
    scope.launch {
      boosterPackDao.editName(id, name)
    }
  }

  override fun incrementCard(id: String, cardId: String, amount: Int) {
    scope.launch {
      boosterPackDao.incrementCard(id, cardId, amount)
    }
  }

  override fun decrementCard(id: String, cardId: String, amount: Int) {
    scope.launch {
      boosterPackDao.decrementCard(id, cardId, amount)
    }
  }

  override fun delete(id: String) {
    scope.launch {
      boosterPackDao.delete(id)
    }
  }

  override fun duplicate(id: String) {
    scope.launch {
      boosterPackDao.duplicate(id)
    }
  }
}
