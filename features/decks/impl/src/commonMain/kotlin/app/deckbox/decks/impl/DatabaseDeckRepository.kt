package app.deckbox.decks.impl

import app.deckbox.core.di.AppScope
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.Deck
import app.deckbox.core.model.Legalities
import app.deckbox.core.model.Legality
import app.deckbox.decks.impl.db.DeckDao
import app.deckbox.features.decks.api.DeckRepository
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlin.random.Random
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.tatarka.inject.annotations.Inject

@AppScope
@ContributesBinding(MergeAppScope::class)
@Inject
class DatabaseDeckRepository(
  private val db: DeckDao,
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
}
