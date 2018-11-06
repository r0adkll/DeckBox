package com.r0adkll.deckbuilder.arch.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExportTask(
        val deckId: String?,
        val sessionId: Long?
) : Parcelable