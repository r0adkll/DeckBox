package app.deckbox.features.cards.impl.db

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransactionCallbacks
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrDefault
import app.deckbox.DeckBoxDatabase
import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.logging.LogPriority.ERROR
import app.deckbox.core.logging.bark
import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import app.deckbox.core.model.map
import app.deckbox.db.mapping.toEntity
import app.deckbox.db.mapping.toModel
import app.deckbox.db.mapping.toStackedEntity
import app.deckbox.features.cards.public.model.CardQuery
import app.deckbox.sqldelight.Cards
import app.deckbox.sqldelight.Favorites
import app.deckbox.sqldelight.GetCardsForBoosterPack
import app.deckbox.sqldelight.GetCardsForDeck
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
@ContributesBinding(MergeAppScope::class)
class SqlDelightCardDao(
  private val database: DeckBoxDatabase,
  private val dispatcherProvider: DispatcherProvider,
) : CardDao {

  override suspend fun fetch(id: String): Card? = withContext(dispatcherProvider.databaseRead) {
    database.transactionWithResult {
      database.cardQueries
        .getById(id)
        .executeAsOneOrNull()
        ?.let(::hydrate)
    }
  }

  override suspend fun fetch(ids: List<String>): List<Card> = withContext(dispatcherProvider.databaseRead) {
    database.transactionWithResult {
      database.cardQueries
        .getByIds(ids)
        .executeAsList()
        .let(::hydrate)
    }
  }

  override suspend fun fetch(query: CardQuery): List<Card> {
    // TODO: Support querying the database
    return emptyList()
  }

  override suspend fun fetchByExpansion(
    expansionId: String,
  ): List<Card> = withContext(dispatcherProvider.databaseRead) {
    database.transactionWithResult {
      database.cardQueries
        .getByExpansionId(expansionId)
        .executeAsList()
        .let(::hydrate)
    }
  }

  override suspend fun fetchByRemoteKey(
    query: String,
    key: Int,
    onQuery: (Query<Cards>) -> Unit,
  ): List<Card> = withContext(dispatcherProvider.databaseRead) {
    database.transactionWithResult {
      database.remoteKeyQueries
        .getCardsForQueryAndKey(query, key)
        .also(onQuery)
        .executeAsList()
        .let(::hydrate)
    }
  }

  override suspend fun fetchByRemoteKey(
    remoteKeyId: Long,
    onQuery: (Query<Cards>) -> Unit,
  ): List<Card> = withContext(dispatcherProvider.databaseRead) {
    database.transactionWithResult {
      database.remoteKeyQueries
        .getCardsForRemoteKey(remoteKeyId)
        .also(onQuery)
        .executeAsList()
        .let(::hydrate)
    }
  }

  override fun observe(id: String): Flow<Card> {
    return database.cardQueries
      .getById(id)
      .asFlow()
      .mapNotNull {
        withContext(dispatcherProvider.databaseRead) {
          database.transactionWithResult {
            it.executeAsOneOrNull()?.let(::hydrate)
          }
        }
      }
  }

  override fun observe(ids: List<String>): Flow<List<Card>> {
    return database.cardQueries
      .getByIds(ids)
      .asFlow()
      .mapNotNull {
        withContext(dispatcherProvider.databaseRead) {
          database.transactionWithResult {
            it.executeAsList().let(::hydrate)
          }
        }
      }
  }

  override fun observe(query: CardQuery): Flow<List<Card>> {
    // TODO: Support query filter here
    return emptyFlow()
  }

  override fun observeByExpansion(expansionId: String): Flow<List<Card>> {
    return database.cardQueries
      .getByExpansionId(expansionId)
      .asFlow()
      .mapNotNull {
        withContext(dispatcherProvider.databaseRead) {
          database.transactionWithResult {
            it.executeAsList().let(::hydrate)
          }
        }
      }
  }

  override fun observeByDeck(deckId: String): Flow<List<Stacked<Card>>> {
    return database.deckCardJoinQueries
      .getCardsForDeck(deckId)
      .asFlow()
      .mapNotNull {
        withContext(dispatcherProvider.databaseRead) {
          database.transactionWithResult {
            val stackedEntities = it.executeAsList().map(GetCardsForDeck::toStackedEntity)
            hydrateStacks(stackedEntities)
          }
        }
      }
  }

  override fun observeByBoosterPack(packId: String): Flow<List<Stacked<Card>>> {
    return database.boosterPackCardJoinQueries
      .getCardsForBoosterPack(packId)
      .asFlow()
      .mapNotNull {
        withContext(dispatcherProvider.databaseRead) {
          database.transactionWithResult {
            val stackedEntities = it.executeAsList().map(GetCardsForBoosterPack::toStackedEntity)
            hydrateStacks(stackedEntities)
          }
        }
      }
  }

  override fun observeByFavorites(): Flow<List<Card>> {
    return database.cardFavoriteQueries
      .getCards()
      .asFlow()
      .mapNotNull {
        withContext(dispatcherProvider.databaseRead) {
          database.transactionWithResult {
            it.executeAsList().let(::hydrate)
          }
        }
      }
  }

  override suspend fun insert(card: Card) = withContext(dispatcherProvider.databaseWrite) {
    database.transaction {
      insertCard(card)
    }
  }

  override suspend fun insert(cards: List<Card>) = withContext(dispatcherProvider.databaseWrite) {
    database.transaction {
      cards.forEach { card ->
        insertCard(card)
      }
    }
  }

  override fun insert(callbacks: TransactionCallbacks, cards: List<Card>) {
    cards.forEach { card ->
      callbacks.insertCard(card)
    }
  }

  override suspend fun favorite(id: String, value: Boolean) = withContext(dispatcherProvider.databaseWrite) {
    database.cardFavoriteQueries.favorite(
      favorited = value,
      cardId = id,
    )
  }

  override fun observeFavorites(): Flow<Map<String, Boolean>> {
    return database.cardFavoriteQueries
      .getAll()
      .asFlow()
      .mapToList(dispatcherProvider.databaseRead)
      .map { favorites ->
        favorites.associateBy({ it.cardId }, { it.favorited })
      }
  }

  override fun observeFavorite(id: String): Flow<Boolean> {
    return database.cardFavoriteQueries
      .getById(id)
      .asFlow()
      .mapToOneOrDefault(Favorites(false, id), dispatcherProvider.databaseRead)
      .map { it.favorited }
  }

  /**
   * Insert a card into the database, including the additional tables for:
   * * Expansions
   * * Abilities
   * * Attacks
   * in a database transaction
   */
  private fun TransactionCallbacks.insertCard(card: Card) {
    // Insert the expansion first
    database.expansionQueries.insert(card.expansion.toEntity())

    // Insert base card
    database.cardQueries.insert(card.toEntity())

    // Insert relations
    card.abilities
      ?.map { it.toEntity(card.id) }
      ?.forEach { ability ->
        database.abilityQueries.insert(ability)
      }

    card.attacks
      ?.map { it.toEntity(card.id) }
      ?.forEach { attack ->
        database.attackQueries.insert(attack)
      }

    card.tcgPlayer?.toEntity(card.id)?.let { tcgPlayer ->
      database.tcgPlayerPricesQueries.insert(tcgPlayer)
    }

    card.cardMarket?.toEntity(card.id)?.let { cardMarket ->
      database.cardMarketPricesQueries.insert(cardMarket)
    }

    afterRollback {
      bark(ERROR) { "Insert Card Failed Card(${card.id})" }
    }
  }

  override suspend fun delete(id: String) = withContext(dispatcherProvider.databaseWrite) {
    database.cardQueries.delete(id)
  }

  override suspend fun delete(ids: List<String>) = withContext(dispatcherProvider.databaseWrite) {
    database.cardQueries.deleteMany(ids)
  }

  override suspend fun deleteAll() = withContext(dispatcherProvider.databaseWrite) {
    database.cardQueries.deleteAll()
  }

  /**
   * Hydrate a [Cards] entity with the other relational information
   * in the database.
   *
   * This should only be performed in a transaction
   */
  private fun hydrate(entity: Cards): Card {
    val expansion = database.expansionQueries
      .getById(entity.expansionId)
      .executeAsOne()

    val abilities = database.abilityQueries
      .getById(entity.id)
      .executeAsList()

    val attacks = database.attackQueries
      .getById(entity.id)
      .executeAsList()

    val tcgPlayerPrices = database.tcgPlayerPricesQueries
      .getById(entity.id)
      .executeAsOneOrNull()

    val cardMarketPrices = database.cardMarketPricesQueries
      .getById(entity.id)
      .executeAsOneOrNull()

    return entity.toModel(
      expansion = expansion,
      abilities = abilities,
      attacks = attacks,
      tcgPlayerPrices = tcgPlayerPrices,
      cardMarketPrices = cardMarketPrices,
    )
  }

  private fun hydrate(entities: List<Cards>): List<Card> {
    val ids = entities.map { it.id }
    val expansionIds = entities.map { it.expansionId }.toSet()

    val expansions = database.expansionQueries
      .getByIds(expansionIds)
      .executeAsList()

    val abilities = database.abilityQueries
      .getByIds(ids)
      .executeAsList()
      .groupBy { it.cardId }

    val attacks = database.attackQueries
      .getByIds(ids)
      .executeAsList()
      .groupBy { it.cardId }

    val tcgPlayerPrices = database.tcgPlayerPricesQueries
      .getByIds(ids)
      .executeAsList()
      .associateBy { it.cardId }

    val cardMarketPlayerPrices = database.cardMarketPricesQueries
      .getByIds(ids)
      .executeAsList()
      .associateBy { it.cardId }

    return entities.map { entity ->
      entity.toModel(
        expansion = expansions.find { it.id == entity.expansionId }!!,
        abilities = abilities[entity.id] ?: emptyList(),
        attacks = attacks[entity.id] ?: emptyList(),
        tcgPlayerPrices = tcgPlayerPrices[entity.id],
        cardMarketPrices = cardMarketPlayerPrices[entity.id],
      )
    }
  }

  private fun hydrateStacks(entities: List<Stacked<Cards>>): List<Stacked<Card>> {
    val ids = entities.map { it.card.id }
    val expansionIds = entities.map { it.card.expansionId }.toSet()

    val expansions = database.expansionQueries
      .getByIds(expansionIds)
      .executeAsList()

    val abilities = database.abilityQueries
      .getByIds(ids)
      .executeAsList()
      .groupBy { it.cardId }

    val attacks = database.attackQueries
      .getByIds(ids)
      .executeAsList()
      .groupBy { it.cardId }

    val tcgPlayerPrices = database.tcgPlayerPricesQueries
      .getByIds(ids)
      .executeAsList()
      .associateBy { it.cardId }

    val cardMarketPlayerPrices = database.cardMarketPricesQueries
      .getByIds(ids)
      .executeAsList()
      .associateBy { it.cardId }

    return entities.map { stackedEntity ->
      stackedEntity.map { entity ->
        entity.toModel(
          expansion = expansions.find { it.id == entity.expansionId }!!,
          abilities = abilities[entity.id] ?: emptyList(),
          attacks = attacks[entity.id] ?: emptyList(),
          tcgPlayerPrices = tcgPlayerPrices[entity.id],
          cardMarketPrices = cardMarketPlayerPrices[entity.id],
        )
      }
    }
  }
}
