package com.r0adkll.deckbuilder.util


import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import io.reactivex.Observable
import io.reactivex.Scheduler
import timber.log.Timber
import java.util.concurrent.Executor
import kotlin.reflect.KClass


object RxFirebase {

    fun <T : Any> Task<T>.toObservable(executor: Executor): Observable<T> {
        return RxFirebase.from(this, executor)
    }


    /**
     * Create an observable for any [Task] spawned from Firebase operation
     */
    fun <T : Any> from(task: Task<T>) : Observable<T> {
        return Observable.create { emitter ->
            val successListener = OnSuccessListener<T> {
                if(!emitter.isDisposed) {
                    Timber.d("RxFirebase::from::onSuccess($it)")
                    emitter.onNext(it)
                    emitter.onComplete()
                }
            }

            val failureListener = OnFailureListener {
                if(!emitter.isDisposed) {
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
    fun <T : Any> from(task: Task<T>, executor: Executor) : Observable<T> {
        return Observable.create { emitter ->
            val successListener = OnSuccessListener<T> {
                if(!emitter.isDisposed) {
                    Timber.d("RxFirebase::from::onSuccess($it)")
                    emitter.onNext(it)
                    emitter.onComplete()
                }
            }

            val failureListener = OnFailureListener {
                if(!emitter.isDisposed) {
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
    fun fromVoid(task: Task<Void>) : Observable<Unit> {
        return Observable.create { emitter ->
            val successListener = OnSuccessListener<Void> {
                if(!emitter.isDisposed) {
                    Timber.d("RxFirebase::from::onSuccess($it)")
                    emitter.onNext(Unit)
                    emitter.onComplete()
                }
            }

            val failureListener = OnFailureListener {
                if(!emitter.isDisposed) {
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
    fun fromVoid(task: Task<Void>, executor: Executor) : Observable<Unit> {
        return Observable.create { emitter ->
            val successListener = OnSuccessListener<Void> {
                if(!emitter.isDisposed) {
                    Timber.d("RxFirebase::from::onSuccess($it)")
                    emitter.onNext(Unit)
                    emitter.onComplete()
                }
            }

            val failureListener = OnFailureListener {
                if(!emitter.isDisposed) {
                    Timber.w("RxFirebase::from::onFailure($it)")
                    emitter.onError(it)
                }
            }

            task.addOnSuccessListener(executor, successListener)
            task.addOnFailureListener(executor, failureListener)
        }
    }


//    fun <T : Any> transaction(reference: DatabaseReference, clazz: KClass<T>, runnable: TransactionRunnable<T>) : Observable<T> {
//        return Observable.create { emitter ->
//            reference.runTransaction(object : Transaction.Handler {
//                override fun doTransaction(mutableData: MutableData): Transaction.Result {
//                    val existingValue = mutableData.getChange(clazz.java)
//                    val outputValue = runnable.runTransaction(existingValue)
//                    if(outputValue != null) {
//                        mutableData.value = outputValue
//                    }
//                    return Transaction.success(mutableData)
//                }
//
//                override fun onComplete(error: DatabaseError?, b: Boolean, dataSnapshot: DataSnapshot?) {
//                    if(!emitter.isDisposed) {
//                        if (error != null) {
//                            emitter.onError(error.toException())
//                        } else {
//                            val change = dataSnapshot?.getChange(clazz.java)
//                            if (value != null) {
//                                emitter.onNext(value)
//                            }
//                            emitter.onComplete()
//                        }
//                    }
//                }
//            })
//        }
//    }


    interface TransactionRunnable<T : Any> {
        fun runTransaction(data: T?) : T?
    }

}
