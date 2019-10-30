package com.r0adkll.deckbuilder.arch.domain.features.remote.model

/**
 * The remote configuration for expansion previews
 * @param version the version code of the remote expansion source. Used to update when code doesn't need to change
 * @param expansionCode the expansion code (i.e. sm12) of the expansion to download and check against
 */
data class PreviewExpansionVersion(
        val version: Int,
        val expansionCode: String
)
