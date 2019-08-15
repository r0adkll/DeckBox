package com.r0adkll.deckbuilder.arch.ui.components.palette.actions

interface ExpansionAction {

    val code: String
    fun applyColor(color: Int, isLight: Boolean)
}