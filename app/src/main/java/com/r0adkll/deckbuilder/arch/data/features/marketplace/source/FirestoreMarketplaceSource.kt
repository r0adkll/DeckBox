package com.r0adkll.deckbuilder.arch.data.features.marketplace.source

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import com.r0adkll.deckbuilder.arch.data.features.marketplace.mapper.EntityMapper
import com.r0adkll.deckbuilder.arch.data.features.marketplace.model.ProductEntity
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product
import com.r0adkll.deckbuilder.util.Schedulers
import io.reactivex.Observable
import com.r0adkll.deckbuilder.util.RxFirebase.asObservable

class FirestoreMarketplaceSource(
        private val source: MarketplaceSource.Source,
        private val schedulers: Schedulers
) : MarketplaceSource {

    override fun getPrice(cardId: String): Observable<List<Product>> {
        val query = FirebaseFirestore.getInstance()
                .collectionGroup("prices")
                .whereEqualTo("cardId", cardId)
                .orderBy("updatedAt", Query.Direction.DESCENDING)

        return query.get(source.firestoreSource)
                .asObservable(schedulers.firebaseExecutor)
                .map { snapshot ->
                    snapshot.toObjects(ProductEntity::class.java)
                            .map { EntityMapper.from(it) }
                }
    }

    override fun getPrices(cardIds: List<String>): Observable<Map<String, Product>> {
        val queries = cardIds.map { cardId ->
            FirebaseFirestore.getInstance()
                    .collectionGroup("prices")
                    .whereEqualTo("cardId", cardId)
                    .orderBy("updatedAt", Query.Direction.DESCENDING)
                    .limit(1L)
                    .get(source.firestoreSource)
        }

        return Tasks.whenAllComplete(queries)
                .asObservable(schedulers.firebaseExecutor)
                .map { tasks ->
                    tasks
                            .map {
                                (it.result as QuerySnapshot).toObjects(ProductEntity::class.java)
                                        .map { EntityMapper.from(it) }
                            }
                            .flatten()
                            .map { it.cardId to it }
                            .toMap()
                }
    }

    private val MarketplaceSource.Source.firestoreSource: Source
        get() = when (this) {
            MarketplaceSource.Source.CACHE -> Source.CACHE
            MarketplaceSource.Source.NETWORK -> Source.SERVER
        }
}
