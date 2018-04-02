package com.r0adkll.deckbuilder.arch.domain.features.testing


import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class TestResults(
        val count: Int, // The total number of shuffle iterations ran
        val mulligans: Int, // The total number of mulligans that occured
        val startingHand: Map<String, Int> // The number of times a card has appeared in the starting hand for [count] iterations
) : PaperParcelable {

    companion object {
        @JvmField val CREATOR = PaperParcelTestResults.CREATOR
    }
}