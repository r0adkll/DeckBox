package com.r0adkll.deckbuilder.arch.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.r0adkll.deckbuilder.arch.ui.features.setup.SetupActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(SetupActivity.createIntent(this))
        finish()
    }
}