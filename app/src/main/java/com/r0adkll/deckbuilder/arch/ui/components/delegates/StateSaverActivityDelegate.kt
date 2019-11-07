package com.r0adkll.deckbuilder.arch.ui.components.delegates

import android.os.Bundle
import com.evernote.android.state.StateSaver
import com.ftinc.kit.arch.presentation.BaseActivity
import com.ftinc.kit.arch.presentation.delegates.ActivityDelegate

class StateSaverActivityDelegate<A : BaseActivity>(private val activity: A) : ActivityDelegate {

    override fun onCreate(savedInstanceState: Bundle?) {
        StateSaver.restoreInstanceState(activity, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        StateSaver.saveInstanceState(activity, outState)
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
