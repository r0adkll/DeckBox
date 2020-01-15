package com.r0adkll.deckbuilder.arch.data.features.editing.source

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code.NOT_FOUND
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.decks.cache.FirestoreDeckCache
import com.r0adkll.deckbuilder.arch.data.features.decks.mapper.EntityMapper
import com.r0adkll.deckbuilder.arch.data.features.decks.model.DeckEntity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.util.AppSchedulers
import com.r0adkll.deckbuilder.util.RxFirebase.asObservable
import com.r0adkll.deckbuilder.util.RxFirebase.asVoidObservable
import com.r0adkll.deckbuilder.util.stack
import io.reactivex.Observable

class FirestoreEditSource(
    val preferences: AppPreferences,
    val schedulers: AppSchedulers
) : EditSource {

    override fun startSession(imports: List<PokemonCard>?): Observable<Deck> {
        return getUserDeckCollection()?.let { collection ->
            val newDeck = DeckEntity(
                cardMetadata = imports?.stack()?.map { EntityMapper.to(it) },
                updatedAt = Timestamp.now()
            )

            collection.add(newDeck)
                .asObservable(schedulers.firebaseExecutor)
                .map {
                    Deck(
                        it.id,
                        "",
                        "",
                        null,
                        false,
                        imports ?: emptyList(),
                        false,
                        System.currentTimeMillis()
                    )
                }
        } ?: handleUserAuthException()
    }

    override fun changeName(deckId: String, name: String): Observable<String> {
        return getUserDeckCollection()?.let { collection ->
            collection.document(deckId)
                .update("name", name)
                .asVoidObservable(schedulers.firebaseExecutor)
                .map { name }
        } ?: handleUserAuthException()
    }

    override fun changeDescription(deckId: String, description: String): Observable<String> {
        return getUserDeckCollection()?.let { collection ->
            collection.document(deckId)
                .update("description", description)
                .asVoidObservable(schedulers.firebaseExecutor)
                .map { description }
        } ?: handleUserAuthException()
    }

    override fun changeDeckImage(deckId: String, image: DeckImage): Observable<Unit> {
        return getUserDeckCollection()
            ?.document(deckId)
            ?.update("image", image.uri.toString())
            ?.asVoidObservable(schedulers.firebaseExecutor)
            ?: handleUserAuthException()
    }

    override fun changeCollectionOnly(deckId: String, collectionOnly: Boolean): Observable<Unit> {
        return getUserDeckCollection()
            ?.document(deckId)
            ?.update("collectionOnly", collectionOnly)
            ?.asVoidObservable(schedulers.firebaseExecutor)
            ?: handleUserAuthException()
    }

    override fun addCards(deckId: String, cards: List<PokemonCard>): Observable<Unit> {
        return getUserDeckCollection()?.let { collection ->
            val stackedCards = cards.stack()
            val deckDocRef = collection.document(deckId)

            val transactionTask = Firebase.firestore.runTransaction { transaction ->
                val deck = transaction.get(deckDocRef).toObject(DeckEntity::class.java)
                if (deck != null) {
                    val metadataToUpdate = deck.cardMetadata?.map { cardMetadata ->
                        cardMetadata.count = stackedCards.find {
                            it.card.id == cardMetadata.id
                        }?.count?.plus(cardMetadata.count) ?: cardMetadata.count
                        cardMetadata
                    } ?: emptyList()

                    val metadataToAdd = stackedCards.filter { stackedCard ->
                        deck.cardMetadata?.none { it.id == stackedCard.card.id } != false
                    }.map {
                        EntityMapper.to(it)
                    }

                    // Update the card metadata, and updatedAt
                    transaction
                        .update(deckDocRef, "cardMetadata", metadataToUpdate.plus(metadataToAdd))
                        .update(deckDocRef, "updatedAt", Timestamp.now())

                    Unit
                } else {
                    throw FirebaseFirestoreException("No deck found for id $deckId", NOT_FOUND)
                }
            }

            transactionTask.asObservable(schedulers.firebaseExecutor)
        } ?: handleUserAuthException()
    }

    override fun removeCard(deckId: String, card: PokemonCard): Observable<Unit> {
        return getUserDeckCollection()?.let { collection ->
            val deckDocRef = collection.document(deckId)

            val transactionTask = Firebase.firestore.runTransaction { transaction ->
                val deck = transaction.get(deckDocRef).toObject(DeckEntity::class.java)
                if (deck != null) {
                    val metadataToUpdate = deck.cardMetadata
                        ?.map { cardMetadata ->
                            cardMetadata.count = if (cardMetadata.id == card.id) {
                                cardMetadata.count - 1
                            } else {
                                cardMetadata.count
                            }
                            cardMetadata
                        }
                        ?.filter { it.count > 0 }
                        ?: emptyList()

                    // Update the card metadata, and updatedAt
                    transaction
                        .update(deckDocRef, "cardMetadata", metadataToUpdate)
                        .update(deckDocRef, "updatedAt", Timestamp.now())

                    Unit
                } else {
                    throw FirebaseFirestoreException("No deck found for id $deckId", NOT_FOUND)
                }
            }

            transactionTask.asObservable(schedulers.firebaseExecutor)
        } ?: handleUserAuthException()
    }

    private fun getUserDeckCollection(): CollectionReference? {
        val user = FirebaseAuth.getInstance().currentUser
        val db = Firebase.firestore

        return user?.let { u ->
            db.collection(FirestoreDeckCache.COLLECTION_USERS)
                .document(preferences.testUserId ?: u.uid)
                .collection(FirestoreDeckCache.COLLECTION_DECKS)
        } ?: preferences.deviceId?.let { dId ->
            db.collection(FirestoreDeckCache.COLLECTION_OFFLINE_USERS)
                .document(dId)
                .collection(FirestoreDeckCache.COLLECTION_DECKS)
        }
    }

    private fun <T> handleUserAuthException(): Observable<T> {
        return Observable.error<T>(FirebaseAuthException("-1", "No current user logged in"))
    }
}
