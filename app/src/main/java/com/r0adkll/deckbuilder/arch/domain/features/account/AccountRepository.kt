package com.r0adkll.deckbuilder.arch.domain.features.account

import io.reactivex.Observable


interface  AccountRepository {

    fun migrateAccount(): Observable<Unit>
}