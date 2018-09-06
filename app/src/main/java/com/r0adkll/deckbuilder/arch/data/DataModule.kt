package com.r0adkll.deckbuilder.arch.data


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.arch.data.features.cards.DefaultCacheManager
import com.r0adkll.deckbuilder.arch.data.features.cards.cache.CardCache
import com.r0adkll.deckbuilder.arch.data.features.cards.cache.RequeryCardCache
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.DefaultCardRepository
import com.r0adkll.deckbuilder.arch.data.features.expansions.CachingExpansionDataSource
import com.r0adkll.deckbuilder.arch.data.features.expansions.ExpansionDataSource
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.CombinedSearchDataSource
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.SearchDataSource
import com.r0adkll.deckbuilder.arch.data.features.decks.cache.DeckCache
import com.r0adkll.deckbuilder.arch.data.features.decks.cache.FirestoreDeckCache
import com.r0adkll.deckbuilder.arch.data.features.decks.repository.DefaultDeckRepository
import com.r0adkll.deckbuilder.arch.data.features.editing.cache.RequerySessionCache
import com.r0adkll.deckbuilder.arch.data.features.editing.cache.SessionCache
import com.r0adkll.deckbuilder.arch.data.features.editing.repository.DefaultEditRepository
import com.r0adkll.deckbuilder.arch.data.features.missingcard.repository.DefaultMissingCardRepository
import com.r0adkll.deckbuilder.arch.data.features.ptcgo.repository.DefaultPTCGOConverter
import com.r0adkll.deckbuilder.arch.data.features.testing.DefaultDeckTester
import com.r0adkll.deckbuilder.arch.data.features.tournament.exporter.DefaultTournamentExporter
import com.r0adkll.deckbuilder.arch.data.features.validation.model.BasicRule
import com.r0adkll.deckbuilder.arch.data.features.validation.model.DuplicateRule
import com.r0adkll.deckbuilder.arch.data.features.validation.model.PrismStarRule
import com.r0adkll.deckbuilder.arch.data.features.validation.model.SizeRule
import com.r0adkll.deckbuilder.arch.data.features.validation.repository.DefaultDeckValidator
import com.r0adkll.deckbuilder.arch.domain.features.cards.CacheManager
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.missingcard.repository.MissingCardRepository
import com.r0adkll.deckbuilder.arch.domain.features.ptcgo.repository.PTCGOConverter
import com.r0adkll.deckbuilder.arch.domain.features.testing.DeckTester
import com.r0adkll.deckbuilder.arch.domain.features.tournament.exporter.TournamentExporter
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Rule
import com.r0adkll.deckbuilder.arch.domain.features.validation.repository.DeckValidator
import com.r0adkll.deckbuilder.internal.di.scopes.AppScope
import com.r0adkll.deckbuilder.util.Schedulers
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import io.pokemontcg.Config
import io.pokemontcg.Pokemon
import io.reactivex.android.schedulers.AndroidSchedulers
import io.requery.Persistable
import io.requery.android.sqlite.DatabaseSource
import io.requery.reactivex.KotlinReactiveEntityStore
import io.requery.sql.KotlinEntityDataStore
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import java.util.concurrent.Executors


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
            io.reactivex.schedulers.Schedulers.io(),
            Executors.newSingleThreadExecutor(),
            Executors.newSingleThreadExecutor()
    )


    @Provides @AppScope
    fun providePokemonApiConfig(): Config {
        val level = if (BuildConfig.DEBUG) BODY else NONE
        return Config(logLevel = level)
    }


    @Provides @AppScope
    fun providePokemonApi(config: Config): Pokemon = Pokemon(config)


    /**
     * - 1: Initial Version
     * - 2: Added deck image to SessionEntity;
     */
    @Provides @AppScope
    fun provideDatabase(context: Context): KotlinReactiveEntityStore<Persistable> {
        val source = DatabaseSource(context, Models.DEFAULT, BuildConfig.DATABASE_NAME, 2)
        val entityStore = KotlinEntityDataStore<Persistable>(source.configuration)
        return KotlinReactiveEntityStore(entityStore)
    }


    /*
     * Caching
     */

    @Provides @AppScope
    fun provideCardCache(cache: RequeryCardCache): CardCache = cache


    @Provides @AppScope
    fun provideDeckCache(cache: FirestoreDeckCache): DeckCache = cache


    @Provides @AppScope
    fun provideSessionCache(cache: RequerySessionCache): SessionCache = cache


    @Provides @AppScope
    fun provideCacheManager(manager: DefaultCacheManager): CacheManager = manager


    /*
     * Data Sources
     */

    @Provides @AppScope
    fun provideExpansionDataSource(dataSource: CachingExpansionDataSource): ExpansionDataSource = dataSource


    @Provides @AppScope
    fun provideSearchDataSource(dataSource: CombinedSearchDataSource): SearchDataSource = dataSource


    /*
     * Repositories
     */

    @Provides @AppScope
    fun provideDecksRepository(repository: DefaultDeckRepository): DeckRepository = repository


    @Provides @AppScope
    fun provideEditRepository(repository: DefaultEditRepository): EditRepository = repository


    @Provides @AppScope
    fun provideCardRepository(repository: DefaultCardRepository): CardRepository = repository


    @Provides @AppScope
    fun provideMissingCardRepository(repository: DefaultMissingCardRepository): MissingCardRepository = repository


    @Provides @AppScope
    fun provideDeckValidator(validator: DefaultDeckValidator): DeckValidator = validator


    @Provides @AppScope
    fun providePtcgoConverter(converter: DefaultPTCGOConverter): PTCGOConverter = converter


    @Provides @AppScope
    fun provideTournamentExporter(exporter: DefaultTournamentExporter): TournamentExporter = exporter

    /*
     * Deck Validation Rules
     */

    @Provides @AppScope
    @ElementsIntoSet
    fun provideDefaultRuleSet() : Set<Rule> {
        return setOf(
                SizeRule(),
                DuplicateRule(),
                BasicRule(),
                PrismStarRule()
        )
    }


    @Provides @AppScope
    fun provideDeckTester(tester: DefaultDeckTester): DeckTester = tester

}