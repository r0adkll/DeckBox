package com.r0adkll.deckbuilder.arch.data.features.missingcard.model


/**
 * Firebase Entity for writing missing card models to firebase
 */
class MissingCardEntity(
        val name: String = "",
        val number: Int? = null,
        val description: String? = null,
        val setName: String? = null,
        val print: String = "Regular Art",

        var userId: String = "",
        val resolved: Boolean = false
)