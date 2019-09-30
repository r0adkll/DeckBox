package com.r0adkll.deckbuilder.arch.data


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.arch.data.database.DeckDatabase
import com.r0adkll.deckbuilder.arch.data.features.account.DefaultAccountRepository
import com.r0adkll.deckbuilder.arch.data.features.cards.cache.CardCache
import com.r0adkll.deckbuilder.arch.data.features.cards.cache.RoomCardCache
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.DefaultCardRepository
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.CachingNetworkSearchDataSource
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.CombinedSearchDataSource
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.DiskSearchDataSource
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.SearchDataSource
import com.r0adkll.deckbuilder.arch.data.features.collection.cache.FirestoreCollectionCache
import com.r0adkll.deckbuilder.arch.data.features.collection.cache.RoomCollectionCache
import com.r0adkll.deckbuilder.arch.data.features.collection.repository.DefaultCollectionRepository
import com.r0adkll.deckbuilder.arch.data.features.community.cache.CommunityCache
import com.r0adkll.deckbuilder.arch.data.features.community.cache.FirestoreCommunityCache
import com.r0adkll.deckbuilder.arch.data.features.community.repository.DefaultCommunityRepository
import com.r0adkll.deckbuilder.arch.data.features.decks.cache.DeckCache
import com.r0adkll.deckbuilder.arch.data.features.decks.cache.SwitchingDeckCache
import com.r0adkll.deckbuilder.arch.data.features.decks.repository.DefaultDeckRepository
import com.r0adkll.deckbuilder.arch.data.features.editing.cache.RoomSessionCache
import com.r0adkll.deckbuilder.arch.data.features.editing.cache.SessionCache
import com.r0adkll.deckbuilder.arch.data.features.editing.repository.DefaultEditRepository
import com.r0adkll.deckbuilder.arch.data.features.expansions.CachingExpansionDataSource
import com.r0adkll.deckbuilder.arch.data.features.expansions.ExpansionDataSource
import com.r0adkll.deckbuilder.arch.data.features.exporter.ptcgo.DefaultPtcgoExporter
import com.r0adkll.deckbuilder.arch.data.features.exporter.tournament.DefaultTournamentExporter
import com.r0adkll.deckbuilder.arch.data.features.importer.repository.DefaultImporter
import com.r0adkll.deckbuilder.arch.data.features.marketplace.MockMarketplaceRepository
import com.r0adkll.deckbuilder.arch.data.features.missingcard.repository.DefaultMissingCardRepository
import com.r0adkll.deckbuilder.arch.data.features.offline.repository.DefaultOfflineRepository
import com.r0adkll.deckbuilder.arch.data.features.offline.repository.OfflineStatusConsumer
import com.r0adkll.deckbuilder.arch.data.features.preview.RemotePreviewRepository
import com.r0adkll.deckbuilder.arch.data.features.preview.TestPreviewRepository
import com.r0adkll.deckbuilder.arch.data.features.testing.DefaultDeckTester
import com.r0adkll.deckbuilder.arch.data.features.validation.model.BasicRule
import com.r0adkll.deckbuilder.arch.data.features.validation.model.DuplicateRule
import com.r0adkll.deckbuilder.arch.data.features.validation.model.PrismStarRule
import com.r0adkll.deckbuilder.arch.data.features.validation.model.SizeRule
import com.r0adkll.deckbuilder.arch.data.features.validation.repository.DefaultDeckValidator
import com.r0adkll.deckbuilder.arch.data.remote.FirebaseRemote
import com.r0adkll.deckbuilder.arch.data.remote.plugin.CacheInvalidatePlugin
import com.r0adkll.deckbuilder.arch.data.remote.plugin.RemotePlugin
import com.r0adkll.deckbuilder.arch.domain.features.account.AccountRepository
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.collection.repository.CollectionRepository
import com.r0adkll.deckbuilder.arch.domain.features.community.repository.CommunityRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.exporter.ptcgo.PtcgoExporter
import com.r0adkll.deckbuilder.arch.domain.features.exporter.tournament.TournamentExporter
import com.r0adkll.deckbuilder.arch.domain.features.importer.repository.Importer
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.repository.MarketplaceRepository
import com.r0adkll.deckbuilder.arch.domain.features.missingcard.repository.MissingCardRepository
import com.r0adkll.deckbuilder.arch.domain.features.offline.repository.OfflineRepository
import com.r0adkll.deckbuilder.arch.domain.features.preview.PreviewRepository
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.arch.domain.features.testing.DeckTester
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Rule
import com.r0adkll.deckbuilder.arch.domain.features.validation.repository.DeckValidator
import com.r0adkll.deckbuilder.internal.di.scopes.AppScope
import com.r0adkll.deckbuilder.util.Schedulers
import com.r0adkll.deckbuilder.util.helper.Connectivity
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import dagger.multibindings.IntoSet
import io.pokemontcg.Config
import io.pokemontcg.Pokemon
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import java.util.concurrent.Executors


@Module
class DataModule {

    @Provides @AppScope @IntoSet
    fun provideCacheInvalidatePlugin(plugin: CacheInvalidatePlugin): RemotePlugin = plugin


    @Provides @AppScope
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }


    @Provides @AppScope
    fun provideRxSharedPreferences(sharedPreferences: SharedPreferences) : RxSharedPreferences {
        return RxSharedPreferences.create(sharedPreferences)
    }


    @Provides @AppScope
    fun provideRemotePreferences(remoteConfig: FirebaseRemote): Remote = remoteConfig


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
     * Change Log
     * ---
     * 1. Initial Version
     */
    @Provides @AppScope
    fun provideRoomDatabase(context: Context): DeckDatabase {
        return Room.databaseBuilder(context, DeckDatabase::class.java, BuildConfig.DATABASE_NAME)
                .addMigrations(DeckDatabase.MIGRATION_1_2)
                .build()
    }


    /*
     * Caching
     */

    @Provides @AppScope
    fun provideCardCache(cache: RoomCardCache): CardCache = cache


    @Provides @AppScope
    fun provideDeckCache(cache: SwitchingDeckCache): DeckCache = cache


    @Provides @AppScope
    fun provideCommunityCache(cache: FirestoreCommunityCache): CommunityCache = cache


    @Provides @AppScope
    fun provideSessionCache(cache: RoomSessionCache): SessionCache = cache


    @Provides @AppScope
    fun provideOfflineStatusConsumer(consumer: DefaultOfflineRepository): OfflineStatusConsumer = consumer


    /*
     * Data Sources
     */

    @Provides @AppScope
    fun provideExpansionDataSource(dataSource: CachingExpansionDataSource): ExpansionDataSource = dataSource


    @Provides @AppScope
    fun provideSearchDataSource(
            api: Pokemon,
            cache: CardCache,
            source: ExpansionDataSource,
            remote: Remote,
            schedulers: Schedulers,
            preferences: AppPreferences,
            connectivity: Connectivity
    ): SearchDataSource {
        val disk: SearchDataSource = DiskSearchDataSource(cache, schedulers)
        val network: SearchDataSource = CachingNetworkSearchDataSource(api, source, cache, remote, schedulers)
        return CombinedSearchDataSource(preferences, disk, network, connectivity)
    }


    /*
     * Repositories
     */

    @Provides @AppScope
    fun provideAccountRepository(repository: DefaultAccountRepository): AccountRepository = repository


    @Provides @AppScope
    fun provideDecksRepository(repository: DefaultDeckRepository): DeckRepository = repository


    @Provides @AppScope
    fun provideCommunityRepository(repository: DefaultCommunityRepository): CommunityRepository = repository


    @Provides @AppScope
    fun provideEditRepository(repository: DefaultEditRepository): EditRepository = repository


    @Provides @AppScope
    fun provideCardRepository(repository: DefaultCardRepository): CardRepository = repository


    @Provides @AppScope
    fun provideMissingCardRepository(repository: DefaultMissingCardRepository): MissingCardRepository = repository


    @Provides @AppScope
    fun provideOfflineRepository(repository: DefaultOfflineRepository): OfflineRepository = repository


    @Provides @AppScope
    fun providePreviewRepository(repository: RemotePreviewRepository): PreviewRepository {
//        if (BuildConfig.DEBUG) {
//            return TestPreviewRepository()
//        } else {
            return repository
//        }
    }

    @Provides @AppScope
    fun provideMarketplaceRepository(): MarketplaceRepository {
        return MockMarketplaceRepository()
    }

    @Provides @AppScope
    fun provideCollectionRepository(
            roomCollectionCache: RoomCollectionCache,
            firestoreCollectionCache: FirestoreCollectionCache,
            preferences: AppPreferences
    ): CollectionRepository {
        return DefaultCollectionRepository(roomCollectionCache, firestoreCollectionCache, preferences)
    }


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


    @Provides @AppScope
    fun provideDeckValidator(validator: DefaultDeckValidator): DeckValidator = validator


    @Provides @AppScope
    fun provideImporter(importer: DefaultImporter): Importer = importer


    @Provides @AppScope
    fun providePtcgoExporter(): PtcgoExporter = DefaultPtcgoExporter()


    @Provides @AppScope
    fun provideTournamentExporter(exporter: DefaultTournamentExporter): TournamentExporter = exporter

}
