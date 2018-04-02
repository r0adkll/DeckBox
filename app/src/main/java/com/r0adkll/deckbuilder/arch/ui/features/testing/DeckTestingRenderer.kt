package com.r0adkll.deckbuilder.arch.ui.features.testing


import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class DeckTestingRenderer(
        actions: DeckTestingUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : UiBaseStateRenderer<DeckTestingUi.State, DeckTestingUi.State.Change>(actions, main, comp) {


    override fun onStart() {



    }
}