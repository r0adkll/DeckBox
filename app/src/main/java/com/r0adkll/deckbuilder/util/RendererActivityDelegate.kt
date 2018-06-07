package com.r0adkll.deckbuilder.util

import android.os.Bundle
import com.ftinc.kit.arch.presentation.delegates.ActivityDelegate
import com.ftinc.kit.arch.presentation.renderers.DisposableStateRenderer


class RendererActivityDelegate(val renderer: DisposableStateRenderer<*>) : ActivityDelegate {

    override fun onCreate(savedInstanceState: Bundle?) {

    }

    override fun onSaveInstanceState(outState: Bundle) {
    }

    override fun onResume() {
    }

    override fun onStart() {
        renderer.start()
    }

    override fun onStop() {
        renderer.stop()
    }

    override fun onPause() {
    }

    override fun onDestroy() {
    }

}