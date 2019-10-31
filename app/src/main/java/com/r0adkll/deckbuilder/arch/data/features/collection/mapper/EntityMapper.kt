package com.r0adkll.deckbuilder.arch.data.features.collection.mapper

import com.r0adkll.deckbuilder.arch.data.database.entities.CollectionCountEntity
import com.r0adkll.deckbuilder.arch.domain.features.collection.model.CollectionCount
import com.r0adkll.deckbuilder.arch.data.features.collection.model.CollectionCountEntity as FirebaseCollectionCountEntity

object EntityMapper {

    fun to(entity: CollectionCountEntity): CollectionCount {
        return CollectionCount(
                entity.cardId,
                entity.count,
                entity.set,
                entity.series
        )
    }

    fun to(entity: FirebaseCollectionCountEntity): CollectionCount {
        return to(entity, null)
    }

    fun to(entity: FirebaseCollectionCountEntity, documentId: String? = null): CollectionCount {
        val isSourceOld = documentId?.length == 20 // This indicates the the entity was
        return CollectionCount(
                entity.cardId,
                entity.count,
                entity.set,
                entity.series,
                isSourceOld
        )
    }
}
