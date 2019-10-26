package com.r0adkll.deckbuilder.util.extensions

import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.arch.data.mappings.CardMapper
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import io.pokemontcg.model.Card
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import timber.log.Timber
import java.util.concurrent.TimeUnit

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    this.add(disposable)
}

fun <T, R> Observable<T>.scanMap(func2: (T?, T) -> R): Observable<R> {
    return this.startWith(null as T?) //emit a null change first, otherwise the .buffer() below won't emit at first (needs 2 emissions to emit)
            .buffer(2, 1) //buffer the previous and current emission
            .filter { it.size >= 2 } //when the buffer terminates (onCompleted/onError), the remaining buffer is emitted. When don't want those!
            .map { func2.invoke(it[0], it[1]) }
}

fun <T, R> Observable<T>.scanMap(initialValue: T, func2: (T, T) -> R): Observable<R> {
    return this.startWith(initialValue)
            .buffer(2, 1)
            .filter { it.size >= 2 }
            .map { func2.invoke(it[0], it[1]) }
}

data class Nullable<out T> constructor(val value: T? = null) {

    fun isNull(): Boolean {
        return value == null
    }

    fun isNonNull(): Boolean {
        return !isNull()
    }

    override fun toString(): String {
        return value.toString()
    }

    companion object {
        val NULL = Nullable(null)
    }
}

fun <T : Any?> T.toNullable(): Nullable<T> {
    if (this == null) {
        return Nullable.NULL //reuse singleton
    } else {
        return Nullable(this)
    }
}

fun <T : Any, R : Any?> Observable<T>.mapNullable(func: (T) -> R?): Observable<Nullable<R?>> {
    return this.map { Nullable(func.invoke(it)) }
}

fun <T : Any> Observable<T>.uiDebounce(): Observable<T> {
    return this.debounce(300, TimeUnit.MILLISECONDS)
}

fun <T : Any> Observable<T>.uiDebounce(delayInMilliseconds: Long): Observable<T> {
    return this.debounce(delayInMilliseconds, TimeUnit.MILLISECONDS)
}

fun <T : Any> Observable<T>.mapError(throwable: Throwable): Observable<T> {
    return this.onErrorResumeNext { _: Throwable -> Observable.error<T>(throwable) }
}

fun <T : Any> Observable<T>.logState(): Observable<T> {
    return this.doOnNext { state ->
        if (BuildConfig.DEBUG) {
            Timber.v("    --- $state")
        }
    }
}

fun <T: Any> Observable<T>.retryWithBackoff(numRetries: Int = 3, delayInSeconds: Int = 5): Observable<T> {
    return this.retryWhen { t ->
        t.zipWith(Observable.range(1, numRetries), BiFunction<Throwable, Int, Int> { _, i -> i} )
                .flatMap { retryCount ->
                    Observable.timer(Math.pow(delayInSeconds.toDouble(), retryCount.toDouble()).toLong(), TimeUnit.SECONDS)
                }
    }
}

@Suppress("UNCHECKED_CAST")
operator fun Observable<List<Expansion>>.plus(cardSource: Observable<List<Card>>): Observable<List<PokemonCard>> {
    return Observable.combineLatestDelayError(listOf(this, cardSource)) { t: Array<out Any> ->
        val expansions = t[0] as List<Expansion>
        val cards = t[1] as List<Card>
        cards.map { CardMapper.to(it, expansions) }
    }.onErrorReturnItem(emptyList())
}
