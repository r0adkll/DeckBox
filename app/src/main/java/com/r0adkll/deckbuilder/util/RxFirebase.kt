package com.r0adkll.deckbuilder.util


import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import io.reactivex.Observable
import timber.log.Timber
import kotlin.reflect.KClass


object RxFirebase {

    /**
     * Get a single value from a Firebase [DatabaseReference] in an observable manner
     */
//    fun <T : Any> getSingleValue(reference: DatabaseReference, clazz: KClass<T>) : Observable<T> {
//        return Observable.create { emitter ->
//            val listener = object : ValueEventListener {
//                override fun onCancelled(p0: DatabaseError?) {
//                    emitter.onError(p0?.toException() ?: DatabaseException("Error fetching value from Firebase"))
//                }
//
//                override fun onDataChange(dataSnapshot: DataSnapshot?) {
//                    dataSnapshot?.let {
//                        val change = it.getChange(clazz.java)
//                        if(value != null) {
//                            emitter.onNext(value)
//                        }
//                        emitter.onComplete()
//                    } ?: emitter.onError(DatabaseException("Error parsing value from Firebase"))
//                }
//            }
//
//            reference.addListenerForSingleValueEvent(listener)
//
//            emitter.setCancellable {
//                reference.removeEventListener(listener)
//            }
//        }
//    }
//
//
//    /**
//     * Observe changes/updates of a value from a Firebase [DatabaseReference] in an observable manner
//     */
//    fun <T : Any> observeValue(reference: DatabaseReference, clazz: KClass<T>) : Observable<T> {
//        return Observable.create { emitter ->
//            val listener = object : ValueEventListener {
//                override fun onCancelled(p0: DatabaseError?) {
//                    emitter.onError(p0?.toException() ?: DatabaseException("Error fetching value from Firebase"))
//                }
//
//                override fun onDataChange(dataSnapshot: DataSnapshot?) {
//                    dataSnapshot?.let {
//                        val change = it.getChange(clazz.java)
//                        if(value != null) {
//                            emitter.onNext(value)
//                        }
//                    } ?: emitter.onError(DatabaseException("Error parsing value from Firebase"))
//                }
//            }
//
//            reference.addValueEventListener(listener)
//
//            emitter.setCancellable {
//                reference.removeEventListener(listener)
//            }
//        }
//    }


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
