package com.r0adkll.deckbuilder.arch.data


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.DefaultCardRepository
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.CachingCardDataSource
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.CardDataSource
import com.r0adkll.deckbuilder.arch.data.features.decks.cache.DeckCache
import com.r0adkll.deckbuilder.arch.data.features.decks.cache.FirestoreDeckCache
import com.r0adkll.deckbuilder.arch.data.features.decks.repository.DefaultDeckRepository
import com.r0adkll.deckbuilder.arch.data.features.validation.repository.DefaultDeckValidator
import com.r0adkll.deckbuilder.arch.data.features.ptcgo.repository.DefaultPTCGOConverter
import com.r0adkll.deckbuilder.arch.data.features.validation.model.BasicRule
import com.r0adkll.deckbuilder.arch.data.features.validation.model.DuplicateRule
import com.r0adkll.deckbuilder.arch.data.features.validation.model.SizeRule
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.validation.repository.DeckValidator
import com.r0adkll.deckbuilder.arch.domain.features.ptcgo.repository.PTCGOConverter
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Rule
import com.r0adkll.deckbuilder.internal.di.scopes.AppScope
import com.r0adkll.deckbuilder.util.Schedulers
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import io.pokemontcg.Config
import io.pokemontcg.Pokemon
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.logging.HttpLoggingInterceptor.Level.*


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
    fun providePokemonApiConfig(): Config {
        val level = if (BuildConfig.DEBUG) HEADERS else NONE
        return Config(logLevel = level)
    }


    @Provides @AppScope
    fun providePokemonApi(config: Config): Pokemon = Pokemon(config)


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

    @Provides @AppScope
    @ElementsIntoSet
    fun provideDefaultRuleSet() : Set<Rule> {
        return setOf(
                SizeRule(),
                DuplicateRule(),
                BasicRule()
        )
    }

}