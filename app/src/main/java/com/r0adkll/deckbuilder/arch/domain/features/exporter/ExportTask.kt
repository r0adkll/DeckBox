package com.r0adkll.deckbuilder.arch.domain.features.exporter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExportTask(val deckId: String) : Parcelable
