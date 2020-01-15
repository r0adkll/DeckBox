package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage

import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Edit
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.DeckImageUi.State
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.DeckImageUi.State.Change
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class DeckImagePresenter @Inject constructor(
    ui: DeckImageUi,
    val intentions: DeckImageUi.Intentions,
    val deckRepository: DeckRepository,
    val editor: EditRepository
) : UiPresenter<State, Change>(ui) {

    override fun smashObservables(): Observable<Change> {

        val deckImageClicks = intentions.pickedDeckImage
            .map { Change.ImageSelected(it) as Change }

        val deckImageSelected = intentions.selectDeckImageClicks
            .flatMap {
                editor.submit(ui.state.deckId, Edit.Image(ui.state.selectedDeckImage!!))
                    .map { Change.ImageSaved as Change }
            }

        val observeDeck = editor.observeSession(ui.state.deckId)
            .map { Change.CardsLoaded(it.cards) as Change }
            .onErrorReturn(handleUnknownError)

        return observeDeck
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
