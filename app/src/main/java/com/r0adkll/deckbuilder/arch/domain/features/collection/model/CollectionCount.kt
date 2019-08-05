package com.r0adkll.deckbuilder.arch.domain.features.collection.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CollectionCount(
        val id: String,
        val count: Int,
        val set: String,
        val series: String
) : Parcelable