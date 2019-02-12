package com.r0adkll.deckbuilder.arch.ui.features.collection.set

import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import io.reactivex.Observable


class CollectionSetPresenter(
        ui: CollectionSetUi,
        val intentions: CollectionSetUi.Intentions
) : UiPresenter<CollectionSetUi.State, CollectionSetUi.State.Change>(ui) {

    override fun smashObservables(): Observable<CollectionSetUi.State.Change> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}