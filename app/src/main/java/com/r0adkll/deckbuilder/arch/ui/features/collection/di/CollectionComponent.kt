package com.r0adkll.deckbuilder.arch.ui.features.collection.di

import com.r0adkll.deckbuilder.arch.ui.features.collection.CollectionFragment
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import dagger.Subcomponent


@FragmentScope
@Subcomponent(modules = [CollectionModule::class])
interface CollectionComponent {

    fun inject(fragment: CollectionFragment)
}