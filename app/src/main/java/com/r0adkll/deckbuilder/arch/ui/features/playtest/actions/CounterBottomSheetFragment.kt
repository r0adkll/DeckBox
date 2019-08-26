package com.r0adkll.deckbuilder.arch.ui.features.playtest.actions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.ftinc.kit.kotlin.utils.bindInt
import com.ftinc.kit.kotlin.utils.bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.r0adkll.deckbuilder.R
import kotlinx.android.synthetic.main.fragment_playtest_actionsheet_counter.*

class CounterBottomSheetFragment : BottomSheetDialogFragment() {

    interface CounterListener {

        fun onCountAccepted(count: Int)
    }

    private val titleResId by bindInt(KEY_TITLE)
    private val positiveActionText by bindInt(KEY_POSITIVE_ACTION)
    private val negativeActionText by bindInt(KEY_NEGATIVE_ACTION)

    var counterListener: CounterListener? = null

    private var count: Int = 0
        set(value) {
            field = value
            counter.text = "$value"
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_playtest_actionsheet_counter, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        count = savedInstanceState?.getInt(KEY_COUNT) ?: 0
        title.setText(titleResId)
        actionPositive.setText(positiveActionText)
        actionNegative.setText(negativeActionText)

        actionAdd.setOnClickListener {
            count += CHANGE_AMOUNT
        }

        actionSubtract.setOnClickListener {
            count -= CHANGE_AMOUNT
        }

        actionPositive.setOnClickListener {
            counterListener?.onCountAccepted(count)
            dismiss()
        }

        actionNegative.setOnClickListener {
            dismiss()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_COUNT, count)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CounterListener) {
            counterListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        counterListener = null
    }

    companion object {
        const val TAG = "CounterBottomSheetFragment"
        private const val CHANGE_AMOUNT = 10
        private const val KEY_TITLE = "CounterBottomSheetFragment.Title"
        private const val KEY_POSITIVE_ACTION = "CounterBottomSheetFragment.PositiveAction"
        private const val KEY_NEGATIVE_ACTION = "CounterBottomSheetFragment.NegativeAction"
        private const val KEY_COUNT = "CounterBottomSheetFragment.Count"

        fun show(
                fragmentManager: FragmentManager,
                @StringRes titleResId: Int,
                @StringRes positiveActionText: Int,
                @StringRes negativeActionText: Int
        ) {
            val fragment = CounterBottomSheetFragment()
            fragment.arguments = bundle {
                KEY_TITLE to titleResId
                KEY_POSITIVE_ACTION to positiveActionText
                KEY_NEGATIVE_ACTION to negativeActionText
            }
            fragment.show(fragmentManager, TAG)
        }
    }
}
