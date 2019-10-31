package com.r0adkll.deckbuilder.arch.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.r0adkll.deckbuilder.arch.data.database.entities.AttackEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.CardEntity

class CardWithAttacks(
        @Embedded val card: CardEntity,

        @Relation(entity = AttackEntity::class,
                parentColumn = "id",
                entityColumn = "cardId")
        val attacks: List<AttackEntity>
)
