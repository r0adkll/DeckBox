package com.r0adkll.deckbuilder.arch.ui.features.decks

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ftinc.kit.arch.presentation.BaseFragment
import com.ftinc.kit.arch.presentation.delegates.StatefulFragmentDelegate
import com.ftinc.kit.arch.util.plusAssign
import com.ftinc.kit.extensions.snackbar
import com.ftinc.kit.widget.EmptyView
import com.jakewharton.rxrelay2.PublishRelay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.ExpansionPreview
import com.r0adkll.deckbuilder.arch.ui.Shortcuts
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderActivity
import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksUi.State
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.DecksRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.Item
import com.r0adkll.deckbuilder.arch.ui.features.decks.di.DecksModule
import com.r0adkll.deckbuilder.arch.ui.features.exporter.MultiExportActivity
import com.r0adkll.deckbuilder.arch.ui.features.home.di.HomeComponent
import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.SetBrowserActivity
import com.r0adkll.deckbuilder.arch.ui.features.testing.DeckTestingActivity
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.util.DialogUtils
import com.r0adkll.deckbuilder.util.DialogUtils.DialogText.Resource
import com.r0adkll.deckbuilder.util.ScreenUtils
import com.r0adkll.deckbuilder.util.ScreenUtils.smallestWidth
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_decks.*
import javax.inject.Inject

class DecksFragment : BaseFragment(), DecksUi, DecksUi.Intentions, DecksUi.Actions {

    override var state: State = State.DEFAULT

    @Inject lateinit var renderer: DecksRenderer
    @Inject lateinit var presenter: DecksPresenter
    @Inject lateinit var preferences: AppPreferences
    @Inject lateinit var editor: EditRepository

    private val viewPreview = PublishRelay.create<ExpansionPreview>()
    private val dismissPreview = PublishRelay.create<Unit>()
    private val shareClicks = PublishRelay.create<Deck>()
    private val duplicateClicks = PublishRelay.create<Deck>()
    private val deleteClicks = PublishRelay.create<Deck>()
    private val testClicks = PublishRelay.create<Deck>()
    private val quickStartClicks = PublishRelay.create<Deck>()
    private val dismissQuickStart = PublishRelay.create<Unit>()

    private lateinit var adapter: DecksRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_decks, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = DecksRecyclerAdapter(requireActivity(), shareClicks, duplicateClicks, deleteClicks,
            testClicks, dismissPreview, viewPreview, quickStartClicks, dismissQuickStart)

        adapter.itemClickListener = { item ->
            if (item is Item.DeckItem) {
                Analytics.event(Event.SelectContent.Deck.Opened)

                // Update shortcuts
                Shortcuts.addDeckShortcut(requireContext(), item.validatedDeck.deck)

                startActivity(DeckBuilderActivity.createIntent(requireActivity(), item.validatedDeck.deck.id))
            }
        }
        adapter.emptyView = empty_view

        recycler.layoutManager = if (smallestWidth(ScreenUtils.Config.TABLET_10)) {
            StaggeredGridLayoutManager(TABLET_SPAN_SIZE, StaggeredGridLayoutManager.VERTICAL)
        } else {
            val lm = GridLayoutManager(activity, PHONE_SPAN_SIZE)
            lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val item = adapter.currentList[position]
                    return when (item) {
                        is Item.Preview -> PHONE_SPAN_SIZE
                        is Item.QuickStart -> PHONE_SPAN_SIZE
                        is Item.Header -> PHONE_SPAN_SIZE
                        else -> SINGLE_SPAN_SIZE
                    }
                }
            }
            lm
        }

        recycler.adapter = adapter

        fab.setOnClickListener {
            Analytics.event(Event.SelectContent.Action("new_deck"))
            startActivity(DeckBuilderActivity.createIntent(requireActivity(), editor.createNewSession(), isNew = true))
        }

        if (preferences.quickStart.get()) {
            state = state.copy(quickStart = DecksUi.QuickStart())
        }

        @SuppressLint("RxSubscribeOnError")
        disposables += shareClicks
            .subscribe {
                Analytics.event(Event.SelectContent.Action("export_decklist"))
                val intent = MultiExportActivity.createIntent(requireActivity(), it.id)
                startActivity(intent)
            }

        @SuppressLint("RxSubscribeOnError")
        disposables += testClicks
            .subscribe {
                Analytics.event(Event.SelectContent.Action("test_decklist"))
                val intent = DeckTestingActivity.createIntent(requireActivity(), it.id)
                startActivity(intent)
            }

        @SuppressLint("RxSubscribeOnError")
        disposables += viewPreview
            .subscribe { preview ->
                startActivity(SetBrowserActivity.createIntent(requireActivity(), preview.code))
            }

        @SuppressLint("RxSubscribeOnError")
        disposables += dismissQuickStart
            .subscribe {
                preferences.quickStart.set(false)
            }

        @SuppressLint("RxSubscribeOnError")
        disposables += quickStartClicks
            .subscribe {
                startActivity(DeckBuilderActivity.createIntent(requireActivity(), it.id))
            }
    }

    override fun setupComponent() {
        getComponent(HomeComponent::class)
            .plus(DecksModule(this))
            .inject(this)

        delegates += StatefulFragmentDelegate(renderer, Lifecycle.Event.ON_START)
        delegates += StatefulFragmentDelegate(presenter, Lifecycle.Event.ON_START)
    }

    override fun render(state: State) {
        this.state = state
        renderer.render(state)
    }

    override fun dismissPreview(): Observable<Unit> = dismissPreview.doOnNext {
        Analytics.event(Event.SelectContent.Action("dismiss_preview"))
    }

    override fun shareClicks(): Observable<Deck> = shareClicks

    override fun duplicateClicks(): Observable<Deck> = duplicateClicks

    override fun deleteClicks(): Observable<Deck> = deleteClicks.flatMap { deck ->
        DialogUtils.confirmDialog(requireActivity(),
            Resource(R.string.dialog_delete_deck_title),
            Resource(R.string.dialog_delete_deck_message, deck.name),
            R.string.action_delete,
            R.string.action_cancel)
            .flatMap { if (it) Observable.just(deck) else Observable.empty() }
    }

    override fun showLoading(isLoading: Boolean) {
        empty_view.state = if (isLoading) {
            EmptyView.State.LOADING
        } else {
            EmptyView.State.EMPTY
        }
    }

    override fun showError(description: String) {
        snackbar(description)
    }

    override fun hideError() {
    }

    override fun showItems(items: List<Item>) {
        adapter.submitList(items)
    }

    override fun balanceShortcuts(decks: List<Deck>) {
        Shortcuts.balanceShortcuts(requireActivity(), decks)
    }

    companion object {
        private const val TABLET_SPAN_SIZE = 6
        private const val PHONE_SPAN_SIZE = 2
        private const val SINGLE_SPAN_SIZE = 1

        fun newInstance(): DecksFragment = DecksFragment()
    }
}
