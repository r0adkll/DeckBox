package com.r0adkll.deckbuilder.arch.domain.features.community.model

import android.os.Parcelable
import com.r0adkll.deckbuilder.arch.domain.Format
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tournament(
        val name: String,
        val date: String,
        val country: String,
        val format: Format,
        val playerCount: Int
): Parcelable
