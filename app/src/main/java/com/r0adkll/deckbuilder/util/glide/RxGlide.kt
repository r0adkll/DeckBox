package com.r0adkll.deckbuilder.util.glide

import android.content.Context
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.GlideRequest
import io.reactivex.Observable

object RxGlide {

    /**
     * Convert a [GlideRequest] into an observable to observe the resulting image load
     * operation in combination with other streams.
     */
    fun <T> GlideRequest<T>.asObservable(context: Context): Observable<T> {
        return Observable.fromCallable {
            val target = this.submit()
            val bitmap = target.get()

            GlideApp.with(context).clear(target)

            bitmap
        }
    }
}
