package com.r0adkll.deckbuilder.arch.ui.components.delegates

import android.os.Bundle
import com.evernote.android.state.StateSaver
import com.ftinc.kit.arch.presentation.BaseFragment
import com.ftinc.kit.arch.presentation.delegates.FragmentDelegate

class StateSaverFragmentDelegate<F : BaseFragment>(private val fragment: F) : FragmentDelegate {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        StateSaver.restoreInstanceState(fragment, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        StateSaver.saveInstanceState(fragment, outState)
    }

    override fun onResume() {
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onPause() {
    }

    override fun onDestroy() {
    }
}
