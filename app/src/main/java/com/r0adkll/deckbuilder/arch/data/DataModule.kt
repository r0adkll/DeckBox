package com.r0adkll.deckbuilder.arch.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.arch.data.database.DeckDatabase
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.CardDataSource
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.DefaultCardDataSource
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.DiskCardDataSource
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.NetworkCardDataSource
import com.r0adkll.deckbuilder.arch.data.features.expansions.repository.source.DefaultExpansionDataSource
import com.r0adkll.deckbuilder.arch.data.features.exporter.ptcgo.DefaultPtcgoExporter
import com.r0adkll.deckbuilder.arch.data.features.exporter.tournament.DefaultTournamentExporter
import com.r0adkll.deckbuilder.arch.data.features.importer.repository.DefaultImporter
import com.r0adkll.deckbuilder.arch.data.features.marketplace.source.MarketplaceSource
import com.r0adkll.deckbuilder.arch.data.features.marketplace.source.TcgReplayerMarketplaceSource
import com.r0adkll.deckbuilder.arch.data.features.testing.DefaultDeckTester
import com.r0adkll.deckbuilder.arch.data.features.validation.model.BasicRule
import com.r0adkll.deckbuilder.arch.data.features.validation.model.DuplicateRule
import com.r0adkll.deckbuilder.arch.data.features.validation.model.PrismStarRule
import com.r0adkll.deckbuilder.arch.data.features.validation.model.SizeRule
import com.r0adkll.deckbuilder.arch.data.features.validation.repository.DefaultDeckValidator
import com.r0adkll.deckbuilder.arch.data.remote.FirebaseRemote
import com.r0adkll.deckbuilder.arch.data.remote.plugin.CacheInvalidatePlugin
import com.r0adkll.deckbuilder.arch.data.remote.plugin.RemotePlugin
import com.r0adkll.deckbuilder.arch.domain.features.exporter.ptcgo.PtcgoExporter
import com.r0adkll.deckbuilder.arch.domain.features.exporter.tournament.TournamentExporter
import com.r0adkll.deckbuilder.arch.domain.features.importer.repository.Importer
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.arch.domain.features.testing.DeckTester
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Rule
import com.r0adkll.deckbuilder.arch.domain.features.validation.repository.DeckValidator
import com.r0adkll.deckbuilder.internal.di.scopes.AppScope
import com.r0adkll.deckbuilder.util.AppSchedulers
import com.r0adkll.deckbuilder.util.helper.Connectivity
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import dagger.multibindings.IntoSet
import io.pokemontcg.Config
import io.pokemontcg.Pokemon
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import java.util.concurrent.Executors

@Module
class DataModule {

    @Provides @AppScope @IntoSet
    fun provideCacheInvalidatePlugin(
        defaultSource: DefaultExpansionDataSource,
        preferences: AppPreferences,
        schedulers: AppSchedulers
    ): RemotePlugin = CacheInvalidatePlugin(defaultSource, preferences, schedulers)

    @Provides @AppScope
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides @AppScope
    fun provideRxSharedPreferences(sharedPreferences: SharedPreferences): RxSharedPreferences {
        return RxSharedPreferences.create(sharedPreferences)
    }

    @Provides @AppScope
    fun provideRemotePreferences(remoteConfig: FirebaseRemote): Remote = remoteConfig

    @Provides @AppScope
    fun provideSchedulers(): AppSchedulers = AppSchedulers(
        AndroidSchedulers.mainThread(),
        io.reactivex.schedulers.Schedulers.io(),
        io.reactivex.schedulers.Schedulers.computation(),
        io.reactivex.schedulers.Schedulers.io(),
        Executors.newSingleThreadExecutor(),
        Executors.newSingleThreadExecutor()
    )

    @Provides @AppScope
    fun providePokemonApiConfig(): Config {
        val level = if (BuildConfig.DEBUG) HEADERS else NONE
        return Config(logLevel = level)
    }

    @Provides @AppScope
    fun providePokemonApi(config: Config): Pokemon = Pokemon(config)

    /**
     * Changelog
     * ---
     * 1. Initial Version
     * 2. Added collections support
     * 3. Marketplace price cache support
     */
    @Provides @AppScope
    fun provideRoomDatabase(context: Context): DeckDatabase {
        return Room.databaseBuilder(context, DeckDatabase::class.java, BuildConfig.DATABASE_NAME)
            .addMigrations(
                DeckDatabase.MIGRATION_1_2
            )
            .build()
    }

    /*
     * Data Sources
     */

    @Provides @AppScope
    fun provideSearchDataSource(
        disk: DiskCardDataSource,
        network: NetworkCardDataSource,
        remote: Remote,
        preferences: AppPreferences,
        connectivity: Connectivity
    ): CardDataSource {
        return DefaultCardDataSource(preferences, disk, network, connectivity, remote)
    }

    @Provides @AppScope
    fun provideMarketplaceSource(
        source: TcgReplayerMarketplaceSource
    ): MarketplaceSource = source

    /*
     * Deck Validation Rules
     */

    @Provides @AppScope
    @ElementsIntoSet
    fun provideDefaultRuleSet(): Set<Rule> {
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
