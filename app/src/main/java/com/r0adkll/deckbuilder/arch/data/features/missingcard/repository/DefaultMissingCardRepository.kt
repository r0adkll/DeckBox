package com.r0adkll.deckbuilder.arch.data.features.missingcard.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.r0adkll.deckbuilder.arch.data.features.missingcard.mapper.EntityMapper
import com.r0adkll.deckbuilder.arch.domain.features.missingcard.model.MissingCard
import com.r0adkll.deckbuilder.arch.domain.features.missingcard.repository.MissingCardRepository
import com.r0adkll.deckbuilder.util.RxFirebase
import io.reactivex.Observable
import javax.inject.Inject


class DefaultMissingCardRepository @Inject constructor(
    val firestore: FirebaseFirestore
) : MissingCardRepository {

    override fun reportMissingCard(missingCard: MissingCard): Observable<Unit> {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.let { u ->
            val entity = EntityMapper.to(missingCard)
            entity.userId = u.uid
            val collection = firestore.collection(COLLECTION_MISSING_CARDS)
            val task = collection.add(entity)
            RxFirebase.from(task).map { Unit }
        } ?: Observable.error(FirebaseAuthException("-1", "No current user logged in"))
    }


    companion object {
        private val COLLECTION_MISSING_CARDS = "missing_cards"
    }
}