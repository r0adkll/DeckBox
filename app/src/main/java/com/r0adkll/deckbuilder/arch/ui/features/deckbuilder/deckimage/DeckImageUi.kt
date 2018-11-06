package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage


import android.os.Parcelable
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActions
import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize


interface DeckImageUi : StateRenderer<DeckImageUi.State> {

    val state: State


    interface Intentions {

        val pickedDeckImage: Observable<DeckImage>
        val selectDeckImageClicks: Observable<Unit>
    }


    interface Actions : BaseActions {

        fun setDeckImages(images: List<DeckImage>)
        fun setSelectedDeckImage(image: DeckImage?)
        fun close()
    }


    @Parcelize
    data class State(
            val sessionId: Long,
            val isLoading: Boolean,
            val error: String?,
            val cards: List<PokemonCard>,
            val selectedDeckImage: DeckImage?,
            val isSaved: Boolean
    ) : Parcelable {

        fun reduce(change: Change): State = when(change) {
            Change.IsLoading -> this.copy(isLoading = true, error = null)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
            is Change.CardsLoaded -> this.copy(cards = change.cards, isLoading = false)
            is Change.ImageSelected -> {
                this.copy(selectedDeckImage = change.image)
            }
            Change.ImageSaved -> this.copy(isSaved = true)
        }


        sealed class Change(val logText: String) {
            object IsLoading : Change("disk -> Loading session")
            class Error(val description: String) : Change("error -> $description")
            class CardsLoaded(val cards: List<PokemonCard>) : Change("disk -> ${cards.size} deck cards loaded")
            class ImageSelected(val image: DeckImage) : Change("user -> deck image selected: $image")
            object ImageSaved : Change("user -> deck image saved")
        }


        companion object {

            val DEFAULT by lazy {
                State(-1L, false, null, emptyList(), null, false)
            }
        }
    }
}