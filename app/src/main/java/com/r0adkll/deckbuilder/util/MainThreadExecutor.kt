package com.r0adkll.deckbuilder.util

import java.util.concurrent.Executor


class MainThreadExecutor : Executor {

    override fun execute(p0: Runnable?) {
        p0?.run()
    }
}