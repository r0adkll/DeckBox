package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderModule
import com.r0adkll.deckbuilder.internal.di.AppComponent


class DeckBuilderActivity : BaseActivity() {

    companion object {

        fun createIntent(context: Context): Intent = Intent(context, DeckBuilderActivity::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_builder)

    }


    override fun setupComponent(component: AppComponent) {
        component.plus(DeckBuilderModule(this))
                .inject(this)
    }



}