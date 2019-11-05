package com.r0adkll.deckbuilder.arch.data

import com.r0adkll.deckbuilder.arch.data.features.cards.cache.CardCache
import com.r0adkll.deckbuilder.arch.data.features.cards.cache.RoomCardCache
import com.r0adkll.deckbuilder.arch.data.features.community.cache.CommunityCache
import com.r0adkll.deckbuilder.arch.data.features.community.cache.FirestoreCommunityCache
import com.r0adkll.deckbuilder.arch.data.features.decks.cache.DeckCache
import com.r0adkll.deckbuilder.arch.data.features.decks.cache.SwitchingDeckCache
import com.r0adkll.deckbuilder.arch.data.features.editing.cache.RoomSessionCache
import com.r0adkll.deckbuilder.arch.data.features.editing.cache.SessionCache
import com.r0adkll.deckbuilder.arch.data.features.offline.repository.DefaultOfflineRepository
import com.r0adkll.deckbuilder.arch.data.features.offline.repository.OfflineStatusConsumer
import com.r0adkll.deckbuilder.internal.di.scopes.AppScope
import dagger.Module
import dagger.Provides

@Module
class CacheModule {

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
}
