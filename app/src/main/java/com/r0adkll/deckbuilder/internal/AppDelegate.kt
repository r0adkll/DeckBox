package com.r0adkll.deckbuilder.internal

import android.app.Application

/**
 * A delegate to provide configuration application setups based on flavor or build type that
 * can be injected via Dagger
 */
interface AppDelegate {

    fun onCreate(app: Application)
}

/**
 * No-Op delegate to provide when needed
 */
class NoOpAppDelegate : AppDelegate {
    override fun onCreate(app: Application) {}
}
