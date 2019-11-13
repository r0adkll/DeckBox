package com.r0adkll.deckbuilder.arch.domain.features.offline.model

import android.os.Parcelable
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DownloadRequest(
    val expansion: List<Expansion>,
    val includeHiRes: Boolean,
    val wifiOnly: Boolean = false
) : Parcelable
