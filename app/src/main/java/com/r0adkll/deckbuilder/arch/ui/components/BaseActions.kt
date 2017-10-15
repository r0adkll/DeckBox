package com.r0adkll.deckbuilder.arch.ui.components


interface BaseActions {

    fun showLoading(isLoading: Boolean)
    fun showError(description: String)
    fun hideError()
}