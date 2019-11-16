package com.r0adkll.deckbuilder.arch.domain.features.offline.model

import android.os.Parcelable
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OfflineStatus(val expansions: Map<Expansion, CacheStatus> = emptyMap()) : Parcelable {

    fun set(expansion: Expansion, status: CacheStatus): OfflineStatus {
        val updatedExpansions = expansions.plus(expansion to status)
        return copy(expansions = updatedExpansions)
    }

    fun set(status: Pair<Expansion, CacheStatus>): OfflineStatus {
        val updatedExpansions = expansions.plus(status)
        return copy(expansions = updatedExpansions)
    }

    fun set(statuses: Map<Expansion, CacheStatus>): OfflineStatus {
        val updatedExpansions = expansions.plus(statuses)
        return copy(expansions = updatedExpansions)
    }
}
