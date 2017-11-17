package com.r0adkll.deckbuilder.arch.data


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import com.r0adkll.deckbuilder.arch.data.features.cards.cache.ExpansionCache
import com.r0adkll.deckbuilder.arch.data.features.cards.cache.InMemoryExpansionCache
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.DefaultCardRepository
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.CachingCardDataSource
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.CardDataSource
import com.r0adkll.deckbuilder.arch.data.features.decks.cache.DeckCache
import com.r0adkll.deckbuilder.arch.data.features.decks.cache.FirestoreDeckCache
import com.r0adkll.deckbuilder.arch.data.features.decks.repository.DefaultDeckRepository
import com.r0adkll.deckbuilder.arch.data.features.decks.repository.DefaultDeckValidator
import com.r0adkll.deckbuilder.arch.data.features.decks.repository.DefaultPTCGOConverter
import com.r0adkll.deckbuilder.arch.data.features.decks.rules.DuplicateRule
import com.r0adkll.deckbuilder.arch.data.features.decks.rules.SizeRule
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckValidator
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.PTCGOConverter
import com.r0adkll.deckbuilder.internal.di.AppScope
import com.r0adkll.deckbuilder.util.Schedulers
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import io.pokemontcg.Pokemon
import io.reactivex.android.schedulers.AndroidSchedulers


@Module
class DataModule {

    @Provides @AppScope
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }


    @Provides @AppScope
    fun provideRxSharedPreferences(sharedPreferences: SharedPreferences) : RxSharedPreferences {
        return RxSharedPreferences.create(sharedPreferences)
    }


    @Provides @AppScope
    fun provideSchedulers(): Schedulers = Schedulers(
            AndroidSchedulers.mainThread(),
            io.reactivex.schedulers.Schedulers.io(),
            io.reactivex.schedulers.Schedulers.computation(),
            io.reactivex.schedulers.Schedulers.io()
    )


    @Provides @AppScope
    fun providePokemonApi(): Pokemon = Pokemon()


    @Provides @AppScope
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()


    /*
     * Caching
     */

    @Provides @AppScope
    fun provideDeckCache(cache: FirestoreDeckCache): DeckCache = cache


    /*
     * Data Sources
     */

    @Provides @AppScope
    fun provideCardDataSource(dataSource: CachingCardDataSource): CardDataSource = dataSource


    /*
     * Repositories
     */

    @Provides @AppScope
    fun provideDecksRepository(repository: DefaultDeckRepository): DeckRepository = repository


    @Provides @AppScope
    fun provideCardRepository(repository: DefaultCardRepository): CardRepository = repository


    @Provides @AppScope
    fun provideDeckValidator(validator: DefaultDeckValidator): DeckValidator = validator


    @Provides @AppScope
    fun providePtcgoConverter(converter: DefaultPTCGOConverter): PTCGOConverter = converter

    /*
     * Deck Validation Rules
     */

    @Provides @AppScope @ElementsIntoSet
    fun provideDefaultRuleSet() : Set<DeckValidator.Rule> {
        return setOf(
                SizeRule(),
                DuplicateRule()
        )
    }
}