package com.r0adkll.deckbuilder.arch.data.features.expansions.repository

import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.expansions.repository.source.ExpansionDataSource
import com.r0adkll.deckbuilder.arch.domain.features.expansions.repository.ExpansionRepository
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.lang.IllegalArgumentException

class DefaultExpansionRepository(
        val defaultSource: ExpansionDataSource,
        val previewSource: ExpansionDataSource,
        val appPreferences: AppPreferences,
        val remote: Remote
) : ExpansionRepository {

    private val shouldIncludePreview: Boolean
        get() = remote.previewExpansionVersion != null &&
                remote.previewExpansionVersion?.expansionCode != remote.expansionVersion?.expansionCode

    override fun getExpansion(code: String): Observable<Expansion> {
        val previewExpansionVersion = remote.previewExpansionVersion
        return if (previewExpansionVersion?.expansionCode == code) {
            previewSource.getExpansions()
                    .map { expansions ->
                        expansions.find { it.code == code }
                                ?: throw IllegalArgumentException("No preview expansion found for ${previewExpansionVersion.expansionCode}")
                    }
        } else {
            defaultSource.getExpansions()
                    .map { expansions ->
                        expansions.find { it.code == code }
                                ?: throw IllegalArgumentException("No expansion found for $code")
                    }
        }
    }

    override fun getExpansions(): Observable<List<Expansion>> {
        return if (shouldIncludePreview) {
            Observable.combineLatest(
                    defaultSource.getExpansions(),
                    previewSource.getExpansions(),
                    BiFunction { default, preview ->
                        default.plus(preview)
                    }
            )
        } else {
            defaultSource.getExpansions()
        }
    }

    override fun refreshExpansions(): Observable<List<Expansion>> {
        val isRemoteDefined = remote.previewExpansionVersion != null

        return if (isRemoteDefined) {
            Observable.combineLatest(
                    defaultSource.refreshExpansions(),
                    previewSource.getExpansions(),
                    BiFunction { default, preview ->
                        default.plus(preview)
                    }
            )
        } else {
            defaultSource.refreshExpansions()
        }
    }
}
