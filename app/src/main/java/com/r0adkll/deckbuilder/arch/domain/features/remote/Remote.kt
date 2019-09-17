package com.r0adkll.deckbuilder.arch.domain.features.remote


import com.r0adkll.deckbuilder.arch.domain.features.remote.model.*


/**
 * Wrapper around Firebase Remote Configuration SDK
 */
interface Remote {

    /**
     * This is the versioning string for the latest expansion set offered by the api. It's format as
     * follows: <version_code>.<expansion_code> e.g. 1.sm7
     *
     * - version_code represents the version of the data that may change unrelated to new expansions (i.e. rotation legality changes)
     * - expansion_code represents the latest available expansion in the set (i.e. sm7 - Celestial Storm) which can indicate if a new expansion was added
     */
    val expansionVersion: ExpansionVersion?

    /**
     * This is the versioning string for the latest preview expansion that is exposed by my preview
     * api microservice [DeckBox Preview API](https://github.com/r0adkll/DeckBox-Preview-API).
     */
    val previewExpansionVersion: PreviewExpansionVersion?

    /**
     * This is the spec for an expansion preview card that appears on the deck list screen to tell
     * users about a new expansion that has been added and other information about it. It also attempts
     * to direct them to browse the expansion
     */
    val expansionPreview: ExpansionPreview?

    /**
     * This is a list of search proxy/aliases that better improve the search experience for the user
     */
    val searchProxies: SearchProxies?

    /**
     * This is a list of hashes for cards that are not in standard or expanded formats but have been
     * reprinted in format valid sets since.
     */
    val reprints: Reprints?

    /**
     * This is a list of all the banned cards by format, specified as a list of card ids to check
     */
    val banList: BanList?

    /**
     * This is a set of format legality overrides for special use cases. Such when a promo set is only partially legal, or
     * special subsets are released in sets that don't follow the set's legality, such as the Shiny Vault
     * collection in Hidden Fates
     */
    val legalOverrides: LegalOverrides?

    /**
     * Check for update remote config values and update them if needed. Also set
     * remote configuration settings if needed
     */
    fun check()
}
