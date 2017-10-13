package com.r0adkll.deckbuilder.arch.ui.components


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.bindOptionalView


abstract class BaseActivity : AppCompatActivity() {

    protected val appbar: Toolbar? by bindOptionalView(R.id.appbar)


    protected abstract fun setupComponent(component: AppComponent)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupComponent(DeckApp.component)

    }


    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setSupportActionBar(appbar)
    }
}