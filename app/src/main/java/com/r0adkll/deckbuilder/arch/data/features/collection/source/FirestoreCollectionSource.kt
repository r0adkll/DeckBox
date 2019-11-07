package com.r0adkll.deckbuilder.arch.data.features.collection.source

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code
import com.google.firebase.firestore.WriteBatch
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.collection.mapper.EntityMapper
import com.r0adkll.deckbuilder.arch.data.features.collection.model.CollectionCountEntity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.collection.model.CollectionCount
import com.r0adkll.deckbuilder.util.AppSchedulers
import com.r0adkll.deckbuilder.util.RxFirebase.asObservable
import com.r0adkll.deckbuilder.util.RxFirebase.asVoidObservable
import com.r0adkll.deckbuilder.util.RxFirebase.observeAs
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.ceil

class FirestoreCollectionSource @Inject constructor(
    val preferences: AppPreferences,
    val schedulers: AppSchedulers
) : CollectionSource {

    override fun observeAll(): Observable<List<CollectionCount>> {
        return getUserCardCollection()?.let { collection ->
            collection
                .observeAs {
                    val documentId = it.id
                    val entity = it.toObject(CollectionCountEntity::class.java)
                    EntityMapper.to(entity, documentId)
                }
                .toObservable()
                .doOnNext { Timber.i("Collection All from Network: ${it.size}") }
        } ?: Observable.error(FirebaseAuthException("-1", "No current user logged in"))
    }

    override fun getCount(cardId: String): Observable<CollectionCount> {
        return getUserCardCollection()?.let { collection ->
            collection.document(cardId).get()
                .asObservable(schedulers.firebaseExecutor)
                .map {
                    it.toObject(CollectionCountEntity::class.java)
                        ?: throw IllegalStateException("Unable to parse search result")
                }
                .map { EntityMapper.to(it) }
        } ?: Observable.error(FirebaseAuthException("-1", "no current user logged in"))
    }

    override fun getCountForSet(set: String): Observable<List<CollectionCount>> {
        return getUserCardCollection()?.let { collection ->
            collection.whereEqualTo("set", set).get()
                .asObservable(schedulers.firebaseExecutor)
                .map {
                    it.map { documentSnapshot ->
                        val documentId = documentSnapshot.id
                        val entity = documentSnapshot.toObject(CollectionCountEntity::class.java)
                        EntityMapper.to(entity, documentId)
                    }
                }
        } ?: Observable.error(FirebaseAuthException("-1", "no current user logged in"))
    }

    override fun getCountForSeries(series: String): Observable<List<CollectionCount>> {
        return getUserCardCollection()?.let { collection ->
            collection.whereEqualTo("series", series).get()
                .asObservable(schedulers.firebaseExecutor)
                .map {
                    it.map { documentSnapshot ->
                        val documentId = documentSnapshot.id
                        val entity = documentSnapshot.toObject(CollectionCountEntity::class.java)
                        EntityMapper.to(entity, documentId)
                    }
                }
        } ?: Observable.error(FirebaseAuthException("-1", "no current user logged in"))
    }

    override fun incrementCount(card: PokemonCard): Observable<Unit> {
        return getUserCardCollection()?.let { collection ->
            collection.document(card.id)
                .update("count", FieldValue.increment(1),
                    "cardId", card.id,
                    "set", card.expansion?.code ?: "",
                    "series", card.expansion?.series ?: "")
                .asVoidObservable(schedulers.firebaseExecutor)
                .onErrorResumeNext { t: Throwable ->
                    if (t is FirebaseFirestoreException) {
                        when (t.code) {
                            Code.NOT_FOUND -> {
                                collection.document(card.id)
                                    .set(CollectionCountEntity(
                                        card.id,
                                        1,
                                        card.expansion?.code ?: "",
                                        card.expansion?.series ?: ""
                                    ))
                                    .asVoidObservable(schedulers.firebaseExecutor)
                            }
                            else -> Observable.error(t)
                        }
                    } else {
                        Observable.error(t)
                    }
                }
                .subscribeOn(schedulers.firebase)
        } ?: Observable.error(FirebaseAuthException("-1", "no current user logged in"))
    }

    override fun decrementCount(card: PokemonCard): Observable<Unit> {
        return getUserCardCollection()?.let { collection ->
            collection.document(card.id)
                .update("count", FieldValue.increment(-1),
                    "cardId", card.id,
                    "set", card.expansion?.code ?: "",
                    "series", card.expansion?.series ?: "")
                .asVoidObservable(schedulers.firebaseExecutor)
                .subscribeOn(schedulers.firebase)
        } ?: Observable.error(FirebaseAuthException("-1", "no current user logged in"))
    }

    override fun incrementSet(set: String, cards: List<PokemonCard>): Observable<List<CollectionCount>> {
        return getUserCardCollection()?.let { collection ->
            collection.whereEqualTo("set", set)
                .get()
                .asObservable(schedulers.firebaseExecutor)
                .flatMap {
                    val batch = FirebaseFirestore.getInstance().batch()
                    val entities = it.toObjects(CollectionCountEntity::class.java)
                    entities.forEach { entity ->
                        batch.update(collection.document(entity.cardId), "count", entity.count + 1)
                    }

                    val missingCards = cards.filter { card ->
                        entities.none { entity ->
                            entity.cardId == card.id
                        }
                    }.map { card ->
                        CollectionCountEntity(
                            card.id, 1, card.expansion!!.code, card.expansion.series
                        )
                    }

                    missingCards.forEach { count ->
                        batch.set(collection.document(count.cardId), count)
                    }

                    batch.commit()
                        .asVoidObservable(schedulers.firebaseExecutor)
                        .map {
                            entities.forEach { it.count++ }
                            entities.plus(missingCards)
                                .map(EntityMapper::to)
                        }
                }
        } ?: Observable.error(FirebaseAuthException("-1", "no current user logged in"))
    }

    fun migrateLegacyCounts(): Observable<Unit> {
        return getUserCardCollection()?.let { collection ->
            collection.get()
                .asObservable(schedulers.firebaseExecutor)
                .flatMap {
                    val counts = it.map { queryDocumentSnapshot ->
                        val documentId = queryDocumentSnapshot.id
                        val entity = queryDocumentSnapshot.toObject(CollectionCountEntity::class.java)
                        documentId to EntityMapper.to(entity, documentId)
                    }

                    val legacyCounts = counts.filter { count -> count.second.isSourceOld }
                    if (legacyCounts.isNotEmpty()) {
                        // Since we are writing and deleting each count
                        val batchCount = ceil(legacyCounts.size.toFloat() / HALF_BATCH_SIZE).toInt()
                        val batches = ArrayList<WriteBatch>(batchCount)
                        for (i in 0 until batchCount) {
                            Timber.i("Legacy collection counts(${legacyCounts.size}) found! Migrating...")
                            val batch = FirebaseFirestore.getInstance().batch()

                            val start = i * HALF_BATCH_SIZE
                            val end = start + (legacyCounts.size - start).coerceAtMost(HALF_BATCH_SIZE)
                            for (index in start until end) {
                                val legacyCountPair = legacyCounts[index]
                                Timber.d("""Migrating Id(${legacyCountPair.first}) to 
                                    /collection/${legacyCountPair.second.id}""".trimMargin())
                                val legacyCount = legacyCountPair.second
                                val newDocumentRef = collection.document(legacyCount.id)
                                batch.set(newDocumentRef, CollectionCountEntity(
                                    legacyCount.id,
                                    legacyCount.count,
                                    legacyCount.set,
                                    legacyCount.series
                                ))
                                batch.delete(collection.document(legacyCountPair.first))
                            }

                            batches += batch
                        }

                        if (batches.isNotEmpty()) {
                            val commits = batches.map { it.commit().asVoidObservable(schedulers.firebaseExecutor) }
                            Observable.zip(commits) { Unit }
                        } else {
                            Timber.i("No batch changes made, Aborting...")
                            Observable.just(Unit)
                        }
                    } else {
                        Timber.i("No legacy count objects found. Aborting...")
                        Observable.just(Unit)
                    }
                }
        } ?: Observable.error(FirebaseAuthException("-1", "no current user logged in"))
    }

    /**
     * Internal function to help migrate offline collection counts to online
     */
    fun putCounts(counts: List<CollectionCount>): Observable<Unit> {
        return getUserCardCollection()?.let { collection ->
            val batchCount = ceil(counts.size.toFloat() / BATCH_SIZE).toInt()
            val batches = ArrayList<WriteBatch>(batchCount)

            for (i in 0 until batchCount) {
                Timber.i("Collection counts(${counts.size}) found! Migrating...")
                val batch = FirebaseFirestore.getInstance().batch()

                val start = i * BATCH_SIZE
                val end = start + (counts.size - start).coerceAtMost(BATCH_SIZE)
                for (index in start until end) {
                    val count = counts[index]
                    val documentRef = collection.document(count.id)
                    batch.set(documentRef, CollectionCountEntity(
                        count.id,
                        count.count,
                        count.set,
                        count.series
                    ))
                }

                batches += batch
            }

            if (batches.isNotEmpty()) {
                val commits = batches.map { it.commit().asVoidObservable(schedulers.firebaseExecutor) }
                Observable.zip(commits) { Unit }
            } else {
                Timber.i("No batch changes made, Aborting...")
                Observable.just(Unit)
            }
        } ?: Observable.error(FirebaseAuthException("-1", "no current user logged in"))
    }

    private fun getUserCardCollection(): CollectionReference? {
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        return user?.let { u ->
            db.collection(COLLECTION_USERS)
                .document(preferences.testUserId ?: u.uid)
                .collection(COLLECTION_COLLECTION)
        } ?: preferences.deviceId?.let { dId ->
            db.collection(COLLECTION_OFFLINE_USERS)
                .document(dId)
                .collection(COLLECTION_COLLECTION)
        }
    }

    companion object {
        // Do to an error on my side, this is now stuck as 'decks', but it is users
        private const val COLLECTION_USERS = "decks"
        private const val COLLECTION_OFFLINE_USERS = "offline_users"
        private const val COLLECTION_COLLECTION = "collection"

        private const val BATCH_SIZE = 500
        private const val HALF_BATCH_SIZE = 250
    }
}
