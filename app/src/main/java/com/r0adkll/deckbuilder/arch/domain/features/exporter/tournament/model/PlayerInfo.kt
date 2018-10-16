package com.r0adkll.deckbuilder.arch.domain.features.exporter.tournament.model

import java.text.SimpleDateFormat
import java.util.*


data class PlayerInfo(
        val id: String,
        val name: String,
        val dob: Date,
        val ageDivision: AgeDivision,
        val format: Format
) {

    fun displayDate(): String {
        val f = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        return f.format(dob)
    }
}