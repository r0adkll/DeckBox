package com.r0adkll.deckbuilder

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Test

import org.junit.Assert.*

class ObservableThreadTest {

    @Test
    fun observableChainExecutesOnProperThreads() {
        Observable.fromArray(1, 2, 3, 4, 5)
                .subscribeOn(Schedulers.io())
                .doOnNext { System.out.println("IO::Thread: ${Thread.currentThread().name}") }
                .flatMap {
                    Observable.just("Dark Bacon Midnight")
                            .subscribeOn(Schedulers.computation())
                            .doOnNext { System.out.println("Computation::Thread: ${Thread.currentThread().name}") }
                }
                .doOnNext { System.out.println("Final::Thread: ${Thread.currentThread().name}") }
                .blockingFirst()

        System.out.println("Subscribe::Thread: ${Thread.currentThread().name}")
    }
}
