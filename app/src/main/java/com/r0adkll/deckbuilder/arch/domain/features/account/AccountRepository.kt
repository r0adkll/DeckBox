package com.r0adkll.deckbuilder.arch.domain.features.account

import io.reactivex.Observable

interface AccountRepository {

    fun migrateAccount(): Observable<Unit>
    fun migrateCollections(): Observable<Unit>
    fun migrateLegacyCollectionCounts(): Observable<Unit>
}
