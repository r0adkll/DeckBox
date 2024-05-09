package app.deckbox.decks.impl.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.deckbox.DeckBoxDatabase
import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.logging.bark
import app.deckbox.core.model.Card
import app.deckbox.core.model.Deck
import app.deckbox.core.model.Stacked
import app.deckbox.core.time.FatherTime
import app.deckbox.db.mapping.asDecks
import app.deckbox.db.mapping.toModel
import app.deckbox.decks.impl.ids.DeckIdGenerator
import app.deckbox.sqldelight.Deck_card_join
import app.deckbox.sqldelight.Decks
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@ContributesBinding(MergeAppScope::class)
@Inject
class SqlDelightDeckDao(
  private val database: DeckBoxDatabase,
  private val deckIdGenerator: DeckIdGenerator,
  private val fatherTime: FatherTime,
  private val dispatcherProvider: DispatcherProvider,
) : DeckDao {

  override fun observe(id: String): Flow<Deck> {
    return database.deckQueries
      .getById(id)
      .asFlow()
      .mapToOneOrNull(dispatcherProvider.databaseRead)
      .filterNotNull()
      .map { it.toModel(fatherTime.now()) }
  }

  override fun observeAll(): Flow<List<Deck>> {
    return database.deckQueries
      .getAll()
      .asFlow()
      .mapToList(dispatcherProvider.databaseRead)
      .map {
        it.map {
          it.toModel(fatherTime.now())
        }
      }
  }

  override suspend fun editName(deckId: String, name: String) {
    withContext(dispatcherProvider.databaseWrite) {
      database.deckQueries.upsertName(
        id = deckId,
        name = name,
        updatedAt = fatherTime.now(),
        createdAt = fatherTime.now(),
      )
    }
  }

  override suspend fun editDescription(deckId: String, description: String) {
    withContext(dispatcherProvider.databaseWrite) {
      database.deckQueries.upsertDescription(
        id = deckId,
        description = description,
        updatedAt = fatherTime.now(),
        createdAt = fatherTime.now(),
      )
    }
  }

  override suspend fun addTag(deckId: String, tag: String) = withContext(dispatcherProvider.databaseWrite) {
    database.transaction {
      val tags = database.deckQueries.getTags(deckId)
        .executeAsOneOrNull()
        ?.tags ?: emptySet()

      val updatedTags = tags + tag

      database.deckQueries.upsertTags(
        id = deckId,
        tags = updatedTags,
        updatedAt = fatherTime.now(),
        createdAt = fatherTime.now(),
      )
    }
  }

  override suspend fun removeTag(deckId: String, tag: String) = withContext(dispatcherProvider.databaseWrite) {
    database.transaction {
      val tags = database.deckQueries.getTags(deckId)
        .executeAsOneOrNull()
        ?.tags ?: emptySet()

      val updatedTags = tags - tag

      database.deckQueries.upsertTags(
        id = deckId,
        tags = updatedTags,
        updatedAt = fatherTime.now(),
        createdAt = fatherTime.now(),
      )
    }
  }

  override suspend fun incrementCard(deckId: String, cardId: String, amount: Int) {
    withContext(dispatcherProvider.databaseWrite) {
      database.deckCardJoinQueries.incrementCount(
        deckId = deckId,
        cardId = cardId,
        amount = amount,
        updatedAt = fatherTime.now(),
        createdAt = fatherTime.now(),
      )
    }
  }

  override suspend fun decrementCard(deckId: String, cardId: String, amount: Int) {
    withContext(dispatcherProvider.databaseWrite) {
      database.deckCardJoinQueries.decrementCount(
        deckId = deckId,
        cardId = cardId,
        amount = amount,
        updatedAt = fatherTime.now(),
        createdAt = fatherTime.now(),
      )
    }
  }

  override suspend fun removeCard(deckId: String, cardId: String) {
    withContext(dispatcherProvider.databaseWrite) {
      database.deckCardJoinQueries.delete(
        deckId = deckId,
        cardId = cardId,
      )
    }
  }

  override suspend fun addBoosterPack(deckId: String, boosterPackId: String) {
    withContext(dispatcherProvider.databaseWrite) {
      database.transaction {
        // Fetch booster pack cards
        val boosterPackCards = database.boosterPackCardJoinQueries
          .getByBoosterPack(boosterPackId)
          .executeAsList()

        if (boosterPackCards.isNotEmpty()) {
          boosterPackCards.forEach { packCard ->
            database.deckCardJoinQueries.incrementCount(
              deckId = deckId,
              cardId = packCard.cardId,
              amount = packCard.count,
              updatedAt = fatherTime.now(),
              createdAt = fatherTime.now(),
            )
          }
        } else {
          bark { "No booster pack or empty pack found for ID($boosterPackId)" }
        }
      }
    }
  }

  override suspend fun delete(deckId: String) {
    withContext(dispatcherProvider.databaseWrite) {
      database.deckQueries.delete(deckId)
    }
  }

  override suspend fun duplicate(deckId: String): String {
    return withContext(dispatcherProvider.databaseWrite) {
      database.transactionWithResult {
        val deck = database.deckQueries.getById(deckId).executeAsOne()
        val deckCards = database.deckCardJoinQueries.getByDeck(deckId).executeAsList()

        val newDeck = deck.copy(
          id = deckIdGenerator.generate(),
          updatedAt = fatherTime.now(),
          createdAt = fatherTime.now(),
        )
        val newCards = deckCards.map { it.copy(deckId = newDeck.id) }

        database.deckQueries.insert(newDeck.asDecks())
        newCards.forEach { newCard ->
          database.deckCardJoinQueries.insert(newCard)
        }

        newDeck.id
      }
    }
  }

  override suspend fun import(deckId: String, name: String, cards: List<Stacked<Card>>) {
    withContext(dispatcherProvider.databaseWrite) {
      database.transaction {
        // Insert the new deck into the DB
        database.deckQueries.insert(
          Decks(
            id = deckId,
            name = name,
            description = null,
            tags = null,
            createdAt = fatherTime.now(),
            updatedAt = fatherTime.now(),
          ),
        )

        // Insert all the card relations
        cards.forEach { stackedCard ->
          database.deckCardJoinQueries.insert(
            Deck_card_join(
              deckId = deckId,
              cardId = stackedCard.card.id,
              count = stackedCard.count,
            ),
          )
        }
      }
    }
  }
}
