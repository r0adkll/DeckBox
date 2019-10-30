package com.r0adkll.deckbuilder.arch.domain.features.expansions.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Expansion(
        val code: String,
        val ptcgoCode: String? = null,
        val name: String = "",
        val series: String = "",
        val totalCards: Int = 0,
        val standardLegal: Boolean = false,
        val expandedLegal: Boolean = false,
        val releaseDate: String = "",
        val symbolUrl: String = "",
        val logoUrl: String? = null,
        val isPreview: Boolean = false
) : Parcelable
