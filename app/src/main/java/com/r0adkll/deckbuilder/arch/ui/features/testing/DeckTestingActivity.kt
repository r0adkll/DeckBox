package com.r0adkll.deckbuilder.arch.ui.features.testing


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.ftinc.kit.arch.presentation.BaseActivity
import com.ftinc.kit.arch.presentation.delegates.StatefulActivityDelegate
import com.ftinc.kit.arch.util.bindViews
import com.ftinc.kit.arch.util.uiDebounce
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.ftinc.kit.kotlin.extensions.gone
import com.ftinc.kit.kotlin.extensions.toast
import com.ftinc.kit.kotlin.extensions.visible
import com.ftinc.kit.kotlin.utils.bindLong
import com.ftinc.kit.kotlin.utils.bindOptionalString
import com.ftinc.kit.widget.DividerSpacerItemDecoration
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.longClicks
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.features.testing.DeckTestingUi.State
import com.r0adkll.deckbuilder.arch.ui.features.testing.adapter.TestResult
import com.r0adkll.deckbuilder.arch.ui.features.testing.adapter.TestResultsRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.testing.di.DeckTestingModule
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.util.extensions.fromHtml
import com.r0adkll.deckbuilder.util.extensions.isMulligan
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_deck_testing.*
import timber.log.Timber
import javax.inject.Inject


class DeckTestingActivity : BaseActivity(), DeckTestingUi, DeckTestingUi.Intentions, DeckTestingUi.Actions {

    private val sessionId: Long by bindLong(EXTRA_SESSION_ID, -1L)
    private val deckId: String? by bindOptionalString(EXTRA_DECK_ID)
    private val cards: List<PokemonCardView> by bindViews(R.id.card1, R.id.card2, R.id.card3,
            R.id.card4, R.id.card5, R.id.card6, R.id.card7)

    override var state: State = State.DEFAULT

    @Inject lateinit var renderer: DeckTestingRenderer
    @Inject lateinit var presenter: DeckTestingPresenter

    private lateinit var adapter: TestResultsRecyclerAdapter


    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_testing)

        // Configure state
        if (sessionId != -1L) {
            state = state.copy(sessionId = sessionId)
        } else if (deckId != null) {
            state = state.copy(deckId = deckId)
        }

        // Setup appbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appbar?.setNavigationOnClickListener { finish() }

        // Setup result recycler
        adapter = TestResultsRecyclerAdapter(this)
        recycler.adapter = adapter
        recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recycler.addItemDecoration(DividerSpacerItemDecoration(dpToPx(8f), false))

        inputIterations.setOnClickListener { toast(R.string.test_iterations_explanation) }
    }


    override fun setupComponent() {
        DeckApp.component.plus(DeckTestingModule(this))
                .inject(this)

        delegates += StatefulActivityDelegate(renderer)
        delegates += StatefulActivityDelegate(presenter)
    }


    override fun render(state: State) {
        this.state = state
        renderer.render(state)
    }


    override fun testSingleHand(): Observable<Int> {
        return actionTestSingleHand.clicks()
                .uiDebounce()
                .flatMap {
                    Observable.create<Unit> { e ->
                        val superSet = createHideHandAnimation()
                        superSet.addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                e.onNext(Unit)
                                e.onComplete()
                            }

                            override fun onAnimationCancel(animation: Animator?) {
                                e.onNext(Unit)
                                e.onComplete()
                            }
                        })
                        superSet.start()
                    }.subscribeOn(AndroidSchedulers.mainThread())
                }
                .map { state.iterations }
                .doOnNext { Analytics.event(Event.SelectContent.Action("test_deck", value = it.toLong())) }
    }


    override fun testOverallHands(): Observable<Int> {
        return actionTestOverall.clicks()
                .uiDebounce()
                .map { state.iterations }
    }


    override fun incrementIterations(): Observable<Int> {
        val incrementSmall = actionIterationPlus.clicks()
                .map { STEP }

        val incrementLarge = actionIterationPlus.longClicks()
                .map { LARGE_STEP }

        return incrementSmall.mergeWith(incrementLarge)
                .doOnNext { Analytics.event(Event.SelectContent.Action("increment_test_iterations", value = it.toLong())) }
    }


    override fun decrementIterations(): Observable<Int> {
        val decrementSmall = actionIterationMinus.clicks()
                .map { STEP }

        val decrementLarge = actionIterationMinus.longClicks()
                .map { LARGE_STEP }

        return decrementSmall.mergeWith(decrementLarge)
                .doOnNext { Analytics.event(Event.SelectContent.Action("decrement_test_iterations", value = it.toLong())) }
    }


    override fun setMetadata(metadata: DeckTestingUi.Metadata) {
        name.text = metadata.name
        description.text = if (metadata.description.isEmpty()) getString(R.string.empty_metadata_description).fromHtml() else metadata.description
        cardCount.count(metadata.pokemon, metadata.trainer, metadata.energy)
    }


    override fun showTestResults(results: List<TestResult>) {
        adapter.setTestResults(results)
    }


    override fun showTestHand(hand: List<PokemonCard>) {
        val futures = hand.map {
            GlideApp.with(this)
                    .load(it.imageUrl)
                    .submit()
        }

        disposables += Observable.just(futures)
                .map { it.map { it.get() } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ images ->
                    field.post { // Ensure that this occurs when things have been layed out so the math doesn't get wonky
                        (0 until cards.size).forEach {
                            cards[it].setImageDrawable(images[it])
                            val isBasic = hand[it].let { it.supertype == SuperType.POKEMON
                                    && (it.subtype == SubType.BASIC || it.evolvesFrom.isNullOrBlank()) }
                            cards[it].elevation = if (isBasic) dpToPx(4f) else 0f
                            cards[it].scaleX = if (isBasic) 1.05f else 1f
                            cards[it].scaleY = if (isBasic) 1.05f else 1f
                        }

                        // Animate cards out
                        val width = field.width
                        val height = field.height
                        val outerMargin = dpToPx(32f)
                        val innerMargin = dpToPx(8f)
                        val cardWidth = ((width - ((2 * outerMargin) + (3 * innerMargin))) / 4f).toInt()
                        val lowerOuterMargin = (width - ((3 * cardWidth) + (2 * innerMargin))) / 2f
                        Timber.i("Showing Hand (width: $width, height: $height, outerMargin: $outerMargin, lowerOuterMargin: $lowerOuterMargin, innerMargin: $innerMargin, cardWidth: $cardWidth)")
                        (0 until 4).forEach {
                            val x = outerMargin + (it * cardWidth) + (it * innerMargin)
                            val y = (height / 2f) + ((cardWidth * PokemonCardView.RATIO) + innerMargin/2f) + dpToPx(8f)
                            Timber.i("Card($it) [x: $x, y: $y]")

                            val card = cards[it]
                            val lp = card.layoutParams
                            if (lp.width != cardWidth) {
                                lp.width = cardWidth
                                lp.height = (cardWidth * PokemonCardView.RATIO).toInt()
                                card.layoutParams = lp
                            }

                            val translationX = -(card.x - x)
                            val translationY = -y
                            val startDelay = it * DEAL_ANIMATION_DELAY
                            if (card.translationX != translationX || card.translationY != translationY) {
                                card.animate()
                                        .translationX(translationX)
                                        .translationY(translationY)
                                        .setDuration(DEAL_ANIMATION_DURATION)
                                        .setStartDelay(startDelay)
                                        .setInterpolator(FastOutSlowInInterpolator())
                                        .start()
                            }
                        }

                        (0 until 3).forEach {
                            val x = lowerOuterMargin + (it * cardWidth) + (it * innerMargin)
                            val y = (height / 2f) - (innerMargin / 2f)
                            Timber.i("Card(${it + 4}) [x: $x, y: $y]")
                            val card = cards[4 + it]
                            val lp = card.layoutParams
                            if (lp.width != cardWidth) {
                                lp.width = cardWidth
                                lp.height = (cardWidth * PokemonCardView.RATIO).toInt()
                                card.layoutParams = lp
                            }

                            val translationX = -(card.x - x)
                            val translationY = -y
                            val startDelay = (it + 4) * DEAL_ANIMATION_DELAY
                            if (card.translationX != translationX || card.translationY != translationY) {
                                card.animate()
                                        .translationX(translationX)
                                        .translationY(translationY)
                                        .setDuration(DEAL_ANIMATION_DURATION)
                                        .setStartDelay(startDelay)
                                        .setInterpolator(FastOutSlowInInterpolator())
                                        .start()
                            }
                        }


                        if (hand.isMulligan()) {
                            showError(getString(R.string.mulligan))
                        } else {
                            hideError()
                        }
                    }
                }, {
                    Timber.e(it, "Error loading all card images")
                })

    }


    override fun setTestIterations(iterations: Int) {
        inputIterations.text = "$iterations"
    }


    override fun showLoading(isLoading: Boolean) {
        hideError()
        actionTestSingleHand.isEnabled = !isLoading
        actionTestOverall.isEnabled = !isLoading
        emptyView.setLoading(isLoading)
    }


    override fun showError(description: String) {
        errorBar.text = description
        errorBar.visible()
    }


    override fun hideError() {
        errorBar.gone()
    }


    override fun hideTestHand() {
        createHideHandAnimation().start()
    }


    override fun hideTestResults() {
        adapter.clear()
        adapter.notifyDataSetChanged()
    }


    override fun showEmptyView() {
        emptyView.visible()
    }


    override fun hideEmptyView() {
        emptyView.gone()
    }


    private fun createHideHandAnimation(): AnimatorSet {
        val animators = cards.map {
            it.elevation = 0f
            val x = ObjectAnimator.ofFloat(it, "translationX", 0f)
            val y = ObjectAnimator.ofFloat(it, "translationY", 0f)
            val set = AnimatorSet()
            set.playTogether(x, y)
            set.duration = DEAL_RETURN_ANIMATION_DURATION
            set.interpolator = FastOutSlowInInterpolator()
            set
        }

        val superSet = AnimatorSet()
        superSet.playTogether(animators)
        return superSet
    }


    companion object {
        private const val EXTRA_SESSION_ID = "DeckTestingActivity.SessionId"
        private const val EXTRA_DECK_ID = "DeckTestingActivity.DeckId"
        private const val STEP = 100
        private const val LARGE_STEP = 1000

        private const val DEAL_ANIMATION_DELAY = 100L
        private const val DEAL_ANIMATION_DURATION = 250L
        private const val DEAL_RETURN_ANIMATION_DURATION = 200L


        fun createIntent(context: Context, sessionId: Long): Intent {
            val intent = Intent(context, DeckTestingActivity::class.java)
            intent.putExtra(EXTRA_SESSION_ID, sessionId)
            return intent
        }


        fun createIntent(context: Context, deckId: String): Intent {
            val intent = Intent(context, DeckTestingActivity::class.java)
            intent.putExtra(EXTRA_DECK_ID, deckId)
            return intent
        }
    }
}