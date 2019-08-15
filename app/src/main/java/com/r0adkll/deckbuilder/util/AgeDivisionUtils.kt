package com.r0adkll.deckbuilder.util

import android.content.Context
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.exporter.tournament.model.AgeDivision
import com.r0adkll.deckbuilder.arch.domain.features.exporter.tournament.model.AgeDivision.*
import java.util.*

object AgeDivisionUtils {

    val seasonYear: Int
        get() = Calendar.getInstance().let {
            val year = it[Calendar.YEAR]
            val month = it[Calendar.MONTH]
            if (month >= Calendar.AUGUST) {
                year + 1
            } else {
                year
            }
        }

    fun divisionLabel(context: Context, ageDivision: AgeDivision): String {
        return when(ageDivision) {
            JUNIOR -> context.getString(R.string.age_division_junior, seasonYear - 11)
            SENIOR -> context.getString(R.string.age_division_senior, seasonYear - 15, seasonYear - 12)
            MASTERS -> context.getString(R.string.age_division_masters, seasonYear - 16)
        }
    }
}