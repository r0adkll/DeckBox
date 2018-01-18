package com.r0adkll.deckbuilder.arch.domain.features.missingcard.model


data class MissingCard(
        val name: String,
        val number: Int?,
        val description: String?,
        val setCode: String?,
        val print: String
)