package com.r0adkll.deckbuilder.arch.domain.features.offline.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


sealed class CacheStatus : Parcelable {

    @Parcelize
    object Empty : CacheStatus()

    @Parcelize
    object Cached : CacheStatus()

    @Parcelize
    object Downloading : CacheStatus()
}