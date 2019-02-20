package com.r0adkll.deckbuilder.arch.data.database.dao


import android.net.Uri
import androidx.room.*
import com.r0adkll.deckbuilder.arch.data.database.entities.*
import com.r0adkll.deckbuilder.arch.data.database.relations.CardWithAttacks
import com.r0adkll.deckbuilder.arch.data.database.relations.StackedCard
import com.r0adkll.deckbuilder.arch.data.database.relations.SessionWithChanges
import io.reactivex.Flowable


@Dao
abstract class SessionDao {

    @Transaction @Query("SELECT * FROM sessions WHERE uid = :sessionId")
    abstract fun getSessionWithChanges(sessionId: Long): Flowable<SessionWithChanges>

    @Transaction @Query("SELECT * FROM session_card_join INNER JOIN cards ON session_card_join.cardId = cards.id WHERE session_card_join.sessionId = :sessionId")
    abstract fun getSessionCards(sessionId: Long): Flowable<List<StackedCard>>

    @Query("SELECT * FROM session_card_join WHERE sessionId = :sessionId")
    abstract fun getSessionCardJoins(sessionId: Long): List<SessionCardJoin>

    @Query("SELECT * FROM session_card_join WHERE sessionId = :sessionId AND cardId IN(:cardIds)")
    abstract fun getSessionCardJoins(sessionId: Long, cardIds: List<String>): List<SessionCardJoin>

    @Query("SELECT * FROM session_card_join WHERE sessionId = :sessionId AND cardId == :cardId")
    abstract fun getSessionCardJoin(sessionId: Long, cardId: String): SessionCardJoin?

    @Query("SELECT * FROM sessions WHERE uid = :sessionId")
    abstract fun getSession(sessionId: Long): SessionEntity?

    @Query("Select * FROM session_changes WHERE sessionId = :sessionId AND searchSessionId = :searchSessionId")
    abstract fun getChangesForSearchSession(sessionId: Long, searchSessionId: String): List<SessionChangeEntity>

    @Query("UPDATE sessions SET name = :name WHERE uid = :sessionId")
    abstract fun updateName(sessionId: Long, name: String): Int

    @Query("UPDATE sessions SET description = :description WHERE uid = :sessionId")
    abstract fun updateDescription(sessionId: Long, description: String): Int

    @Query("UPDATE sessions SET image = :image WHERE uid = :sessionId")
    abstract fun updateImage(sessionId: Long, image: Uri): Int

    @Query("UPDATE sessions SET collectionOnly = :collectionOnly WHERE uid = :sessionId")
    abstract fun updateCollectionOnly(sessionId: Long, collectionOnly: Boolean): Int

    @Query("DELETE FROM sessions WHERE uid = :sessionId")
    abstract fun deleteSession(sessionId: Long): Int

    @Query("DELETE FROM session_changes WHERE sessionId = :sessionId")
    abstract fun deleteChanges(sessionId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSession(session: SessionEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertCards(cards: List<CardEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertCard(card: CardEntity): Long

    @Insert
    abstract fun insertAttacks(attacks: List<AttackEntity>)

    @Insert
    abstract fun insertJoins(joins: List<SessionCardJoin>)

    @Insert
    abstract fun insertChanges(changes: List<SessionChangeEntity>)

    @Insert
    abstract fun insertChange(change: SessionChangeEntity)

    @Update
    abstract fun updateJoins(joins: List<SessionCardJoin>)

    @Update
    abstract fun updateJoin(join: SessionCardJoin)

    @Update
    abstract fun updateSession(session: SessionEntity)

    @Delete
    abstract fun deleteJoins(joins: List<SessionCardJoin>)

    @Delete
    abstract fun deleteJoin(join: SessionCardJoin)

    @Delete
    abstract fun deleteChanges(changes: List<SessionChangeEntity>)


    private fun insertCardsWithAttacks(cards: List<CardWithAttacks>) {
        cards.forEach {
            insertCardWithAttacks(it)
        }
    }

    private fun insertCardWithAttacks(card: CardWithAttacks) {
        if (insertCard(card.card) > 0L) {
            insertAttacks(card.attacks)
        }
    }

    /*
     * Insert new session
     */

    @Transaction
    open fun insertNewSession(session: SessionEntity, cards: List<CardWithAttacks>, joins: List<SessionCardJoin>): Long {
        insertCardsWithAttacks(cards)

        val sessionId = insertSession(session)
        joins.forEach { it.sessionId = sessionId }
        insertJoins(joins)
        return sessionId
    }

    /*
     * Add/Remove session cards
     */

    @Transaction
    open fun insertAddChanges(sessionId: Long, cards: List<CardWithAttacks>, changes: List<SessionChangeEntity>) {
        insertCardsWithAttacks(cards)
        insertChanges(changes)

        val joins = getSessionCardJoins(sessionId).toMutableList()

        val condensedChanges = changes.groupBy { it.cardId }.map {
            it.key to it.value.sumBy { it.change }
        }

        // Find missing joins
        val joinsToInsert = condensedChanges.filter { change ->
            joins.none { it.cardId == change.first }
        }.map {
            SessionCardJoin(sessionId, it.first, it.second)
        }

        // Find joins to Update
        val joinsToUpdate = joins.filter { join ->
            condensedChanges.any { it.first == join.cardId }
        }.map { join ->
            val change = condensedChanges.find { it.first == join.cardId }
            change?.let {
                join.count += it.second
                join
            } ?: join
        }

        insertJoins(joinsToInsert)
        updateJoins(joinsToUpdate)
    }

    @Transaction
    open fun insertRemoveChange(sessionId: Long, card: CardWithAttacks?, change: SessionChangeEntity){
        card?.let { insertCardWithAttacks(it) }
        insertChange(change)

        // Find existing join
        val join = getSessionCardJoin(sessionId, change.cardId)
        join?.let {
            it.count += change.change
            if (it.count <= 0) {
                deleteJoin(it)
            } else {
                updateJoin(it)
            }
        }
    }

    /*
     * Transaction: Reset Session
     */

    @Transaction
    open fun resetSession(sessionId: Long) {
        val session = getSession(sessionId)
        if (session != null) {
            session.originalName = session.name
            session.originalDescription = session.description
            session.originalImage = session.image
            session.originalCollectionOnly = session.collectionOnly
            updateSession(session)
        }
        deleteChanges(sessionId)
    }

    @Transaction
    open fun clearSearchSession(sessionId: Long, searchSessionId: String) {
        val changes = getChangesForSearchSession(sessionId, searchSessionId)
        val condensedChanges = changes.groupBy { it.cardId }
                .mapValues { it.value.sumBy { it.change } }
                .filter { it.value > 0 }

        val joins = getSessionCardJoins(sessionId, condensedChanges.map { it.key })
        val joinsToUpdate = ArrayList<SessionCardJoin>()
        val joinsToDelete = ArrayList<SessionCardJoin>()

        joins.forEach { join ->
            val change = condensedChanges[join.cardId] ?: 0
            join.count -= change
            if (join.count <= 0) {
                joinsToDelete += join
            } else {
                joinsToUpdate += join
            }
        }

        updateJoins(joinsToUpdate)
        deleteJoins(joinsToDelete)
        deleteChanges(changes)
    }
}