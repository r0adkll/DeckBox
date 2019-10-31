package com.r0adkll.deckbuilder.internal.di

interface HasComponent<out C> {
    fun getComponent(): C
}
