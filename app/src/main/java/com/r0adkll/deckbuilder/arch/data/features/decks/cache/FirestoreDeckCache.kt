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
            val task = collection.document(id).get()
            RxFirebase.from(task)
                    .map { it.toObject(DeckEntity::class.java) }
                    .flatMap { deck ->
                        val cardIds = deck.metadata().map { it.id }.toHashSet()
                        cardRepository.find(cardIds.toList())
                                .map { EntityMapper.to(deck, it) }
                    }
                    .subscribeOn(schedulers.firebase)
                    .doOnNext { Timber.d("Firebase::getDeck($id) - Thread(${Thread.currentThread()?.name})") }
        } ?: Observable.error(FirebaseAuthException("-1", "no current user logged in"))
    }


    override fun getDecks(): Observable<List<Deck>> {
        return Observable.create<List<DeckEntity>> { emitter ->
                    getUserDeckCollection()?.let { collection ->
                        val registration = collection.addSnapshotListener(schedulers.firebaseExecutor, EventListener { snapshot, exception ->
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
                .subscribeOn(schedulers.firebase)
    }


    override fun putDeck(id: String?, cards: List<PokemonCard>, name: String, description: String?, image: DeckImage?): Observable<Deck> {
        return getUserDeckCollection()?.let { collection ->
            val newDeck = Deck(id ?: "", name, description ?: "", image, cards, false, System.currentTimeMillis())
            val model = EntityMapper.to(newDeck)
            if (id == null) {
                val task = collection.add(model)
                RxFirebase.from(task)
                        .map { newDeck.copy(id = it.id) }
                        .subscribeOn(schedulers.firebase)
            }
            else {
                val task = collection.document(id)
                        .set(model)
                RxFirebase.fromVoid(task)
                        .map { newDeck }
                        .subscribeOn(schedulers.firebase)
            }
        } ?: Observable.error(FirebaseAuthException("-1", "No current user logged in"))
    }


    override fun duplicateDeck(deck: Deck): Observable<Unit> {
        return getUserDeckCollection()?.let { collection ->
            val query = collection.whereEqualTo("name", deck.name)
            RxFirebase.from(query.get())
                    .map { it.isEmpty }
                    .flatMap {
                        if (it) {
                            putDeck(null, deck.cards, deck.name, deck.description, deck.image)
                        }
                        else {
                            val regex = DUPLICATE_REGEX.toRegex()
                            val count = regex.findAll(deck.name).lastOrNull()?.let {
                                val count = it.value.replace("(", "").replace(")", "").trim().toIntOrNull() ?: 1
                                count + 1
                            } ?: 1

                            val cleanName = deck.name.replace(regex, "").trim()
                            val newName = "$cleanName ($count)"
                            duplicateDeck(Deck("", newName, deck.description, deck.image, deck.cards, false, deck.timestamp))
                        }
                    }
                    .map { Unit }
                    .subscribeOn(schedulers.firebase)

        } ?: Observable.error(FirebaseAuthException("-1", "No current user logged in"))
    }


    override fun deleteDeck(deck: Deck): Observable<Unit> {
        return getUserDeckCollection()?.let { collection ->
            val task = collection
                    .document(deck.id)
                    .delete()
            RxFirebase.fromVoid(task)
                    .subscribeOn(schedulers.firebase)
        } ?: Observable.error(FirebaseAuthException("-1", "No current user logged in"))
    }


    fun putDecks(decks: List<Deck>): Observable<Unit> {
        val db = FirebaseFirestore.getInstance()
        return getUserDeckCollection()?.let { collection ->
            val models = decks.map { EntityMapper.to(it) }
            val batch = db.batch()

            models.forEach {
                val document = collection.document()
                batch.set(document, it)
            }

            val task = batch.commit()

            RxFirebase.fromVoid(task)
                    .subscribeOn(schedulers.firebase)
        } ?: Observable.error(FirebaseAuthException("-1", "No current user logged in"))
    }


    private fun getUserDeckCollection(): CollectionReference? {
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        return user?.let { u ->
            db.collection(COLLECTION_USERS)
                    .document(u.uid)
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