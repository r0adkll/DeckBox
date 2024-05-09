package app.deckbox.decks.impl

import app.deckbox.core.di.AppScope
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.Card
import app.deckbox.core.model.Deck
import app.deckbox.core.model.Stacked
import app.deckbox.decks.impl.db.DeckDao
import app.deckbox.decks.impl.ids.DeckIdGenerator
import app.deckbox.features.decks.api.DeckRepository
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@AppScope
@ContributesBinding(MergeAppScope::class)
@Inject
class DatabaseDeckRepository(
  private val db: DeckDao,
  private val deckIdGenerator: DeckIdGenerator,
) : DeckRepository {

  override fun observeDecks(): Flow<List<Deck>> {
    return db.observeAll()
  }

  override suspend fun deleteDeck(deckId: String) {
    db.delete(deckId)
  }

  override suspend fun duplicateDeck(deckId: String): String {
    return db.duplicate(deckId)
  }

  override suspend fun importDeck(name: String, cards: List<Stacked<Card>>): String {
    val newDeckId = deckIdGenerator.generate()
    db.import(newDeckId, name, cards)
    return newDeckId
  }
}
