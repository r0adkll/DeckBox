package com.r0adkll.deckbuilder.arch.ui.features.testing.adapter


import android.content.Context
import android.view.ViewGroup
import com.r0adkll.deckbuilder.arch.ui.components.ListRecyclerAdapter


class TestResultsRecyclerAdapter(context: Context) : ListRecyclerAdapter<TestResult, TestResultViewHolder>(context) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestResultViewHolder {
        return TestResultViewHolder.create(inflater, parent)
    }


    override fun onBindViewHolder(vh: TestResultViewHolder, i: Int) {
        val item = items[i]
        vh.bind(item)
    }


    fun setTestResults(results: List<TestResult>) {
        val diff = calculateDiff(results, items)
        items = ArrayList(diff.new)
        diff.diff.dispatchUpdatesTo(getListUpdateCallback())
    }
}