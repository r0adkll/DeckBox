package com.r0adkll.deckbuilder.arch.data.features.collection.cache

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.collection.mapper.EntityMapper
import com.r0adkll.deckbuilder.arch.data.features.collection.model.CollectionCountEntity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.collection.model.CollectionCount
import com.r0adkll.deckbuilder.util.RxFirebase
import com.r0adkll.deckbuilder.util.RxFirebase.toObservable
import com.r0adkll.deckbuilder.util.Schedulers
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject


class FirestoreCollectionCache @Inject constructor(
        val preferences: AppPreferences,
        val schedulers: Schedulers
) : CollectionCache {

    override fun observeAll(): Observable<List<CollectionCount>> {
        return Observable.create<List<CollectionCountEntity>> { emitter ->
            getUserCardCollection()?.let { collection ->
                val registration = collection.addSnapshotListener(/*schedulers.firebaseExecutor, */EventListener { snapshot, exception ->
                    if (exception != null) {
                        emitter.onError(exception)
                        return@EventListener
                    }

                    val counts = ArrayList<CollectionCountEntity>()
                    snapshot?.forEach { document ->
                        counts += document.toObject(CollectionCountEntity::class.java)
                    }

                    Timber.d("Firebase::getDecks() - Thread(${Thread.currentThread().name})")
                    emitter.onNext(counts)
                })

                emitter.setCancellable {
                    registration.remove()
                }
            } ?: emitter.onError(FirebaseAuthException("-1", "No current user logged in"))
        }.map { it.map(EntityMapper::to) }
    }

    override fun getCount(cardId: String): Observable<CollectionCount> {
        return getUserCardCollection()?.let { collection ->
            val task = collection.whereEqualTo("cardId", cardId).get()
            RxFirebase.from(task, schedulers.firebaseExecutor)
                    .map { it.firstOrNull()?.toObject(CollectionCountEntity::class.java) ?: throw IllegalStateException("Unable to parse search result") }
                    .map { EntityMapper.to(it) }
        } ?: Observable.error(FirebaseAuthException("-1", "no current user logged in"))
    }

    override fun getCounts(cardIds: List<String>): Observable<List<CollectionCount>> {
        return getUserCardCollection()?.let { collection ->

            val tasks = cardIds
                    .map { collection.whereEqualTo("cardId", it).get() }
                    .map { task ->
                        RxFirebase.from(task, schedulers.firebaseExecutor)
                                .map { it.first().toObject(CollectionCountEntity::class.java) }
                    }

            Observable.merge(tasks)
                    .toList()
                    .map { it.map(EntityMapper::to) }
                    .toObservable()
        } ?: Observable.error(FirebaseAuthException("-1", "no current user logged in"))
    }

    override fun getCountForSet(set: String): Observable<List<CollectionCount>> {
        return getUserCardCollection()?.let { collection ->
            val task = collection.whereEqualTo("set", set).get()
            RxFirebase.from(task, schedulers.firebaseExecutor)
                    .map { it.toObjects(CollectionCountEntity::class.java) }
                    .map { it.map(EntityMapper::to) }
        } ?: Observable.error(FirebaseAuthException("-1", "no current user logged in"))
    }

    override fun getCountForSeries(series: String): Observable<List<CollectionCount>> {
        return getUserCardCollection()?.let { collection ->
            val task = collection.whereEqualTo("series", series).get()
            RxFirebase.from(task, schedulers.firebaseExecutor)
                    .map { it.toObjects(CollectionCountEntity::class.java) }
                    .map { it.map(EntityMapper::to) }
        } ?: Observable.error(FirebaseAuthException("-1", "no current user logged in"))
    }

    override fun incrementCount(card: PokemonCard): Observable<Unit> {
        return getUserCardCollection()?.let { collection ->
            collection.whereEqualTo("cardId", card.id).get()
                    .toObservable(schedulers.firebaseExecutor)
                    .flatMap { result ->
                        val countObject = result.firstOrNull()
                        when {
                            countObject != null -> {
                                val docId = countObject.id
                                val count = countObject.toObject(CollectionCountEntity::class.java)
                                count.count.inc()
                                collection.document(docId)
                                        .set(count)
                                        .toObservable(schedulers.firebaseExecutor)
                                        .map { Unit }
                            }
                            card.expansion != null -> collection.add(
                                    CollectionCountEntity(
                                            card.id,
                                            1,
                                            card.expansion.code,
                                            card.expansion.series
                                    )
                            ).toObservable(schedulers.firebaseExecutor).map { Unit }
                            else -> Observable.error(IllegalArgumentException("Can't find expansion for the provided card"))
                        }
                    }
        } ?: Observable.error(FirebaseAuthException("-1", "no current user logged in"))
    }

    override fun decrementCount(card: PokemonCard): Observable<Unit> {
        return getUserCardCollection()?.let { collection ->
            collection.whereEqualTo("cardId", card.id).get()
                    .toObservable(schedulers.firebaseExecutor)
                    .flatMap { result ->
                        val countObject = result.firstOrNull()
                        if (countObject != null) {
                            val docId = countObject.id
                            val count = countObject.toObject(CollectionCountEntity::class.java)
                            count.count.dec().coerceAtLeast(0)
                            collection.document(docId)
                                    .set(count)
                                    .toObservable(schedulers.firebaseExecutor)
                                    .map { Unit }
                        } else {
                            Observable.error(IllegalArgumentException("Can't find expansion for the provided card"))
                        }
                    }
        } ?: Observable.error(FirebaseAuthException("-1", "no current user logged in"))
    }

    private fun getUserCardCollection(): CollectionReference? {
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        return user?.let { u ->
            db.collection(COLLECTION_USERS)
                    .document(u.uid)
                    .collection(COLLECTION_COLLECTION)
        } ?: preferences.deviceId?.let { dId ->
            db.collection(COLLECTION_OFFLINE_USERS)
                    .document(dId)
                    .collection(COLLECTION_COLLECTION)
        }
    }


    companion object {
        private const val COLLECTION_USERS = "decks" // Do to an error on my side, this is now stuck as 'decks', but it is users
        private const val COLLECTION_OFFLINE_USERS = "offline_users" // Do to an error on my side, this is now stuck as 'decks', but it is users
        private const val COLLECTION_COLLECTION = "collection"
    }
}