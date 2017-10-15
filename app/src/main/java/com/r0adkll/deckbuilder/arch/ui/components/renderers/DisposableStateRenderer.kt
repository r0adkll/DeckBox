package com.r0adkll.deckbuilder.arch.ui.components.renderers


import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.disposables.CompositeDisposable


abstract class DisposableStateRenderer<T> : StateRenderer<T> {

    protected val disposables = CompositeDisposable()
    protected val state: Relay<T> = PublishRelay.create<T>().toSerialized()


    abstract fun start()


    open fun stop() {
        disposables.clear()
    }


    override fun render(state: T) {
        this.state.accept(state)
    }
}