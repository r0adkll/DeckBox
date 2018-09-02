package com.r0adkll.deckbuilder.arch.ui.features.playtest

import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import io.reactivex.Scheduler


class PlaytestRenderer(
        actions: PlaytestUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : UiBaseStateRenderer<PlaytestUi.State, PlaytestUi.State.Change, PlaytestUi.Actions>(actions, main, comp) {

    override fun onStart() {

    }
}