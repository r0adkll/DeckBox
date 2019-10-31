package com.r0adkll.deckbuilder.arch.domain.features.validation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Validation(
        val standard: Boolean,
        val expanded: Boolean,
        val rules: List<Int>
) : Parcelable {

    val isValid: Boolean get() = rules.isEmpty()
}
