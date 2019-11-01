package com.r0adkll.deckbuilder.arch.ui.features.testing.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerViewItemCallback

class TestResultsRecyclerAdapter(
    context: Context
) : ListAdapter<TestResult, TestResultViewHolder>(RecyclerViewItemCallback()) {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestResultViewHolder {
        return TestResultViewHolder.create(inflater, parent)
    }

    override fun onBindViewHolder(vh: TestResultViewHolder, i: Int) {
        val item = getItem(i)
        vh.bind(item)
    }
}
