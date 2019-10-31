package com.r0adkll.deckbuilder.arch.ui.components.renderers

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

abstract class DisposableStateRenderer<T>(
        val main: Scheduler,
        val comp: Scheduler
) : StateRenderer<T> {

    protected val disposables = CompositeDisposable()
    protected val state: Relay<T> = PublishRelay.create<T>().toSerialized()

    abstract fun start()

    open fun stop() {
        disposables.clear()
    }

    override fun render(state: T) {
        this.state.accept(state)
    }

    protected fun <T> Observable<T>.addToLifecycle(): Observable<T> {
        return this.subscribeOn(comp)
                .observeOn(main)
    }
}
