package com.r0adkll.deckbuilder.arch.ui.features.testing.adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.widgets.TestResultView


class TestResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val testResultView = itemView as TestResultView


    fun bind(item: TestResult) {
        testResultView.percentage = item.percentage
        testResultView.isMulligan = item.pokemonCard == null
        testResultView.cardImageUrl = item.pokemonCard?.imageUrl
    }


    companion object {

        fun create(layoutInflater: LayoutInflater, parent: ViewGroup?): TestResultViewHolder {
            return TestResultViewHolder(layoutInflater.inflate(R.layout.item_test_result, parent, false))
        }
    }
}