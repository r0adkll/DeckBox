package com.r0adkll.deckbuilder.arch.domain.features.remote.model

data class LegalOverrides(
    val promos: Promo?,
    val expandedPromos: Promo?,
    val singles: List<Single>
) {

    data class Promo(
        val setCode: String,
        val startNumber: Int
    )

    data class Single(
        val id: String,
        val sourceId: String,
        val sourceSetCode: String
    )
}
