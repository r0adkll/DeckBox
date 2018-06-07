package com.r0adkll.deckbuilder.arch.ui.features.testing


import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import com.r0adkll.deckbuilder.arch.ui.features.testing.adapter.TestResult
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


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

                        if (result.mulligans > 0) {
                            val percentage = result.mulligans.toFloat() / result.count.toFloat()
                            testResults += TestResult(null, percentage)
                        }

                        result.startingHand.forEach {
                            val percentage = it.value.toFloat() / result.count.toFloat()
                            testResults += TestResult(it.key, percentage)
                        }

                        testResults
                    }
                }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    if (it.value != null) {
                        actions.showTestResults(it.value)
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
    }
}