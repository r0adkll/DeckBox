package com.r0adkll.deckbuilder.arch.domain.features.community.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeckInfo(val name: String, val iconUrl: String) : Parcelable
