package app.deckbox.features.decks.api.validation

sealed interface Validation {

  data object Valid : Validation

  data class Invalid(
    val rule: String,
    val reason: String,
  ) : Validation
}

data class DeckValidation(
  val ruleValidations: List<Validation> = emptyList()
) {
  val isValid: Boolean
    get() = ruleValidations.all { it is Validation.Valid }
}
