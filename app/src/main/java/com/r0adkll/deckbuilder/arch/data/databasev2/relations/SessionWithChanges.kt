package com.r0adkll.deckbuilder.arch.data.databasev2.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.r0adkll.deckbuilder.arch.data.databasev2.entities.SessionChangeEntity
import com.r0adkll.deckbuilder.arch.data.databasev2.entities.SessionEntity


data class SessionWithChanges(
        @Embedded
        val session: SessionEntity,

        @Relation(entity = SessionChangeEntity::class,
                parentColumn = "uid",
                entityColumn = "sessionId")
        val changes: List<SessionChangeEntity>
)