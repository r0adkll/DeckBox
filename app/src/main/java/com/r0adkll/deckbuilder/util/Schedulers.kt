package com.r0adkll.deckbuilder.util


import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers


class Schedulers(
        val main: Scheduler,
        val disk: Scheduler,
        val comp: Scheduler,
        val network: Scheduler
) {
    companion object {
        fun createTrampoline(useMain: Boolean = false): Schedulers {
            return Schedulers(
                    main = if (useMain) AndroidSchedulers.mainThread() else io.reactivex.schedulers.Schedulers.trampoline(),
                    disk = io.reactivex.schedulers.Schedulers.trampoline(),
                    comp = io.reactivex.schedulers.Schedulers.trampoline(),
                    network = io.reactivex.schedulers.Schedulers.trampoline()
            )
        }
    }
}