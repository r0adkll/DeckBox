package com.r0adkll.deckbuilder.internal.di

import com.r0adkll.deckbuilder.BuildModule
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.arch.data.DataModule
import com.r0adkll.deckbuilder.arch.data.features.offline.service.CacheService
import com.r0adkll.deckbuilder.arch.ui.RouteActivity
import com.r0adkll.deckbuilder.arch.ui.ShortcutActivity
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.di.CardDetailComponent
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.di.CardDetailModule
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.di.CollectionSetComponent
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.di.CollectionSetModule
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.di.DeckImageComponent
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.di.DeckImageModule
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderComponent
import com.r0adkll.deckbuilder.arch.ui.features.exporter.DeckExportActivity
import com.r0adkll.deckbuilder.arch.ui.features.exporter.di.MultiExportComponent
import com.r0adkll.deckbuilder.arch.ui.features.exporter.di.MultiExportModule
import com.r0adkll.deckbuilder.arch.ui.features.home.di.HomeComponent
import com.r0adkll.deckbuilder.arch.ui.features.home.di.HomeModule
import com.r0adkll.deckbuilder.arch.ui.features.importer.di.DeckImportComponent
import com.r0adkll.deckbuilder.arch.ui.features.importer.di.DeckImportModule
import com.r0adkll.deckbuilder.arch.ui.features.onboarding.OnboardingActivity
import com.r0adkll.deckbuilder.arch.ui.features.search.di.SearchComponent
import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.di.SetBrowserComponent
import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.di.SetBrowserModule
import com.r0adkll.deckbuilder.arch.ui.features.settings.SettingsActivity
import com.r0adkll.deckbuilder.arch.ui.features.setup.SetupActivity
import com.r0adkll.deckbuilder.arch.ui.features.testing.di.DeckTestingComponent
import com.r0adkll.deckbuilder.arch.ui.features.testing.di.DeckTestingModule
import com.r0adkll.deckbuilder.internal.di.scopes.AppScope
import dagger.Component

@AppScope
@Component(modules = [AppModule::class, BuildModule::class, DataModule::class])
interface AppComponent {

    fun inject(app: DeckApp)
    fun inject(activity: SetupActivity)
    fun inject(activity: DeckExportActivity)
    fun inject(activity: RouteActivity)
    fun inject(activity: ShortcutActivity)
    fun inject(activity: OnboardingActivity)
    fun inject(fragment: SettingsActivity.SettingsFragment)
    fun inject(service: CacheService)

    fun plus(module: HomeModule): HomeComponent
    fun plus(module: CardDetailModule): CardDetailComponent
    fun plus(module: DeckImportModule): DeckImportComponent
    fun plus(module: MultiExportModule): MultiExportComponent
    fun plus(module: SetBrowserModule): SetBrowserComponent
    fun plus(module: DeckImageModule): DeckImageComponent
    fun plus(module: DeckTestingModule): DeckTestingComponent
    fun plus(module: CollectionSetModule): CollectionSetComponent

    fun searchComponentBuilder(): SearchComponent.Builder
    fun deckBuilderComponentBuilder(): DeckBuilderComponent.Builder
}
