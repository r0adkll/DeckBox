package com.r0adkll.deckbuilder.arch.ui.features.settings.cache

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ftinc.kit.arch.presentation.BaseActivity
import com.r0adkll.deckbuilder.R

class ManageCacheActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_offline)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appbar?.setNavigationOnClickListener { finish() }
    }

    override fun setupComponent() {
    }

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, ManageCacheActivity::class.java)
        }
    }
}
