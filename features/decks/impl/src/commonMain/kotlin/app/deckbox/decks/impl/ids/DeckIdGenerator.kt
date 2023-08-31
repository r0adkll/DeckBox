package app.deckbox.decks.impl.ids

import app.deckbox.core.di.MergeAppScope
import com.benasher44.uuid.uuid4
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import me.tatarka.inject.annotations.Inject

interface DeckIdGenerator {
  fun generate(): String
}

@ContributesBinding(MergeAppScope::class)
@Inject
class UuidDeckIdGenerator : DeckIdGenerator {

  override fun generate(): String {
    return uuid4().toString()
  }
}
