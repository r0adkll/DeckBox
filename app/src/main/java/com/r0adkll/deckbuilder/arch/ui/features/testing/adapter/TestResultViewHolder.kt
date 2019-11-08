package com.r0adkll.deckbuilder.arch.ui.features.testing.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ftinc.kit.arch.util.bindView
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.widgets.TestResultProgressView
import com.r0adkll.deckbuilder.cache.CardImageKey

@Suppress("MagicNumber")
class TestResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val background: TestResultProgressView by bindView(R.id.background)
    private val imageView: ImageView by bindView(R.id.pokemonImage)
    private val percent: TextView by bindView(R.id.percent)

    @SuppressLint("SetTextI18n")
    fun bind(item: TestResult) {
        background.percentage = item.percentage * 100f
        background.maxPercentage = item.maxPercentage * 100f
        background.isMulligan = item.pokemonCard == null
        background.requestLayout()
        percent.text = "${background.percentage.toInt()}%"

        GlideApp.with(itemView)
            .load(item.pokemonCard?.imageUrl)
            .signature(CardImageKey(
                item.pokemonCard?.expansion?.code ?: "",
                item.pokemonCard?.id ?: "",
                CardImageKey.Type.NORMAL
            ))
            .into(imageView)
    }

    companion object {

        fun create(layoutInflater: LayoutInflater, parent: ViewGroup?): TestResultViewHolder {
            return TestResultViewHolder(layoutInflater.inflate(R.layout.item_test_result2, parent, false))
        }
    }
}
