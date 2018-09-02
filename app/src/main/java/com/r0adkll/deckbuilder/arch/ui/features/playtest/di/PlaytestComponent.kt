package com.r0adkll.deckbuilder.arch.ui.features.playtest.di


import com.r0adkll.deckbuilder.arch.ui.features.playtest.PlaytestActivity
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = [PlaytestModule::class])
interface PlaytestComponent {

    fun inject(activity: PlaytestActivity)
}