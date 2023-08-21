package app.deckbox.decks.impl

import app.deckbox.core.di.MergeAppScope
import app.deckbox.features.decks.api.DeckBuilderRepository
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import me.tatarka.inject.annotations.Inject

@ContributesBinding(MergeAppScope::class)
@Inject
class LocalDeckBuilderRepository : DeckBuilderRepository
