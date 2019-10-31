package com.r0adkll.deckbuilder.arch.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.r0adkll.deckbuilder.arch.data.database.entities.SessionChangeEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.SessionEntity

data class SessionWithChanges(
        @Embedded
        val session: SessionEntity,

        @Relation(entity = SessionChangeEntity::class,
                parentColumn = "uid",
                entityColumn = "sessionId")
        val changes: List<SessionChangeEntity>
)
