package com.r0adkll.deckbuilder.arch.ui.features.carddetail.di

import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailActivity
import com.r0adkll.deckbuilder.internal.di.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(CardDetailModule::class))
interface CardDetailComponent {

    fun inject(activity: CardDetailActivity)
}