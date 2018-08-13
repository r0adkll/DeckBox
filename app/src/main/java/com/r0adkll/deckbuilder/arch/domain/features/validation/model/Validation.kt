package com.r0adkll.deckbuilder.arch.domain.features.validation.model


import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class Validation(
        val standard: Boolean,
        val expanded: Boolean,
        val rules: List<Int>
) : PaperParcelable {

    val isValid: Boolean get() = rules.isEmpty()


    companion object {
        @JvmField val CREATOR = PaperParcelValidation.CREATOR
    }
}