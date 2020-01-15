package com.r0adkll.deckbuilder.arch.data

import com.r0adkll.deckbuilder.arch.data.database.DeckDatabase
import com.r0adkll.deckbuilder.arch.data.features.account.DefaultAccountRepository
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.DefaultCardRepository
import com.r0adkll.deckbuilder.arch.data.features.collection.repository.DefaultCollectionRepository
import com.r0adkll.deckbuilder.arch.data.features.collection.source.FirestoreCollectionSource
import com.r0adkll.deckbuilder.arch.data.features.collection.source.RoomCollectionSource
import com.r0adkll.deckbuilder.arch.data.features.community.repository.DefaultCommunityRepository
import com.r0adkll.deckbuilder.arch.data.features.decks.repository.DefaultDeckRepository
import com.r0adkll.deckbuilder.arch.data.features.editing.repository.DefaultEditRepository
import com.r0adkll.deckbuilder.arch.data.features.editing.source.FirestoreEditSource
import com.r0adkll.deckbuilder.arch.data.features.editing.source.RoomEditSource
import com.r0adkll.deckbuilder.arch.data.features.expansions.repository.DefaultExpansionRepository
import com.r0adkll.deckbuilder.arch.data.features.expansions.repository.source.DefaultExpansionDataSource
import com.r0adkll.deckbuilder.arch.data.features.marketplace.DefaultMarketplaceRepository
import com.r0adkll.deckbuilder.arch.data.features.offline.repository.DefaultOfflineRepository
import com.r0adkll.deckbuilder.arch.data.features.preview.RemotePreviewRepository
import com.r0adkll.deckbuilder.arch.domain.features.account.AccountRepository
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.collection.repository.CollectionRepository
import com.r0adkll.deckbuilder.arch.domain.features.community.repository.CommunityRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.expansions.repository.ExpansionRepository
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.repository.MarketplaceRepository
import com.r0adkll.deckbuilder.arch.domain.features.offline.repository.OfflineRepository
import com.r0adkll.deckbuilder.arch.domain.features.preview.PreviewRepository
import com.r0adkll.deckbuilder.internal.di.scopes.AppScope
import com.r0adkll.deckbuilder.util.AppSchedulers
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides @AppScope
    fun provideAccountRepository(repository: DefaultAccountRepository): AccountRepository = repository

    @Provides @AppScope
    fun provideDecksRepository(repository: DefaultDeckRepository): DeckRepository = repository

    @Provides @AppScope
    fun provideCommunityRepository(repository: DefaultCommunityRepository): CommunityRepository = repository

    @Provides @AppScope
    fun provideEditRepository(
        db: DeckDatabase,
        preferences: AppPreferences,
        schedulers: AppSchedulers,
        deckRepository: DeckRepository
    ): EditRepository {
        val localSource = RoomEditSource(db, schedulers)
        val remoteSource = FirestoreEditSource(preferences, schedulers)
        return DefaultEditRepository(localSource, remoteSource, preferences, deckRepository)
    }

    @Provides @AppScope
    fun provideExpansionRepository(
        defaultSource: DefaultExpansionDataSource
    ): ExpansionRepository = DefaultExpansionRepository(defaultSource)

    @Provides @AppScope
    fun provideCardRepository(repository: DefaultCardRepository): CardRepository = repository

    @Provides @AppScope
    fun provideOfflineRepository(repository: DefaultOfflineRepository): OfflineRepository = repository

    @Provides @AppScope
    fun providePreviewRepository(repository: RemotePreviewRepository): PreviewRepository {
        return repository
    }

    @Provides @AppScope
    fun provideMarketplaceRepository(
        repository: DefaultMarketplaceRepository
    ): MarketplaceRepository = repository

    @Provides @AppScope
    fun provideCollectionRepository(
        roomCollectionCache: RoomCollectionSource,
        firestoreCollectionCache: FirestoreCollectionSource,
        preferences: AppPreferences
    ): CollectionRepository {
        return DefaultCollectionRepository(roomCollectionCache, firestoreCollectionCache, preferences)
    }
}
