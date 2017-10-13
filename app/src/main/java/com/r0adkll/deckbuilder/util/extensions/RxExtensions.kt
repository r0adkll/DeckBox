package com.r0adkll.deckbuilder.util.extensions


import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    this.add(disposable)
}


fun <T, R> Observable<T>.scanMap(func2: (T?, T) -> R): Observable<R> {
    return this.startWith(null as T?) //emit a null value first, otherwise the .buffer() below won't emit at first (needs 2 emissions to emit)
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


data class Nullable<out T> constructor(val value: T?) {
    constructor() : this(null)


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
    return this.debounce(300, java.util.concurrent.TimeUnit.MILLISECONDS)
}


fun <T : Any> Observable<T>.mapError(throwable: Throwable): Observable<T> {
    return this.onErrorResumeNext { _: Throwable -> Observable.error<T>(throwable) }
}