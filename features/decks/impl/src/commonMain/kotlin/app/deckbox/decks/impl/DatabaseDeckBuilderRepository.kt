package app.deckbox.decks.impl

import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.AppScope
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.Deck
import app.deckbox.core.time.FatherTime
import app.deckbox.decks.impl.db.DeckDao
import app.deckbox.decks.impl.ids.DeckIdGenerator
import app.deckbox.features.decks.api.builder.DeckBuilderRepository
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@AppScope
@ContributesBinding(MergeAppScope::class)
@Inject
class DatabaseDeckBuilderRepository(
  private val deckDao: DeckDao,
  private val deckIdGenerator: DeckIdGenerator,
  private val fatherTime: FatherTime,
  private val dispatcherProvider: DispatcherProvider,
) : DeckBuilderRepository {

  private val scope = CoroutineScope(SupervisorJob() + dispatcherProvider.databaseWrite)

  private val draftDecks = mutableMapOf<String, Deck>()

  override fun createSession(): String {
    val deckId = deckIdGenerator.generate()
    draftDecks[deckId] = createDraft(deckId)
    return deckId
  }

  private fun createDraft(id: String): Deck {
    return Deck(
      id = id,
      name = "",
      createdAt = fatherTime.now(),
      updatedAt = fatherTime.now(),
    )
  }

  override fun observeSession(deckId: String): Flow<Deck> {
    return deckDao.observe(deckId)
      .onStart { draftDecks[deckId]?.let { emit(it) } }
      .flowOn(dispatcherProvider.databaseRead)
  }

  override fun editName(deckId: String, name: String) {
    scope.launch {
      deckDao.editName(deckId, name)
    }
  }

  override fun editDescription(deckId: String, description: String) {
    scope.launch {
      deckDao.editDescription(deckId, description)
    }
  }

  override fun addTag(deckId: String, tag: String) {
    scope.launch {
      deckDao.addTag(deckId, tag)
    }
  }

  override fun removeTag(deckId: String, tag: String) {
    scope.launch {
      deckDao.removeTag(deckId, tag)
    }
  }

  override fun incrementCard(deckId: String, cardId: String, amount: Int) {
    scope.launch {
      deckDao.incrementCard(deckId, cardId, amount)
    }
  }

  override fun decrementCard(deckId: String, cardId: String, amount: Int) {
    scope.launch {
      deckDao.decrementCard(deckId, cardId, amount)
    }
  }

  override fun removeCard(deckId: String, cardId: String) {
    scope.launch {
      deckDao.removeCard(deckId, cardId)
    }
  }

  override fun addBoosterPack(deckId: String, boosterPackId: String) {
    scope.launch {
      deckDao.addBoosterPack(deckId, boosterPackId)
    }
  }
}
