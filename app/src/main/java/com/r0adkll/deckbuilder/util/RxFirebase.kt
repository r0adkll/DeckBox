package com.r0adkll.deckbuilder.util

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import timber.log.Timber
import java.util.concurrent.Executor

object RxFirebase {

    fun <T : Any> Task<T>.asObservable(executor: Executor): Observable<T> {
        return from(this, executor)
    }

    fun Task<Void>.asVoidObservable(executor: Executor): Observable<Unit> {
        return fromVoid(this, executor)
    }

    inline fun <reified T : Any> Query.observeAs(): Flowable<List<T>> {
        return Flowable.create({ source ->

            val registration = this.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    source.onError(firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = querySnapshot!!.toObjects(T::class.java)
                source.onNext(items)
            }

            source.setCancellable {
                registration.remove()
            }
        }, BackpressureStrategy.BUFFER)
    }

    inline fun <reified T : Any> Query.observeAs(crossinline mapper: (QueryDocumentSnapshot) -> T): Flowable<List<T>> {
        return Flowable.create({ source ->

            val registration = this.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    source.onError(firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = querySnapshot?.map { queryDocumentSnapshot ->
                    mapper(queryDocumentSnapshot)
                }

                if (!items.isNullOrEmpty()) {
                    source.onNext(items)
                }
            }

            source.setCancellable {
                registration.remove()
            }
        }, BackpressureStrategy.BUFFER)
    }

    /**
     * Create an observable for any [Task] spawned from Firebase operation
     */
    fun <T : Any> from(task: Task<T>): Observable<T> {
        return Observable.create { emitter ->
            val successListener = OnSuccessListener<T> {
                if (!emitter.isDisposed) {
                    Timber.d("RxFirebase::from::onSuccess($it)")
                    emitter.onNext(it)
                    emitter.onComplete()
                }
            }

            val failureListener = OnFailureListener {
                if (!emitter.isDisposed) {
                    Timber.w("RxFirebase::from::onFailure($it)")
                    emitter.onError(it)
                }
            }

            task.addOnSuccessListener(successListener)
            task.addOnFailureListener(failureListener)
        }
    }

    /**
     * Create an observable for any [Task] spawned from Firebase operation
     */
    fun <T : Any> from(task: Task<T>, executor: Executor): Observable<T> {
        return Observable.create { emitter ->
            val successListener = OnSuccessListener<T> {
                if (!emitter.isDisposed) {
                    Timber.d("RxFirebase::from::onSuccess($it)")
                    emitter.onNext(it)
                    emitter.onComplete()
                }
            }

            val failureListener = OnFailureListener {
                if (!emitter.isDisposed) {
                    Timber.w("RxFirebase::from::onFailure($it)")
                    emitter.onError(it)
                }
            }

            task.addOnSuccessListener(executor, successListener)
            task.addOnFailureListener(executor, failureListener)
        }
    }

    /**
     * Create an observable for any [Task] spawned from Firebase operation
     */
    fun fromVoid(task: Task<Void>): Observable<Unit> {
        return Observable.create { emitter ->
            val successListener = OnSuccessListener<Void> {
                if (!emitter.isDisposed) {
                    Timber.d("RxFirebase::from::onSuccess($it)")
                    emitter.onNext(Unit)
                    emitter.onComplete()
                }
            }

            val failureListener = OnFailureListener {
                if (!emitter.isDisposed) {
                    Timber.w("RxFirebase::from::onFailure($it)")
                    emitter.onError(it)
                }
            }

            task.addOnSuccessListener(successListener)
            task.addOnFailureListener(failureListener)
        }
    }

    /**
     * Create an observable for any [Task] spawned from Firebase operation
     */
    fun fromVoid(task: Task<Void>, executor: Executor): Observable<Unit> {
        return Observable.create { emitter ->
            val successListener = OnSuccessListener<Void> {
                if (!emitter.isDisposed) {
                    Timber.d("RxFirebase::from::onSuccess($it)")
                    emitter.onNext(Unit)
                    emitter.onComplete()
                }
            }

            val failureListener = OnFailureListener {
                if (!emitter.isDisposed) {
                    Timber.w("RxFirebase::from::onFailure($it)")
                    emitter.onError(it)
                }
            }

            task.addOnSuccessListener(executor, successListener)
            task.addOnFailureListener(executor, failureListener)
        }
    }
}
