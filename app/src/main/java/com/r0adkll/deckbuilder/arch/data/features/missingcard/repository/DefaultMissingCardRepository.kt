package com.r0adkll.deckbuilder.arch.data.features.missingcard.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.missingcard.mapper.EntityMapper
import com.r0adkll.deckbuilder.arch.domain.features.missingcard.model.MissingCard
import com.r0adkll.deckbuilder.arch.domain.features.missingcard.repository.MissingCardRepository
import com.r0adkll.deckbuilder.util.RxFirebase
import io.reactivex.Observable
import javax.inject.Inject


class DefaultMissingCardRepository @Inject constructor(
    val firestore: FirebaseFirestore,
    val preferences: AppPreferences
) : MissingCardRepository {

    override fun reportMissingCard(missingCard: MissingCard): Observable<Unit> {
        val entity = EntityMapper.to(missingCard)
        val user = FirebaseAuth.getInstance().currentUser

        user?.let { u ->
            entity.userId = u.uid
        } ?: preferences.deviceId?.let {
            entity.userId = it
        }

        val collection = firestore.collection(COLLECTION_MISSING_CARDS)
        val task = collection.add(entity)
        return RxFirebase.from(task).map { Unit }
    }


    companion object {
        private val COLLECTION_MISSING_CARDS = "missing_cards"
    }
}