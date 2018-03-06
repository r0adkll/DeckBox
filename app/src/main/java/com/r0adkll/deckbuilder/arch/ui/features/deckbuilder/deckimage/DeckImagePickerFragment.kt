package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ftinc.kit.kotlin.utils.bindArgument
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.util.bindLong
import com.r0adkll.deckbuilder.util.bundle
import io.reactivex.Observable


class DeckImagePickerFragment : DialogFragment(), DeckImageUi, DeckImageUi.Intentions, DeckImageUi.Actions {

    override var state: DeckImageUi.State = DeckImageUi.State.DEFAULT
    private val sessionId: Long by bindLong(EXTRA_SESSION_ID)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deck_image_picker, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        state = state.copy(sessionId = sessionId)


    }


    override fun render(state: DeckImageUi.State) {
        this.state = state

    }


    override val pickedDeckImage: Observable<DeckImageUi.DeckImage>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.


    override val selectDeckImageClicks: Observable<Unit>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.


    override fun setDeckImages(images: List<DeckImageUi.DeckImage>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun setSelectedDeckImage(image: DeckImageUi.DeckImage) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun close() {
        dismiss()
    }


    override fun showLoading(isLoading: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun showError(description: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun hideError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    companion object {
        private const val EXTRA_SESSION_ID = "DeckImagePickerFragment.SessionId"


        fun newInstance(sessionId: Long): DeckImagePickerFragment {
            val fragment = DeckImagePickerFragment()
            fragment.arguments = bundle { EXTRA_SESSION_ID to sessionId }
            return fragment
        }
    }
}