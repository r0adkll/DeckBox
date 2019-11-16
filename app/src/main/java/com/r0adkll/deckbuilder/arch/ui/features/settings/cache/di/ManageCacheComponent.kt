package com.r0adkll.deckbuilder.arch.ui.features.settings.cache.di

import com.r0adkll.deckbuilder.arch.ui.features.settings.cache.ManageCacheActivity
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ManageCacheModule::class])
interface ManageCacheComponent {

    fun inject(activity: ManageCacheActivity)
}
