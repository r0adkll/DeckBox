package com.r0adkll.deckbuilder.arch.data.features.missingcard.mapper

import com.r0adkll.deckbuilder.arch.data.features.missingcard.model.MissingCardEntity
import com.r0adkll.deckbuilder.arch.domain.features.missingcard.model.MissingCard


object EntityMapper {

    fun to(model: MissingCard): MissingCardEntity {
        return MissingCardEntity(
                model.name,
                model.number,
                model.description,
                model.setName,
                model.print
        )
    }
}