package com.r0adkll.deckbuilder.arch.ui.features.testing


import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import com.r0adkll.deckbuilder.arch.ui.features.testing.adapter.TestResult
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler
import timber.log.Timber
import kotlin.math.max


class DeckTestingRenderer(
        actions: DeckTestingUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : UiBaseStateRenderer<DeckTestingUi.State, DeckTestingUi.State.Change, DeckTestingUi.Actions>(actions, main, comp) {


    override fun onStart() {

        disposables += state
                .mapNullable {
                    it.results?.let { result ->
                        val testResults = ArrayList<TestResult>()

                        val cumulativeResultCount = result.startingHand.values.sum().toFloat()
                        val maxHandPercentage = ((result.startingHand.values.max()?.toFloat() ?: 1f) / cumulativeResultCount) + 0.1f
                        val maxMulliganPercentage = result.mulligans.toFloat() / result.count.toFloat()
                        val maxPercentage = max(maxHandPercentage, maxMulliganPercentage)

                        if (result.mulligans > 0) {
                            val percentage = result.mulligans.toFloat() / result.count.toFloat()
                            testResults += TestResult(null, percentage, maxPercentage)
                        }

                        result.startingHand.entries
                                .sortedByDescending { it.value }
                                .forEach {
                                    val percentage = it.value.toFloat() / cumulativeResultCount
                                    testResults += TestResult(it.key, percentage, maxPercentage)
                                    Timber.i("Result($percentage, max: $maxPercentage)")
                                }

                        testResults
                    }
                }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    if (it.value != null) {
                        actions.showTestResults(it.value)
                    } else {
                        actions.hideTestResults()
                    }
                }

        disposables += state
                .mapNullable { it.hand }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    if (it.value != null) {
                        actions.showTestHand(it.value)
                    } else {
                        actions.hideTestHand()
                    }
                }

        disposables += state
                .mapNullable { it.metadata }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    if (it.value != null) {
                        actions.setMetadata(it.value)
                    }
                }

        disposables += state
                .map { it.iterations }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setTestIterations(it) }

        disposables += state
                .map {
                    (it.hand == null && it.results == null) || it.isLoading
                }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    if (it) {
                        actions.showEmptyView()
                    } else {
                        actions.hideEmptyView()
                    }
                }
    }
}