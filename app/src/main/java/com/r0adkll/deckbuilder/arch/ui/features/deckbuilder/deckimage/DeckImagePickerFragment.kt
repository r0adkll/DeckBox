package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.ftinc.kit.arch.di.HasComponent
import com.ftinc.kit.arch.util.uiDebounce
import com.ftinc.kit.util.bindString
import com.ftinc.kit.util.bundle
import com.ftinc.kit.widget.EmptyView
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImageRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.di.DeckImageModule
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderComponent
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_deck_image_picker.*
import javax.inject.Inject
import kotlin.reflect.KClass

class DeckImagePickerFragment : DialogFragment(), DeckImageUi, DeckImageUi.Intentions, DeckImageUi.Actions {

    override var state: DeckImageUi.State = DeckImageUi.State.DEFAULT

    private val deckId: String by bindString(EXTRA_DECK_ID)
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
        state = state.copy(deckId = deckId, selectedDeckImage = selection)

        getComponent(DeckBuilderComponent::class)
            .plus(DeckImageModule(this))
            .inject(this)

        adapter = DeckImageRecyclerAdapter(requireContext()) {
            deckImageClicks.accept(it)
        }

        adapter.emptyView = emptyView
        recycler.adapter = adapter
        recycler.layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_SIZE)
        recycler.itemAnimator = null

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
        get() = actionSelect.clicks()
            .uiDebounce()
            .doOnNext {
                Analytics.event(Event.SelectContent.Deck.EditImage)
            }

    override fun setDeckImages(images: List<DeckImage>) {
        adapter.submitList(images)
    }

    override fun setSelectedDeckImage(image: DeckImage?) {
        adapter.selectedDeckImage = image
        actionSelect.isEnabled = image != null
    }

    override fun close() {
        dismiss()
    }

    override fun showLoading(isLoading: Boolean) {
        emptyView.state = if (isLoading) {
            EmptyView.State.LOADING
        } else {
            EmptyView.State.EMPTY
        }
    }

    override fun showError(description: String) {
        emptyView.message = description
    }

    override fun hideError() {
        emptyView.setMessage(R.string.empty_deck_image_message)
    }

    private fun <C : Any> getComponent(componentType: KClass<C>): C {
        return when {
            parentFragment is HasComponent<*> -> {
                componentType.java.cast((parentFragment as HasComponent<*>).getComponent())!!
            }
            activity is HasComponent<*> -> {
                componentType.java.cast((activity as HasComponent<*>).getComponent())!!
            }
            else -> componentType.java.cast((activity as HasComponent<*>).getComponent())!!
        }
    }

    companion object {
        const val TAG = "DeckImagePicker"
        private const val EXTRA_DECK_ID = "DeckImagePickerFragment.SessionId"
        private const val EXTRA_DECK_IMAGE = "DeckImagePickerFragment.DeckImage"
        private const val GRID_SPAN_SIZE = 3

        fun newInstance(deckId: String, image: DeckImage? = null): DeckImagePickerFragment {
            val fragment = DeckImagePickerFragment()
            fragment.arguments = bundle {
                EXTRA_DECK_ID to deckId
                image?.let { EXTRA_DECK_IMAGE to it.uri.toString() }
            }
            return fragment
        }
    }
}
