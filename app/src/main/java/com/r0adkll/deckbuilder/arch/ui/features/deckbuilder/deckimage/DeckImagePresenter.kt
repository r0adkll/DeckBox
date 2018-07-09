package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage


import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.DeckImageUi.State
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.DeckImageUi.State.Change
import com.r0adkll.deckbuilder.util.extensions.logState
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import timber.log.Timber
import javax.inject.Inject


class DeckImagePresenter @Inject constructor(
        val ui: DeckImageUi,
        val intentions: DeckImageUi.Intentions,
        val repository: EditRepository
) : Presenter() {

    override fun start() {

        val loadImages = repository.getSession(ui.state.sessionId)
                .map { Change.CardsLoaded(it.cards) as Change }
                .onErrorReturn(handleUnknownError)

        val deckImageClicks = intentions.pickedDeckImage
                .map { Change.ImageSelected(it) as Change }

        val deckImageSelected = intentions.selectDeckImageClicks
                .flatMap {
                    repository.changeDeckImage(ui.state.sessionId, ui.state.selectedDeckImage!!)
                            .map { Change.ImageSaved as Change }
                }


        val merged = loadImages
                .mergeWith(deckImageClicks)
                .mergeWith(deckImageSelected)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .logState()
                .subscribe { ui.render(it) }
    }


    companion object {

        val handleUnknownError: (Throwable) -> Change = {
            Timber.e(it, "Unknown error loading session")
            Change.Error("Unable to find any Pokémon cards in your deck to set an image with.")
        }
    }
}