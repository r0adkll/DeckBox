package com.r0adkll.deckbuilder.arch.ui.features.settings.offline

import android.os.Bundle
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.internal.di.AppComponent


class ManageOfflineActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_offline)
    }


    override fun setupComponent(component: AppComponent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}