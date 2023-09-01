package app.deckbox.decks.impl.validation

import app.deckbox.decks.impl.validation.rules.Rule
import app.deckbox.features.decks.api.validation.Validation

fun Rule.success(): Validation = Validation.Valid

fun Rule.invalid(reason: String): Validation = Validation.Invalid(name, reason)
