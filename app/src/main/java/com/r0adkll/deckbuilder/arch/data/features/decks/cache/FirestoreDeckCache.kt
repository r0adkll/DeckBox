package com.r0adkll.deckbuilder.arch.data.features.decks.cache

import com.google.firebase.firestore.FirebaseFirestore
import com.r0adkll.deckbuilder.arch.data.features.decks.mapper.EntityMapper
import com.r0adkll.deckbuilder.arch.data.features.decks.model.DeckEntity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.util.RxFirebase
import io.reactivex.Observable
import javax.inject.Inject


class FirestoreDeckCache @Inject constructor(
        val firestore: FirebaseFirestore
) : DeckCache {


    override fun getDecks(): Observable<List<Deck>> {
        return Observable.create({ emitter ->
            val collection = firestore.collection(COLLECTION_DECKS)
            val registration = collection.addSnapshotListener { snapshot, exception ->

                if (exception != null) {
                    emitter.onError(exception)
                }

                val decks = ArrayList<Deck>()
                snapshot.forEach { document ->
                    val deck = document.toObject(DeckEntity::class.java)
                    decks.add(EntityMapper.to(deck, document.id))
                }

                emitter.onNext(decks)
            }

            emitter.setCancellable {
                registration.remove()
            }

        })
    }


    override fun putDeck(id: String?, cards: List<PokemonCard>, name: String, description: String?): Observable<Deck> {
        val newDeck = Deck(id ?: "", name, description ?: "", cards, System.currentTimeMillis())
        val model = EntityMapper.to(newDeck)
        if (id == null) {
            val task = firestore.collection(COLLECTION_DECKS)
                    .add(model)
            return RxFirebase.from(task)
                    .map { newDeck.copy(id = it.id) }
        }
        else {
            val task = firestore.collection(COLLECTION_DECKS)
                    .document(id)
                    .set(model)
            return RxFirebase.fromVoid(task)
                    .map { newDeck }
        }
    }


    override fun deleteDeck(deck: Deck): Observable<Unit> {
        val task = firestore.collection(COLLECTION_DECKS)
                .document(deck.id)
                .delete()

        return RxFirebase.fromVoid(task)
    }


    companion object {
        @JvmField val COLLECTION_DECKS = "decks"
    }
}