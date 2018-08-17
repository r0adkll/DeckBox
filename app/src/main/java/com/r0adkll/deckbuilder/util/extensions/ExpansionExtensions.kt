package com.r0adkll.deckbuilder.util.extensions

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion


fun List<Expansion>.standard(): List<Expansion> = this.filter { it.standardLegal }
fun List<Expansion>.expanded(): List<Expansion> = this.filter { it.expandedLegal }