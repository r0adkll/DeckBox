package com.r0adkll.deckbuilder.arch.data.features.collection.mapper

import com.r0adkll.deckbuilder.arch.data.database.entities.CollectionCountEntity
import com.r0adkll.deckbuilder.arch.domain.features.collection.model.CollectionCount
import com.r0adkll.deckbuilder.arch.data.features.collection.model.CollectionCountEntity as FirebaseCollectionCountEntity

object EntityMapper {

    private const val LEGACY_DOCUMENT_ID_LENGTH = 20

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
        val isSourceOld = documentId?.length == LEGACY_DOCUMENT_ID_LENGTH
        return CollectionCount(
            entity.cardId,
            entity.count,
            entity.set,
            entity.series,
            isSourceOld
        )
    }
}
