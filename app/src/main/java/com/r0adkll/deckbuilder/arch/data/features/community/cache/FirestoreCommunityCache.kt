package com.r0adkll.deckbuilder.arch.data.features.community.cache

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.r0adkll.deckbuilder.arch.data.features.community.mapper.EntityMapper
import com.r0adkll.deckbuilder.arch.data.features.community.model.FirebaseEntity
import com.r0adkll.deckbuilder.arch.data.features.community.model.ThemeDeckTemplateEntity
import com.r0adkll.deckbuilder.arch.data.features.community.model.TournamentDeckTemplateEntity
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.community.model.DeckTemplate
import com.r0adkll.deckbuilder.util.RxFirebase
import com.r0adkll.deckbuilder.util.Schedulers
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import timber.log.Timber
import javax.inject.Inject
import kotlin.reflect.KClass


class FirestoreCommunityCache @Inject constructor(
        val cardRepository: CardRepository,
        val schedulers: Schedulers
) : CommunityCache {

    override fun getDeckTemplates(): Observable<List<DeckTemplate>> {
        val tournaments = getTemplateCollection(TOURNAMENTS)
        val themes = getTemplateCollection(THEMES)

        val query = tournaments.whereEqualTo("rank", 1)
        val tournamentTemplates = getCollectionItems(query, TournamentDeckTemplateEntity::class)
                .flatMap { decks ->
                    val cardIds = decks.flatMap {
                        it.cardMetadata!!.map { it.id }
                    }.toHashSet() // Convert to set so we don't request duplicate id's

                    cardRepository.find(cardIds.toList())
                            .map { cards ->
                                decks.sortedByDescending { it.timestamp }.map {
                                    EntityMapper.to(it, cards)
                                }
                            }
                }

//        val themeTemplates = getCollectionItems(themes, ThemeDeckTemplateEntity::class)
//                .flatMap { decks ->
//                    val cardIds = decks.flatMap {
//                        it.cardMetadata!!.map { it.id }
//                    }.toHashSet() // Convert to set so we don't request duplicate id's
//
//                    cardRepository.find(cardIds.toList())
//                            .map { cards ->
//                                decks.map { EntityMapper.to(it, cards) }
//                            }
//                }

        return tournamentTemplates //Observable.merge(tournamentTemplates, themeTemplates)
    }


    private fun getTemplateCollection(type: String): CollectionReference {
        val db = FirebaseFirestore.getInstance()
        return db.collection(TEMPLATES)
                .document(type)
                .collection(TEMPLATE_DECKS)
    }


    private fun <T : FirebaseEntity> getCollectionItems(query: Query, clazz: KClass<T>): Observable<List<T>> {
        return RxFirebase.from(query.get()/*, schedulers.firebaseExecutor*/)
                .map { snapshot ->
                    Timber.d("Firebase::getTemplates() - Thread(${Thread.currentThread().name})")
                    if (!snapshot.isEmpty) {
                        val items = ArrayList<T>()
                        snapshot.documents.forEach { document ->
                            document.toObject(clazz.java)?.let { entity ->
                                entity.id = document.id
                                items += entity
                            }
                        }
                        items
                    } else {
                        emptyList<T>()
                    }
                }
    }


    companion object {
        const val TEMPLATES = "templates"
        const val TOURNAMENTS = "tournaments"
        const val THEMES = "themes"
        const val TEMPLATE_DECKS = "decks"
    }
}