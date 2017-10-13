package com.r0adkll.deckbuilder.arch.ui.features.setup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.internal.di.AppComponent


class SetupActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)
    }


    override fun setupComponent(component: AppComponent) {

    }


    companion object {

        fun createIntent(context: Context): Intent = Intent(context, SetupActivity::class.java)
    }
}