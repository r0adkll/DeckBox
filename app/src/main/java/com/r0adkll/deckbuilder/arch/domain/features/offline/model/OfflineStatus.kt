package com.r0adkll.deckbuilder.arch.domain.features.offline.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class OfflineStatus(val expansions: Map<String, CacheStatus> = emptyMap()) : Parcelable {

    fun set(setCode: String, status: CacheStatus): OfflineStatus {
        val updatedExpansions = expansions.plus(setCode to status)
        return copy(expansions = updatedExpansions)
    }

    fun set(status: Pair<String, CacheStatus>): OfflineStatus {
        val updatedExpansions = expansions.plus(status)
        return copy(expansions = updatedExpansions)
    }

    fun set(statuses: Map<String, CacheStatus>): OfflineStatus {
        val updatedExpansions = expansions.plus(statuses)
        return copy(expansions = updatedExpansions)
    }


    companion object {
        const val ALL = "all"
    }
}