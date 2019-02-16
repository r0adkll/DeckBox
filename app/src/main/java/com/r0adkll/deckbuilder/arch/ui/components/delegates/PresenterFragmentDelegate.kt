package com.r0adkll.deckbuilder.arch.ui.components.delegates

import android.os.Bundle
import com.ftinc.kit.arch.presentation.delegates.ActivityDelegate
import com.ftinc.kit.arch.presentation.presenter.Presenter


class PresenterFragmentDelegate(val presenter: Presenter) : FragmentDelegate {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
    }

    override fun onSaveInstanceState(outState: Bundle) {
    }

    override fun onResume() {
    }

    override fun onStart() {
        presenter.start()
    }

    override fun onStop() {
        presenter.stop()
    }

    override fun onPause() {
    }

    override fun onDestroy() {
    }

}