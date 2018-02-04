package com.r0adkll.deckbuilder.arch.ui.features.browse.di


import com.r0adkll.deckbuilder.arch.ui.features.browse.SetBrowserActivity
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = [(SetBrowserModule::class)])
interface SetBrowserComponent {

    fun inject(activity: SetBrowserActivity)
}