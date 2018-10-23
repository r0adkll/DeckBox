package com.r0adkll.deckbuilder.arch.data.databasev2.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.r0adkll.deckbuilder.arch.data.databasev2.entities.CardEntity
import com.r0adkll.deckbuilder.arch.data.databasev2.entities.AttackEntity


class CardWithAttacks(
        @Embedded val card: CardEntity,

        @Relation(entity = AttackEntity::class,
                parentColumn = "id",
                entityColumn = "cardId")
        val attacks: List<AttackEntity>
)