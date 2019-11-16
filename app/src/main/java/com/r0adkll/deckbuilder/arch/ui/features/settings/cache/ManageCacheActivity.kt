package com.r0adkll.deckbuilder.arch.ui.features.settings.cache

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Lifecycle
import com.ftinc.kit.arch.presentation.BaseActivity
import com.ftinc.kit.arch.presentation.delegates.StatefulActivityDelegate
import com.ftinc.kit.extensions.snackbar
import com.jakewharton.rxrelay2.PublishRelay
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.ui.features.settings.cache.ManageCacheUi.State
import com.r0adkll.deckbuilder.arch.ui.features.settings.cache.adapter.ExpansionCache
import com.r0adkll.deckbuilder.arch.ui.features.settings.cache.adapter.ExpansionCacheRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.settings.cache.di.ManageCacheModule
import com.r0adkll.deckbuilder.util.DialogUtils
import com.r0adkll.deckbuilder.util.extensions.setLoading
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_manage_offline.*
import javax.inject.Inject

class ManageCacheActivity : BaseActivity(), ManageCacheUi, ManageCacheUi.Intentions, ManageCacheUi.Actions {

    override var state: State = State.DEFAULT

    @Inject lateinit var renderer: ManageCacheRenderer
    @Inject lateinit var presenter: ManageCachePresenter

    private val deleteClicks = PublishRelay.create<Expansion>()
    private val deleteAllClicks = PublishRelay.create<Unit>()
    private lateinit var adapter: ExpansionCacheRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_offline)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appbar?.setNavigationOnClickListener { finish() }

        adapter = ExpansionCacheRecyclerAdapter(this, deleteClicks)
        adapter.emptyView = emptyView
        recycler.adapter = adapter
    }

    override fun setupComponent() {
        DeckApp.component
            .plus(ManageCacheModule(this))
            .inject(this)

        delegates += StatefulActivityDelegate(renderer, Lifecycle.Event.ON_START)
        delegates += StatefulActivityDelegate(presenter, Lifecycle.Event.ON_START)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_manage_cache, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionDeleteAll -> {
                deleteAllClicks.accept(Unit)
                true
            }
            else -> false
        }
    }

    override fun render(state: State) {
        this.state = state
        renderer.render(state)
    }

    override fun deleteAllCache(): Observable<Unit> {
        return deleteAllClicks
            .flatMap {
                DialogUtils.confirmDialog(
                    this,
                    DialogUtils.DialogText.Resource(R.string.dialog_delete_all_cache_title),
                    DialogUtils.DialogText.Resource(R.string.dialog_delete_all_cache_message),
                    R.string.action_delete_all,
                    android.R.string.cancel
                )
                    .filter { it }
                    .map { Unit }
            }
    }

    override fun deleteCache(): Observable<Expansion> {
        return deleteClicks
            .flatMap { expansion ->
                DialogUtils.confirmDialog(
                    this,
                    DialogUtils.DialogText.Literal(
                        getString(R.string.dialog_delete_expansion_cache_title, expansion.name)
                    ),
                    DialogUtils.DialogText.Literal(
                        getString(R.string.dialog_delete_expansion_cache_message, expansion.name)
                    ),
                    R.string.action_clear,
                    android.R.string.cancel
                )
                    .filter { it }
                    .map { expansion }
            }
    }

    override fun setTotalSize(size: String, label: String) {
        cacheSize.text = size
        sizeLabel.text = label
    }

    override fun setItems(items: List<ExpansionCache>) {
        adapter.submitList(items)
    }

    override fun hideError() {
    }

    override fun showError(description: String) {
        snackbar(description)
    }

    override fun showLoading(isLoading: Boolean) {
        emptyView.setLoading(isLoading)
    }

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, ManageCacheActivity::class.java)
        }
    }
}
