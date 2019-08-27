package com.r0adkll.deckbuilder.arch.ui.features.playtest.actions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.util.extensions.coerceScaleFlip
import kotlinx.android.synthetic.main.fragment_playtest_actionsheet_coinflip.*
import kotlin.random.Random

class CoinFlipBottomSheetFragment : BottomSheetDialogFragment() {

    private var headResults = 0
        set(value) {
            field = value
            resultHeads.text = "$value"
        }

    private var tailResults = 0
        set(value) {
            field = value
            resultTails.text = "$value"
        }

    private var flipAnimator: ObjectAnimator? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_playtest_actionsheet_coinflip, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resultHeads.text = "$headResults"
        resultTails.text = "$tailResults"

        coin.setOnClickListener {
            flipCoin()
        }

        actionPositive.setOnClickListener {
            flipCoin()
        }

        actionNegative.setOnClickListener {
            dismiss()
        }
    }

    private fun flipCoin() {
        val isHeads = Random.nextBoolean()
        val count = if (isHeads) 2 else 3
        val start = coin.scaleX.coerceScaleFlip() * -1f
        val end = coin.scaleX.coerceScaleFlip()
        flipAnimator?.cancel()
        flipAnimator = ObjectAnimator.ofFloat(coin, "scaleX", start, end).apply {
            duration = 150L
            interpolator = LinearInterpolator()
            repeatMode = ValueAnimator.REVERSE
            repeatCount = count
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator?) {
                    if (isHeads) {
                        headResults++
                    } else {
                        tailResults++
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {
                    if (isHeads) {
                        headResults++
                    } else {
                        tailResults++
                    }
                }
            })
            start()
        }
    }

    companion object {
        const val TAG = "CoinFlipBottomSheetFragment"

        fun show(fragmentManager: FragmentManager) {
            val fragment = CoinFlipBottomSheetFragment()
            fragment.show(fragmentManager, TAG)
        }
    }
}
