package com.r0adkll.deckbuilder.arch.data.features.preview

import com.google.gson.Gson
import com.jakewharton.rxrelay2.BehaviorRelay
import com.r0adkll.deckbuilder.arch.domain.features.preview.PreviewRepository
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.ExpansionPreview
import io.reactivex.Observable
import java.io.IOException

class TestPreviewRepository : PreviewRepository {

    private val gson = Gson()
    private val testingPreviewJson = ""
    private var dismissed = BehaviorRelay.createDefault(false)

    override fun getExpansionPreview(): Observable<ExpansionPreview> {
        return dismissed.switchMap {
            if (!it) {
                val preview = gson.fromJson(testingPreviewJson, ExpansionPreview::class.java)
                Observable.just(preview)
            } else {
                Observable.error(IOException("No preview found"))
            }
        }
    }

    override fun dismissPreview() {
        dismissed.accept(true)
    }
}
