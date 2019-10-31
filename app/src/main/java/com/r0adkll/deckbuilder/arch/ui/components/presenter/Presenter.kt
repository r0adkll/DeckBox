package com.r0adkll.deckbuilder.arch.ui.components.presenter

import io.reactivex.disposables.CompositeDisposable

@Deprecated("Move to use 52Kit arch lib")
abstract class Presenter {

    protected val disposables = CompositeDisposable()

    abstract fun start()

    open fun stop() {
        disposables.clear()
    }
}
