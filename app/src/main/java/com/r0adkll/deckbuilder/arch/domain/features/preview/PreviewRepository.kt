package com.r0adkll.deckbuilder.arch.domain.features.preview

import com.r0adkll.deckbuilder.arch.data.remote.model.ExpansionPreview
import io.reactivex.Observable


interface PreviewRepository {

    /**
     * Return an observable containing the expansion preview if it exists. Sending an error
     * if it doesn't
     */
    fun getExpansionPreview(): Observable<ExpansionPreview>

    /**
     * Dismiss the preview
     */
    fun dismissPreview()
}