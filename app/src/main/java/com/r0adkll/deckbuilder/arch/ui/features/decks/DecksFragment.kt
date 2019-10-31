package com.r0adkll.deckbuilder.arch.ui.features.decks

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jakewharton.rxrelay2.PublishRelay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.ExpansionPreview
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.Shortcuts
import com.r0adkll.deckbuilder.arch.ui.components.BaseFragment
import com.r0adkll.deckbuilder.arch.ui.features.browse.SetBrowserActivity
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderActivity
import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksUi.State
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.DecksRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.Item
import com.r0adkll.deckbuilder.arch.ui.features.decks.di.DecksModule
import com.r0adkll.deckbuilder.arch.ui.features.exporter.MultiExportActivity
import com.r0adkll.deckbuilder.arch.ui.features.home.di.HomeComponent
import com.r0adkll.deckbuilder.arch.ui.features.testing.DeckTestingActivity
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.util.DialogUtils
import com.r0adkll.deckbuilder.util.DialogUtils.DialogText.*
import com.r0adkll.deckbuilder.util.ScreenUtils
import com.r0adkll.deckbuilder.util.ScreenUtils.smallestWidth
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import com.r0adkll.deckbuilder.util.extensions.snackbar
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_decks.*
import javax.inject.Inject

class DecksFragment : BaseFragment(), DecksUi, DecksUi.Intentions, DecksUi.Actions {

    override var state: State = State.DEFAULT

    @Inject lateinit var renderer: DecksRenderer
    @Inject lateinit var presenter: DecksPresenter
    @Inject lateinit var preferences: AppPreferences

    private val viewPreview = PublishRelay.create<ExpansionPreview>()
    private val dismissPreview = PublishRelay.create<Unit>()
    private val shareClicks = PublishRelay.create<Deck>()
    private val duplicateClicks = PublishRelay.create<Deck>()
    private val deleteClicks = PublishRelay.create<Deck>()
    private val testClicks = PublishRelay.create<Deck>()
    private val quickStartClicks = PublishRelay.create<Deck>()
    private val dismissQuickStart = PublishRelay.create<Unit>()
    private val createSession = PublishRelay.create<Deck>()
    private val createNewSession = PublishRelay.create<Unit>()
    private val clearSession = PublishRelay.create<Unit>()

    private lateinit var adapter: DecksRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_decks, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = DecksRecyclerAdapter(activity!!, shareClicks, duplicateClicks, deleteClicks,
                testClicks, dismissPreview, viewPreview, quickStartClicks, dismissQuickStart)
        adapter.itemClickListener = { item ->
            if (item is Item.DeckItem) {
                Analytics.event(Event.SelectContent.Deck.Opened)

                // Update shortcuts
                Shortcuts.addDeckShortcut(requireContext(), item.validatedDeck.deck)

                // Generate a new session and pass to builder activity
                createSession.accept(item.validatedDeck.deck)
            }
        }
        adapter.emptyView = empty_view

        recycler.layoutManager = if (smallestWidth(ScreenUtils.Config.TABLET_10)) {
            StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL)
        } else {
            val lm = GridLayoutManager(activity, 2)
            lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val item = adapter.currentList[position]
                    return when(item) {
                        is Item.Preview -> 2
                        is Item.QuickStart -> 2
                        is Item.Header -> 2
                        else -> 1
                    }
                }
            }
            lm
        }

        recycler.adapter = adapter

        fab.setOnClickListener {
            Analytics.event(Event.SelectContent.Action("new_deck"))
            createNewSession.accept(Unit)
        }

        if (preferences.quickStart.get()) {
            state = state.copy(quickStart = DecksUi.QuickStart())
        }

        @SuppressLint("RxSubscribeOnError")
        disposables += shareClicks
                .subscribe {
                    Analytics.event(Event.SelectContent.Action("export_decklist"))
                    val intent = MultiExportActivity.createIntent(activity!!, it)
                    startActivity(intent)
                }

        @SuppressLint("RxSubscribeOnError")
        disposables += testClicks
                .subscribe {
                    Analytics.event(Event.SelectContent.Action("test_decklist"))
                    val intent = DeckTestingActivity.createIntent(activity!!, it.id)
                    startActivity(intent)
                }

        @SuppressLint("RxSubscribeOnError")
        disposables += viewPreview
                .subscribe { preview ->
                    startActivity(SetBrowserActivity.createIntent(activity!!, preview.code))
                }

        @SuppressLint("RxSubscribeOnError")
        disposables += dismissQuickStart
                .subscribe {
                    preferences.quickStart.set(false)
                }

        @SuppressLint("RxSubscribeOnError")
        disposables += quickStartClicks
                .subscribe {
                    createSession.accept(it)
                }
    }

    override fun onStart() {
        super.onStart()
        renderer.start()
        presenter.start()
    }

    override fun onStop() {
        super.onStop()
        renderer.stop()
        presenter.stop()
    }

    override fun setupComponent() {
        getComponent(HomeComponent::class)
                .plus(DecksModule(this))
                .inject(this)
    }

    override fun render(state: State) {
        this.state = state
        renderer.render(state)
    }

    override fun createSession(): Observable<Deck> = createSession
    override fun createNewSession(): Observable<Unit> = createNewSession
    override fun clearSession(): Observable<Unit> = clearSession
    override fun dismissPreview(): Observable<Unit> = dismissPreview.doOnNext { Analytics.event(Event.SelectContent.Action("dismiss_preview")) }
    override fun shareClicks(): Observable<Deck> = shareClicks
    override fun duplicateClicks(): Observable<Deck> = duplicateClicks
    override fun deleteClicks(): Observable<Deck> = deleteClicks.flatMap { deck ->
        DialogUtils.confirmDialog(activity!!,
                Resource(R.string.dialog_delete_deck_title),
                Resource(R.string.dialog_delete_deck_message, deck.name),
                R.string.action_delete,
                R.string.action_cancel)
                .flatMap { if (it) Observable.just(deck) else Observable.empty() }
    }

    override fun showLoading(isLoading: Boolean) {
        empty_view.setLoading(isLoading)
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
        Shortcuts.balanceShortcuts(activity!!, decks)
    }

    override fun openSession(sessionId: Long) {
        clearSession.accept(Unit)
        startActivity(DeckBuilderActivity.createIntent(activity!!, sessionId))
    }

    companion object {

        fun newInstance(): DecksFragment = DecksFragment()
    }
}
