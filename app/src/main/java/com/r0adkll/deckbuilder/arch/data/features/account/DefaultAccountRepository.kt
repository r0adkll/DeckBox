package com.r0adkll.deckbuilder.arch.data.features.account

import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.decks.cache.FirestoreDeckCache
import com.r0adkll.deckbuilder.arch.data.features.decks.cache.RoomDeckCache
import com.r0adkll.deckbuilder.arch.domain.features.account.AccountRepository
import com.r0adkll.deckbuilder.util.Schedulers
import io.reactivex.Observable
import java.security.InvalidParameterException
import javax.inject.Inject


class DefaultAccountRepository @Inject constructor(
        val roomDeckCache: RoomDeckCache,
        val firestoreDeckCache: FirestoreDeckCache,
        val preferences: AppPreferences,
        val schedulers: Schedulers
): AccountRepository {

    override fun migrateAccount(): Observable<Unit> {
        // Figure out the account type that we need to migrate
        return when {
            preferences.deviceId != null -> migrateLegacyOfflineAccount() // Legacy unlinked Firestore account
            preferences.offlineId.isSet && preferences.offlineId.get().isNotBlank() -> migrateOfflineAccount() // New room based offline account
            else -> Observable.error(InvalidParameterException("No found to migrate"))
        }
    }


    private fun migrateLegacyOfflineAccount(): Observable<Unit> {
        return firestoreDeckCache.migrateOfflineDecks()
                .doOnNext { preferences.deviceId = null }
                .subscribeOn(schedulers.firebase)
    }


    private fun migrateOfflineAccount(): Observable<Unit> {
        return roomDeckCache.getDecks()
                .take(1)
                .flatMap {
                    firestoreDeckCache.putDecks(it)
                            .subscribeOn(schedulers.firebase)
                            .observeOn(schedulers.database)
                            .doOnNext { _ ->
                                roomDeckCache.deleteAllDecks()
                                preferences.offlineId.delete()
                            }
                }
                .subscribeOn(schedulers.database)
    }
}