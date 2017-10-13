package com.r0adkll.deckbuilder.arch.ui.features.decks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.internal.di.AppComponent


class DecksActivity : BaseActivity() {

    companion object {

        fun createIntent(context: Context): Intent = Intent(context, DecksActivity::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decks)
    }


    override fun setupComponent(component: AppComponent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
