package com.r0adkll.deckbuilder.internal.di

@Deprecated("Move to use HasComponent from 52Kit Arch lib")
interface HasComponent<out C> {
    fun getComponent(): C
}
