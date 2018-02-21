package com.r0adkll.deckbuilder.arch.data.features.decks.cache


import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.decks.mapper.EntityMapper
import com.r0adkll.deckbuilder.arch.data.features.decks.model.DeckEntity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.util.RxFirebase
import com.r0adkll.deckbuilder.util.Schedulers
import io.reactivex.Observable
import javax.inject.Inject
import com.google.firebase.firestore.FirebaseFirestoreSettings




@SuppressLint("CheckResult")
class FirestoreDeckCache @Inject constructor(
        val preferences: AppPreferences,
        val cardRepository: CardRepository,
        val schedulers: Schedulers
) : DeckCache {

    override fun getDeck(id: String): Observable<Deck> {
        return cardRepository.getExpansions()
                .flatMap { expansions ->
                    getUserDeckCollection()?.let { collection ->
                        val task = collection.document(id).get()
                        RxFirebase.from(task)
                                .map { it.toObject(DeckEntity::class.java) }
                                .map { EntityMapper.to(expansions, it, id) }
                    } ?: Observable.error(FirebaseAuthException("-1", "no current user logged in"))
                }
    }


    override fun getDecks(): Observable<List<Deck>> {
        return cardRepository.getExpansions()
                .flatMap { expansions ->
                    Observable.create<List<Deck>>({ emitter ->
                        getUserDeckCollection()?.let { collection ->
                            val registration = collection.addSnapshotListener(schedulers.firebaseExecutor, EventListener { snapshot, exception ->
                                if (exception != null) {
                                    emitter.onError(exception)
                                    return@EventListener
                                }

                                val decks = ArrayList<Deck>()
                                snapshot.forEach { document ->
                                    val deck = document.toObject(DeckEntity::class.java)
                                    decks.add(EntityMapper.to(expansions, deck, document.id))
                                }

                                emitter.onNext(decks)
                            })

                            emitter.setCancellable {
                                registration.remove()
                            }
                        } ?: emitter.onError(FirebaseAuthException("-1", "No current user logged in"))
                    })
                }
    }


    override fun putDeck(id: String?, cards: List<PokemonCard>, name: String, description: String?): Observable<Deck> {
        return getUserDeckCollection()?.let { collection ->
            val newDeck = Deck(id ?: "", name, description ?: "", cards, System.currentTimeMillis())
            val model = EntityMapper.to(newDeck)
            if (id == null) {
                val task = collection.add(model)
                RxFirebase.from(task)
                        .map { newDeck.copy(id = it.id) }
            }
            else {
                val task = collection.document(id)
                        .set(model)
                RxFirebase.fromVoid(task)
                        .map { newDeck }
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
                            putDeck(null, deck.cards, deck.name, deck.description)
                        }
                        else {
                            val regex = DUPLICATE_REGEX.toRegex()
                            val count = regex.findAll(deck.name).lastOrNull()?.let {
                                val count = it.value.replace("(", "").replace(")", "").trim().toIntOrNull() ?: 1
                                count + 1
                            } ?: 1

                            val cleanName = deck.name.replace(regex, "").trim()
                            val newName = "$cleanName ($count)"
                            duplicateDeck(Deck("", newName, deck.description, deck.cards, deck.timestamp))
                        }
                    }
                    .map { Unit }

        } ?: Observable.error(FirebaseAuthException("-1", "No current user logged in"))
    }


    override fun deleteDeck(deck: Deck): Observable<Unit> {
        return getUserDeckCollection()?.let { collection ->
            val task = collection
                    .document(deck.id)
                    .delete()
            RxFirebase.fromVoid(task)
        } ?: Observable.error(FirebaseAuthException("-1", "No current user logged in"))
    }


    private fun getUserDeckCollection(): CollectionReference? {
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        // Attempt to fix Crashlytics Issue #17 where the underlying SQLite database is getting deadlocked by
        // demanding offline persistence each time we try and access the Firestore database
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        db.firestoreSettings = settings

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
        @JvmField val DUPLICATE_REGEX = "\\(\\d+\\)"
        @JvmField val COLLECTION_USERS = "decks" // Do to an error on my side, this is now stuck as 'decks', but it is users
        @JvmField val COLLECTION_OFFLINE_USERS = "offline_users" // Do to an error on my side, this is now stuck as 'decks', but it is users
        @JvmField val COLLECTION_DECKS = "decks"
    }
}