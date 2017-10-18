package com.r0adkll.deckbuilder.arch.ui.components


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.components.delegates.ActivityDelegate
import com.r0adkll.deckbuilder.arch.ui.components.delegates.StateSaverActivityDelegate
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.bindOptionalView


abstract class BaseActivity : AppCompatActivity() {

    protected val appbar: Toolbar? by bindOptionalView(R.id.appbar)
    private val delegates: ArrayList<ActivityDelegate> = ArrayList()


    protected abstract fun setupComponent(component: AppComponent)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupComponent(DeckApp.component)

        // Add the auto-state saving delegate by default
        addDelegate(StateSaverActivityDelegate(this))

        delegates.forEach { it.onCreate(savedInstanceState) }
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setSupportActionBar(appbar)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        delegates.forEach { it.onSaveInstanceState(outState) }
    }

    override fun onResume() {
        super.onResume()
        delegates.forEach { it.onResume() }
    }

    override fun onStart() {
        super.onStart()
        delegates.forEach { it.onStart() }
    }

    override fun onPause() {
        super.onPause()
        delegates.forEach { it.onPause() }
    }

    override fun onStop() {
        super.onStop()
        delegates.forEach { it.onStop() }
    }

    override fun onDestroy() {
        super.onDestroy()
        delegates.forEach { it.onDestroy() }
    }


    protected fun addDelegate(delegate: ActivityDelegate) = delegates.add(delegate)
    protected fun removeDelegate(delegate: ActivityDelegate) = delegates.remove(delegate)
}