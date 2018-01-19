package com.r0adkll.deckbuilder.arch.ui.features.missingcards.di


import com.r0adkll.deckbuilder.arch.ui.features.missingcards.MissingCardsActivity
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = [(MissingCardsModule::class)])
interface MissingCardsComponent {

    fun inject(activity: MissingCardsActivity)
}