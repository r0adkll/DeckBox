package com.r0adkll.deckbuilder.arch.data.features.decks.cache


import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.decks.mapper.EntityMapper
import com.r0adkll.deckbuilder.arch.data.features.decks.mapper.EntityMapper.metadata
import com.r0adkll.deckbuilder.arch.data.features.decks.model.DeckEntity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.util.RxFirebase
import com.r0adkll.deckbuilder.util.RxFirebase.asObservable
import com.r0adkll.deckbuilder.util.RxFirebase.asVoidObservable
import com.r0adkll.deckbuilder.util.Schedulers
import io.reactivex.Observable
import javax.inject.Inject
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import timber.log.Timber


@SuppressLint("CheckResult")
class FirestoreDeckCache @Inject constructor(
        val preferences: AppPreferences,
        val cardRepository: CardRepository,
        val schedulers: Schedulers
) : DeckCache {

    override fun getDeck(id: String): Observable<Deck> {
        return getUserDeckCollection()?.let { collection ->
            collection.document(id)
                    .get()
                    .asObservable(schedulers.firebaseExecutor)
                    .map { it.toObject(DeckEntity::class.java) }
                    .flatMap { deck ->
                        val cardIds = deck.metadata().map { it.id }.toHashSet()
                        cardRepository.find(cardIds.toList())
                                .map { EntityMapper.to(deck, it) }
                    }
                    .doOnNext { Timber.d("Firebase::getDeck($id) - Thread(${Thread.currentThread().name})") }
        } ?: Observable.error(FirebaseAuthException("-1", "no current user logged in"))
    }


    override fun getDecks(): Observable<List<Deck>> {
        return Observable.create<List<DeckEntity>> { emitter ->
                    getUserDeckCollection()?.let { collection ->
                        val registration = collection.addSnapshotListener(EventListener { snapshot, exception ->
                            if (exception != null) {
                                emitter.onError(exception)
                                return@EventListener
                            }

                            val decks = ArrayList<DeckEntity>()
                            snapshot?.forEach { document ->
                                val deckEntity = document.toObject(DeckEntity::class.java)
                                deckEntity.id = document.id
                                decks += deckEntity
                            }

                            Timber.d("Firebase::getDecks() - Thread(${Thread.currentThread().name})")
                            emitter.onNext(decks)
                        })

                        emitter.setCancellable {
                            registration.remove()
                        }
                    } ?: emitter.onError(FirebaseAuthException("-1", "No current user logged in"))
                }
                .flatMap { decks ->
                    val cardIds = decks.flatMap {
                        it.metadata().map { it.id }
                    }.toHashSet() // Convert to set so we don't request duplicate id's

                    cardRepository.find(cardIds.toList())
                            .map { cards ->
                                decks.map { EntityMapper.to(it, cards) }
                            }
                }
    }


    override fun putDeck(
            id: String?,
            cards: List<PokemonCard>,
            name: String,
            description: String?,
            image: DeckImage?,
            collectionOnly: Boolean
    ): Observable<Deck> {
        return getUserDeckCollection()?.let { collection ->
            val newDeck = Deck(id ?: "", name, description ?: "", image, collectionOnly, cards, false, System.currentTimeMillis())
            val model = EntityMapper.to(newDeck)
            if (id == null) {
                collection.add(model)
                        .asObservable(schedulers.firebaseExecutor)
                        .map { newDeck.copy(id = it.id) }
            } else {
                collection.document(id)
                        .set(model)
                        .asVoidObservable(schedulers.firebaseExecutor)
                        .map { newDeck }
            }
        } ?: Observable.error(FirebaseAuthException("-1", "No current user logged in"))
    }


    override fun duplicateDeck(deck: Deck): Observable<Unit> {
        return getUserDeckCollection()?.let { collection ->
            val query = collection.whereEqualTo("name", deck.name)
            query.get()
                    .asObservable(schedulers.firebaseExecutor)
                    .map { it.isEmpty }
                    .flatMap {
                        if (it) {
                            putDeck(null, deck.cards, deck.name, deck.description, deck.image, deck.collectionOnly)
                        }
                        else {
                            val regex = DUPLICATE_REGEX.toRegex()
                            val count = regex.findAll(deck.name).lastOrNull()?.let {
                                val count = it.value.replace("(", "").replace(")", "").trim().toIntOrNull() ?: 1
                                count + 1
                            } ?: 1

                            val cleanName = deck.name.replace(regex, "").trim()
                            val newName = "$cleanName ($count)"
                            duplicateDeck(Deck("", newName, deck.description, deck.image, deck.collectionOnly, deck.cards, deck.isMissingCards, deck.timestamp))
                        }
                    }
                    .map { Unit }

        } ?: Observable.error(FirebaseAuthException("-1", "No current user logged in"))
    }


    override fun deleteDeck(deck: Deck): Observable<Unit> {
        return getUserDeckCollection()?.let { collection ->
            collection.document(deck.id)
                    .delete()
                    .asVoidObservable(schedulers.firebaseExecutor)
        } ?: Observable.error(FirebaseAuthException("-1", "No current user logged in"))
    }


    /**
     * Batch insert a list of decks into an authed account
     * @return observable of the task, or an observable error if account is anonymous or null
     */
    fun putDecks(decks: List<Deck>): Observable<Unit> {
        val models = decks.map { EntityMapper.to(it) }
        return putDeckEntities(models)
    }


    /**
     * Batch insert a list of decks into an authed account
     * @return observable of the task, or an observable error if account is anonymous or null
     */
    private fun putDeckEntities(decks: List<DeckEntity>): Observable<Unit> {
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        if (user?.isAnonymous == false) {
            val collection = db.collection(COLLECTION_USERS)
                    .document(user.uid)
                    .collection(COLLECTION_DECKS)

            val batch = db.batch()

            decks.forEach {
                val document = collection.document()
                batch.set(document, it)
            }

            return batch.commit()
                    .asVoidObservable(schedulers.firebaseExecutor)
        } else {
            return Observable.error(FirebaseAuthException("-1", "No current user logged in"))
        }
    }


    /**
     * Batch insert a list of decks into an authed account
     * @return observable of the task, or an observable error if account is anonymous or null
     */
    fun migrateOfflineDecks(): Observable<Unit> {
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        if (user?.isAnonymous == false && preferences.deviceId != null) {
            val offlineCollection = db.collection(COLLECTION_OFFLINE_USERS)
                    .document(preferences.deviceId!!)
                    .collection(COLLECTION_DECKS)

            val onlineCollection = db.collection(COLLECTION_USERS)
                    .document(user.uid)
                    .collection(COLLECTION_DECKS)

            return offlineCollection.get()
                    .asObservable(schedulers.firebaseExecutor)
                    .map { snapshot ->
                        val decks = ArrayList<DeckEntity>()
                        snapshot.forEach { document ->
                            val deckEntity = document.toObject(DeckEntity::class.java)
                            deckEntity.id = document.id
                            decks += deckEntity
                        }
                        decks
                    }
                    .flatMap { decks ->
                        val batch = db.batch()
                        decks.forEach {
                            val document = onlineCollection.document()
                            batch.set(document, it)
                            batch.delete(offlineCollection.document(it.id))
                        }
                        batch.commit().asVoidObservable(schedulers.firebaseExecutor)
                    }
                    .subscribeOn(schedulers.firebase)
        } else {
            return Observable.error(FirebaseAuthException("-1", "No current user logged in"))
        }
    }


    private fun getUserDeckCollection(): CollectionReference? {
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        return user?.let { u ->
            db.collection(COLLECTION_USERS)
                    .document(preferences.testUserId ?: u.uid)
                    .collection(COLLECTION_DECKS)
        } ?: preferences.deviceId?.let { dId ->
            db.collection(COLLECTION_OFFLINE_USERS)
                    .document(dId)
                    .collection(COLLECTION_DECKS)
        }
    }


    companion object {
        private const val DUPLICATE_REGEX = "\\(\\d+\\)"
        private const val COLLECTION_USERS = "decks" // Do to an error on my side, this is now stuck as 'decks', but it is users
        private const val COLLECTION_OFFLINE_USERS = "offline_users" // Do to an error on my side, this is now stuck as 'decks', but it is users
        private const val COLLECTION_DECKS = "decks"
    }
}