package com.r0adkll.deckbuilder.arch.ui.components.delegates

import android.app.Activity
import android.os.Bundle
import com.evernote.android.state.StateSaver


class StateSaverActivityDelegate(private val activity: Activity) : ActivityDelegate {

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