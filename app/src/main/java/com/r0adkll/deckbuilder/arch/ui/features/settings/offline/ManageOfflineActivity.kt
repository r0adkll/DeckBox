package com.r0adkll.deckbuilder.arch.ui.features.settings.offline

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.internal.di.AppComponent


class ManageOfflineActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_offline)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appbar?.setNavigationOnClickListener { finish() }
    }


    override fun setupComponent(component: AppComponent) {

    }


    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, ManageOfflineActivity::class.java)
        }
    }
}