package com.r0adkll.deckbuilder.arch.ui.features.collection.adapter

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ftinc.kit.arch.util.bindView
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.widgets.ProgressLinearLayout
import com.r0adkll.deckbuilder.util.extensions.max
import com.r0adkll.deckbuilder.util.extensions.readablePercentage
import com.r0adkll.deckbuilder.util.extensions.safePercentage

sealed class UiViewHolder<I : Item>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: I)

    class MigrationViewHolder(
        itemView: View,
        private val migrateClicks: Relay<Unit>,
        private val dismissClicks: () -> Unit
    ) : UiViewHolder<Item.Migration>(itemView) {

        private val message by bindView<TextView>(R.id.message)
        private val loadingIndicator by bindView<LinearLayout>(R.id.loadingIndicator)
        private val errorMessage by bindView<TextView>(R.id.errorMessage)
        private val actionLayout by bindView<LinearLayout>(R.id.actionLayout)

        private val actionMigrate by bindView<Button>(R.id.actionMigrate)
        private val actionDismiss by bindView<Button>(R.id.actionDismiss)

        override fun bind(item: Item.Migration) {
            message.isVisible = !item.isLoading
            loadingIndicator.isVisible = item.isLoading
            errorMessage.isVisible = item.error != null
            errorMessage.text = item.error
            actionLayout.isVisible = !item.isLoading

            actionMigrate.setText(when (item.error) {
                null -> R.string.action_migrate
                else -> R.string.action_retry
            })

            actionMigrate.setOnClickListener {
                migrateClicks.accept(Unit)
            }
            actionDismiss.setOnClickListener {
                dismissClicks()
            }
        }
    }

    class ExpansionSeriesViewHolder(itemView: View) : UiViewHolder<Item.ExpansionSeries>(itemView) {

        private val title by bindView<TextView>(R.id.title)
        private val completion by bindView<TextView>(R.id.completion)

        override fun bind(item: Item.ExpansionSeries) {
            title.text = item.series
            completion.text = itemView.context.getString(R.string.completion_format,
                item.completion.readablePercentage)
        }
    }

    class ExpansionSetViewHolder(itemView: View) : UiViewHolder<Item.ExpansionSet>(itemView) {

        private val logo by bindView<ImageView>(R.id.logo)
        private val completion by bindView<TextView>(R.id.completion)
        private val count by bindView<TextView>(R.id.count)
        private val progress by bindView<ProgressLinearLayout>(R.id.progress)

        override fun bind(item: Item.ExpansionSet) {
            GlideApp.with(itemView)
                .load(item.expansion.logoUrl)
                .into(logo)

            @Suppress("MagicNumber")
            val completionProgress = (item.count.toFloat() / item.expansion.totalCards.toFloat()).safePercentage

            count.text = itemView.context.getString(
                R.string.completion_count_format,
                item.count,
                item.expansion.totalCards.max(item.count)
            )
            completion.text = itemView.context.getString(R.string.completion_format,
                completionProgress.readablePercentage)
            progress.progress = completionProgress
        }
    }

    private enum class ViewType(@LayoutRes val layoutId: Int) {
        MIGRATE(R.layout.item_collection_migration),
        SERIES(R.layout.item_collection_series),
        SET(R.layout.item_collection_expansion);

        companion object {
            val VALUES by lazy { values() }

            fun of(layoutId: Int): ViewType {
                val match = VALUES.firstOrNull { it.layoutId == layoutId }
                match?.let { return match }

                throw EnumConstantNotPresentException(ViewType::class.java, "could not find view type for $layoutId")
            }
        }
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun create(
            itemView: View,
            layoutId: Int,
            migrateClicks: Relay<Unit>,
            dismissClicks: () -> Unit
        ): UiViewHolder<Item> {
            return when (ViewType.of(layoutId)) {
                ViewType.MIGRATE -> MigrationViewHolder(itemView, migrateClicks, dismissClicks) as UiViewHolder<Item>
                ViewType.SERIES -> ExpansionSeriesViewHolder(itemView) as UiViewHolder<Item>
                ViewType.SET -> ExpansionSetViewHolder(itemView) as UiViewHolder<Item>
            }
        }
    }
}
