package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage


import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImageRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.di.DeckImageModule
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderComponent
import com.r0adkll.deckbuilder.util.bindLong
import com.r0adkll.deckbuilder.util.bindString
import com.r0adkll.deckbuilder.util.bundle
import com.r0adkll.deckbuilder.util.extensions.uiDebounce
import gov.scstatehouse.houseofcards.di.HasComponent
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_deck_image_picker.*
import javax.inject.Inject
import kotlin.reflect.KClass


class DeckImagePickerFragment : DialogFragment(), DeckImageUi, DeckImageUi.Intentions, DeckImageUi.Actions {

    override var state: DeckImageUi.State = DeckImageUi.State.DEFAULT

    private val sessionId: Long by bindLong(EXTRA_SESSION_ID)
    private val deckImage: String? by bindString(EXTRA_DECK_IMAGE)

    private val deckImageClicks: Relay<DeckImage> = PublishRelay.create()
    private lateinit var adapter: DeckImageRecyclerAdapter

    @Inject lateinit var renderer: DeckImageRenderer
    @Inject lateinit var presenter: DeckImagePresenter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deck_image_picker, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val selection = deckImage?.let { DeckImage.from(Uri.parse(it)) }
        state = state.copy(sessionId = sessionId, selectedDeckImage = selection)

        getComponent(DeckBuilderComponent::class)
                .plus(DeckImageModule(this))
                .inject(this)

        adapter = DeckImageRecyclerAdapter(activity!!)
        adapter.setEmptyView(emptyView)
        recycler.adapter = adapter
        recycler.layoutManager = GridLayoutManager(activity!!, 3)
        recycler.itemAnimator = null

        adapter.setOnItemClickListener {
            deckImageClicks.accept(it)
        }

        renderer.start()
        presenter.start()

        actionCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroy() {
        presenter.stop()
        renderer.stop()
        super.onDestroy()
    }


    override fun render(state: DeckImageUi.State) {
        this.state = state
        renderer.render(state)
    }


    override val pickedDeckImage: Observable<DeckImage>
        get() = deckImageClicks


    override val selectDeckImageClicks: Observable<Unit>
        get() = actionSelect.clicks().uiDebounce()


    override fun setDeckImages(images: List<DeckImage>) {
        adapter.setDeckImages(images)
    }


    override fun setSelectedDeckImage(image: DeckImage?) {
        adapter.selectedDeckImage = image
        actionSelect.isEnabled = image != null
    }


    override fun close() {
        dismiss()
    }


    override fun showLoading(isLoading: Boolean) {
        emptyView.setLoading(isLoading)
    }


    override fun showError(description: String) {
        emptyView.emptyMessage = description
    }


    override fun hideError() {
        emptyView.setEmptyMessage(R.string.empty_deck_image_message)
    }


    private fun <C : Any> getComponent(componentType: KClass<C>): C {
        if (parentFragment is HasComponent<*>) {
            return componentType.java.cast((parentFragment as HasComponent<*>).getComponent())
        } else if (activity is HasComponent<*>) {
            return componentType.java.cast((activity as HasComponent<*>).getComponent())!!
        }
        return componentType.java.cast((activity as HasComponent<*>).getComponent())!!
    }


    companion object {
        const val TAG = "DeckImagePicker"
        private const val EXTRA_SESSION_ID = "DeckImagePickerFragment.SessionId"
        private const val EXTRA_DECK_IMAGE = "DeckImagePickerFragment.DeckImage"


        fun newInstance(sessionId: Long, image: DeckImage? = null): DeckImagePickerFragment {
            val fragment = DeckImagePickerFragment()
            fragment.arguments = bundle {
                EXTRA_SESSION_ID to sessionId
                image?.let { EXTRA_DECK_IMAGE to it.uri.toString() }
            }
            return fragment
        }
    }
}