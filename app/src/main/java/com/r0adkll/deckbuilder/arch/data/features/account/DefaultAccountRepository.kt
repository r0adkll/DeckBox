package com.r0adkll.deckbuilder.arch.data.features.account

import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.decks.cache.FirestoreDeckCache
import com.r0adkll.deckbuilder.arch.data.features.decks.cache.RoomDeckCache
import com.r0adkll.deckbuilder.arch.domain.features.account.AccountRepository
import io.reactivex.Observable
import java.security.InvalidParameterException
import javax.inject.Inject


class DefaultAccountRepository @Inject constructor(
        val roomDeckCache: RoomDeckCache,
        val firestoreDeckCache: FirestoreDeckCache,
        val preferences: AppPreferences
): AccountRepository {

    override fun migrateAccount(): Observable<Unit> {
        // Figure out the account type that we need to migrate
        return when {
            preferences.deviceId != null -> migrateLegacyOfflineAccount() // Legacy unlinked Firestore account
            preferences.offlineId != null -> migrateOfflineAccount() // New room based offline account
            else -> Observable.error(InvalidParameterException("No found to migrate"))
        }
    }


    private fun migrateLegacyOfflineAccount(): Observable<Unit> {
        TODO()
    }


    private fun migrateOfflineAccount(): Observable<Unit> {
        TODO()
    }
}