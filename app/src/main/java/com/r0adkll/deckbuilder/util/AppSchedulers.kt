package com.r0adkll.deckbuilder.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.Executor

class AppSchedulers(
        val main: Scheduler,
        val disk: Scheduler,
        val comp: Scheduler,
        val network: Scheduler,
        val firebaseExecutor: Executor,
        val databaseExecutor: Executor
) {

    val firebase: Scheduler = io.reactivex.schedulers.Schedulers.from(firebaseExecutor)
    val database: Scheduler = io.reactivex.schedulers.Schedulers.from(databaseExecutor)

    companion object {
        fun createTrampoline(useMain: Boolean = false): AppSchedulers {
            return AppSchedulers(
                    main = if (useMain) AndroidSchedulers.mainThread() else io.reactivex.schedulers.Schedulers.trampoline(),
                    disk = io.reactivex.schedulers.Schedulers.trampoline(),
                    comp = io.reactivex.schedulers.Schedulers.trampoline(),
                    network = io.reactivex.schedulers.Schedulers.trampoline(),
                    firebaseExecutor = MainThreadExecutor(),
                    databaseExecutor = MainThreadExecutor()
            )
        }
    }
}
