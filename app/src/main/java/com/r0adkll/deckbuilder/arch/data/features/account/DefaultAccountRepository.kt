package com.r0adkll.deckbuilder.arch.data.features.account

import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.collection.source.FirestoreCollectionSource
import com.r0adkll.deckbuilder.arch.data.features.collection.source.RoomCollectionSource
import com.r0adkll.deckbuilder.arch.data.features.decks.cache.FirestoreDeckCache
import com.r0adkll.deckbuilder.arch.data.features.decks.cache.RoomDeckCache
import com.r0adkll.deckbuilder.arch.domain.features.account.AccountRepository
import com.r0adkll.deckbuilder.util.AppSchedulers
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.security.InvalidParameterException
import javax.inject.Inject


class DefaultAccountRepository @Inject constructor(
        val roomDeckCache: RoomDeckCache,
        val roomCollectionCache: RoomCollectionSource,
        val firestoreDeckCache: FirestoreDeckCache,
        val firestoreCollectionCache: FirestoreCollectionSource,
        val preferences: AppPreferences,
        val schedulers: AppSchedulers
): AccountRepository {

    override fun migrateAccount(): Observable<Unit> {
        // Figure out the account type that we need to migrate
        return when {
            preferences.deviceId != null -> migrateLegacyOfflineAccount() // Legacy unlinked Firestore account
            preferences.offlineId.isSet && preferences.offlineId.get().isNotBlank() -> migrateOfflineAccount() // New room based offline account
            else -> Observable.error(InvalidParameterException("No found to migrate"))
        }
    }

    override fun migrateCollections(): Observable<Unit> {
        return migrateOfflineCollectionCounts()
    }

    override fun migrateLegacyCollectionCounts(): Observable<Unit> {
        return firestoreCollectionCache.migrateLegacyCounts()
    }

    private fun migrateLegacyOfflineAccount(): Observable<Unit> {
        return firestoreDeckCache.migrateOfflineDecks()
                .doOnNext { preferences.deviceId = null }
                .subscribeOn(schedulers.firebase)
    }

    private fun migrateOfflineAccount(): Observable<Unit> {
        val migrateDecks = migrateOfflineDecks()
        val migrateCollectionCounts = migrateOfflineCollectionCounts()
        return Observable.zip(migrateDecks, migrateCollectionCounts, BiFunction { _, _ ->
            preferences.offlineId.delete()
        })
    }

    private fun migrateOfflineDecks(): Observable<Unit> {
        return roomDeckCache.getDecks()
                .take(1)
                .flatMap {
                    firestoreDeckCache.putDecks(it)
                            .subscribeOn(schedulers.firebase)
                            .observeOn(schedulers.database)
                            .doOnNext {
                                roomDeckCache.deleteAll()
                            }
                }
                .subscribeOn(schedulers.database)
    }

    private fun migrateOfflineCollectionCounts(): Observable<Unit> {
        return roomCollectionCache.getAll()
                .flatMapObservable {
                    firestoreCollectionCache.putCounts(it)
                            .subscribeOn(schedulers.firebase)
                            .observeOn(schedulers.database)
                            .doOnNext {
                                roomCollectionCache.deleteAll()
                            }
                }
                .subscribeOn(schedulers.database)
    }
}
