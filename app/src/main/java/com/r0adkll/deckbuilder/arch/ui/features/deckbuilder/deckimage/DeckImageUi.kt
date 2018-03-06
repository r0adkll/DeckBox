package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActions
import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import io.reactivex.Observable
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


interface DeckImageUi : StateRenderer<DeckImageUi.State> {

    val state: State


    interface Intentions {

        val pickedDeckImage: Observable<DeckImage>
        val selectDeckImageClicks: Observable<Unit>
    }


    interface Actions : BaseActions {

        fun setDeckImages(images: List<DeckImage>)
        fun setSelectedDeckImage(image: DeckImage)
        fun close()
    }


    sealed class DeckImage : PaperParcelable {

        @PaperParcel
        data class Pokemon(val pokemonCard: PokemonCard) : DeckImage() {
            companion object {
                @JvmField val CREATOR = PaperParcelDeckImageUi_DeckImage_Pokemon.CREATOR
            }
        }


        @PaperParcel
        data class Type(val type1: Type, val type2: Type?) : DeckImage() {
            companion object {
                @JvmField val CREATOR = PaperParcelDeckImageUi_DeckImage_Type.CREATOR
            }
        }
    }


    @PaperParcel
    data class State(
            val sessionId: Long,
            val isLoading: Boolean,
            val error: String?,
            val images: List<DeckImage>,
            val selectedDeckImage: DeckImage?,
            val isSaved: Boolean
    ) : PaperParcelable {

        fun reduce(change: Change): State = when(change) {
            Change.IsLoading -> this.copy(isLoading = true, error = null)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
            is Change.ImagesLoaded -> this.copy(images = change.images, isLoading = false)
            is Change.ImageSelected -> this.copy(selectedDeckImage = change.image)
            Change.ImageSaved -> this.copy(isSaved = false)
        }


        sealed class Change(val logText: String) {
            object IsLoading : Change("disk -> Loading session")
            class Error(val description: String) : Change("error -> $description")
            class ImagesLoaded(val images: List<DeckImage>) : Change("disk -> ${images.size} deck images loaded")
            class ImageSelected(val image: DeckImage) : Change("user -> deck image selected: $image")
            object ImageSaved : Change("user -> deck image saved")
        }


        companion object {
            @JvmField val CREATOR = PaperParcelDeckImageUi_State.CREATOR

            val DEFAULT by lazy {
                State(-1L, false, null, emptyList(), null, false)
            }
        }
    }
}