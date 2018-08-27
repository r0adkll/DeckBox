package com.r0adkll.deckbuilder.arch.ui.features.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseFragment
import io.reactivex.Observable
import javax.inject.Inject


class OverviewFragment : BaseFragment(), OverviewUi, OverviewUi.Intentions, OverviewUi.Actions {

    override var state: OverviewUi.State = OverviewUi.State.DEFAULT


    @Inject lateinit var presenter: OverviewPresenter
    @Inject lateinit var renderer: OverviewRenderer


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }


    override fun setupComponent() {

    }


    override fun render(state: OverviewUi.State) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addCards(): Observable<List<PokemonCard>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeCard(): Observable<PokemonCard> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCards(cards: List<EvolutionChain>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading(isLoading: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(description: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}