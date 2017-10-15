package com.r0adkll.deckbuilder.arch.ui.components.presenter


import io.reactivex.disposables.CompositeDisposable


abstract class Presenter {

    protected val disposables = CompositeDisposable()


    abstract fun start()


    open fun stop() {
        disposables.clear()
    }
}