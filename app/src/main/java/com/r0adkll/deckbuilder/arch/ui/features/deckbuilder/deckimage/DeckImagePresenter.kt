package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage

import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.DeckImageUi.State
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.DeckImageUi.State.Change
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class DeckImagePresenter @Inject constructor(
    ui: DeckImageUi,
    val intentions: DeckImageUi.Intentions,
    val repository: EditRepository
) : UiPresenter<State, Change>(ui) {

    override fun smashObservables(): Observable<Change> {

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

        return loadImages
            .mergeWith(deckImageClicks)
            .mergeWith(deckImageSelected)
    }

    companion object {

        val handleUnknownError: (Throwable) -> Change = {
            Timber.e(it, "Unknown error loading session")
            Change.Error("Unable to find any Pokémon cards in your deck to set an image with.")
        }
    }
}
