package com.r0adkll.deckbuilder.arch.ui.components

@Deprecated("Move to use 52Kit arch lib")
interface BaseActions {

    fun showLoading(isLoading: Boolean)
    fun showError(description: String)
    fun hideError()
}
