package com.r0adkll.deckbuilder.arch.domain.features.subtypes

import io.reactivex.Observable

interface SubtypeRepository {

  fun getSubTypes(force: Boolean = false): Observable<List<String>>
}
