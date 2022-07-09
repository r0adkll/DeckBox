package com.r0adkll.deckbuilder.arch.data.features.subtypes

import com.r0adkll.deckbuilder.arch.data.features.subtypes.cache.SubtypeCache
import com.r0adkll.deckbuilder.arch.domain.features.subtypes.SubtypeRepository
import com.r0adkll.deckbuilder.util.AppSchedulers
import io.pokemontcg.Pokemon
import io.pokemontcg.allAsObservable
import io.reactivex.Observable
import javax.inject.Inject

class DefaultSubtypeRepository @Inject constructor(
  val api: Pokemon,
  val cache: SubtypeCache,
  val schedulers: AppSchedulers,
): SubtypeRepository {

  override fun getSubTypes(force: Boolean): Observable<List<String>> {
    return if (force) {
      network()
    } else {
      val cachedSubtypes = cache.get()
      if (cachedSubtypes.isNotEmpty()) {
        Observable.just(cachedSubtypes)
      } else {
        network()
      }
    }
  }

  private fun network(): Observable<List<String>> {
    return api.subType()
      .allAsObservable()
      .doOnNext {
        cache.put(it)
      }
      .subscribeOn(schedulers.network)
  }
}
