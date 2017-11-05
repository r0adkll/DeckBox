package com.r0adkll.deckbuilder.arch.ui.features.carddetail

import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailUi.State.Change
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailUi.State
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject


class CardDetailPresenter @Inject constructor(
        val ui: CardDetailUi,
        val repository: CardRepository
) : Presenter() {

    override fun start() {

        val loadVariants = repository.search(ui.state.card!!.supertype, ui.state.card!!.name)
                .map { it.filter { it.id != ui.state.card!!.id } }
                .map { Change.VariantsLoaded(it) as Change }

        val loadEvolves = ui.state.card!!.evolvesFrom?.let {
            repository.search(ui.state.card!!.supertype, it)
                    .map { Change.EvolvesFromLoaded(it) as Change }
        } ?: Observable.empty()


        val merged = loadVariants.mergeWith(loadEvolves)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .doOnNext { state -> Timber.v("    --- $state") }
                .subscribe { ui.render(it) }
    }
}