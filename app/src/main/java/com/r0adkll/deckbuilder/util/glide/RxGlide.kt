package com.r0adkll.deckbuilder.util.glide

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.GlideRequest
import io.reactivex.Observable
import java.io.IOException


object RxGlide {

    /**
     * Convert a [GlideRequest] into an observable to observe the resulting image load
     * operation in combination with other streams.
     */
    fun <T> GlideRequest<T>.asObservable(context: Context): Observable<T> {
        return Observable.create { s ->
            val target = this.into(object : SimpleTarget<T>() {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    s.onError(IOException("Unable to load drawable"))
                }

                override fun onResourceReady(resource: T, transition: Transition<in T>?) {
                    s.onNext(resource)
                    s.onComplete()
                }
            })

            s.setCancellable {
                GlideApp.with(context).clear(target)
            }
        }
    }
}