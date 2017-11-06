package com.r0adkll.deckbuilder.internal.di


import com.r0adkll.deckbuilder.BuildModule
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.arch.data.DataModule
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.di.CardDetailComponent
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.di.CardDetailModule
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderComponent
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderModule
import com.r0adkll.deckbuilder.arch.ui.features.exporter.DeckExportActivity
import com.r0adkll.deckbuilder.arch.ui.features.home.di.HomeComponent
import com.r0adkll.deckbuilder.arch.ui.features.home.di.HomeModule
import com.r0adkll.deckbuilder.arch.ui.features.importer.di.DeckImportComponent
import com.r0adkll.deckbuilder.arch.ui.features.importer.di.DeckImportModule
import com.r0adkll.deckbuilder.arch.ui.features.search.di.SearchComponent
import com.r0adkll.deckbuilder.arch.ui.features.search.di.SearchModule
import dagger.Component


@AppScope
@Component(modules = arrayOf(
        AppModule::class,
        BuildModule::class,
        DataModule::class
))
interface AppComponent {

    fun inject(app: DeckApp)
    fun inject(activity: DeckExportActivity)

    fun plus(module: HomeModule): HomeComponent
    fun plus(module: DeckBuilderModule): DeckBuilderComponent
    fun plus(module: SearchModule): SearchComponent
    fun plus(module: CardDetailModule): CardDetailComponent
    fun plus(module: DeckImportModule): DeckImportComponent
}