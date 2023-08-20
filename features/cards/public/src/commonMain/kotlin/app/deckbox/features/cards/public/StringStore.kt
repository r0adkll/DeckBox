package app.deckbox.features.cards.public

interface StringStore {

  suspend fun get(): List<String>
}

interface RarityStore : StringStore
interface SubtypeStore : StringStore
