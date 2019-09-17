package com.r0adkll.deckbuilder.arch.domain.features.remote.model

/**
 * The remote configuration for expansion previews
 * @param version the version code of the remote expansion source. Used to update when code doesn't need to change
 * @param expansionCode the expansion code (i.e. sm12) of the expansion to download and check against
 * @param expansionCodeMatcher the expansion code matching regex used to match card id's to this expansion
 * @param conversionProxies a list of preview conversion proxies that instruct how preview cards convert to actual cards
 */
data class PreviewExpansionVersion(
        val version: Int,
        val expansionCode: String,
        val expansionCodeMatcher: String,
        val conversionProxies: List<PreviewProxy>? = null
) {

    /**
     * Model used to instruct how to map preview to actual cards
     * @param previewId the id of the preview api card id
     * @param actualId the id of the card in the main api
     */
    data class PreviewProxy(
            val previewId: String,
            val actualId: String
    )
}
