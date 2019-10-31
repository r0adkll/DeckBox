package com.r0adkll.deckbuilder.arch.domain.features.editing.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Change(
        val id: Long,
        val cardId: String,
        val change: Int,
        val searchSessionId: String?
) : Parcelable
