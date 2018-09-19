package com.r0adkll.deckbuilder.arch.ui.features.carddetail.di

import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailActivity
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [CardDetailModule::class])
interface CardDetailComponent {

    fun inject(activity: CardDetailActivity)
}