package com.r0adkll.deckbuilder.arch.data.databasev2.dao


import androidx.room.Dao
import androidx.room.Query
import com.r0adkll.deckbuilder.arch.data.databasev2.entities.SessionCardEntity
import com.r0adkll.deckbuilder.arch.data.databasev2.entities.SessionEntity
import io.reactivex.Flowable


@Dao
interface SessionDao {

    @Query("SELECT * FROM sessions WHERE uid = :sessionId")
    fun getSession(sessionId: Int): Flowable<SessionEntity>


    @Query("SELECT * FROM session_card_join INNER JOIN cards ON session_card_join.cardId = cards.id WHERE session_card_join.sessionId = sessionId")
    fun getSessionCards(sessionId: Int): Flowable<List<SessionCardEntity>>
}