package com.r0adkll.deckbuilder.arch.data.features.collection.mapper

import com.r0adkll.deckbuilder.arch.data.database.entities.CollectionCountEntity
import com.r0adkll.deckbuilder.arch.domain.features.collection.model.CollectionCount


object EntityMapper {

    fun to(entity: CollectionCountEntity): CollectionCount {
        return CollectionCount(
                entity.cardId,
                entity.count,
                entity.set,
                entity.series
        )
    }

    fun to(entity: com.r0adkll.deckbuilder.arch.data.features.collection.model.CollectionCountEntity): CollectionCount {
        return CollectionCount(
                entity.cardId,
                entity.count,
                entity.set,
                entity.series
        )
    }
}