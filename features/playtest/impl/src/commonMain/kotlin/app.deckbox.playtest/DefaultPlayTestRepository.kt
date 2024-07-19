package app.deckbox.playtest

import app.deckbox.core.di.AppScope
import app.deckbox.core.di.MergeAppScope
import app.deckbox.playtest.api.PlaySessionId
import app.deckbox.playtest.api.PlayTestRepository
import app.deckbox.playtest.api.model.Action
import app.deckbox.playtest.api.model.Board
import app.deckbox.playtest.cache.GameCache
import app.deckbox.playtest.db.GameDao
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.flow.StateFlow
import me.tatarka.inject.annotations.Inject

@AppScope
@ContributesBinding(MergeAppScope::class)
@Inject
class DefaultPlayTestRepository(
  private val db: GameDao,
  private val cache: GameCache,
) : PlayTestRepository {

  override fun newSession(): PlaySessionId {
    TODO("Not yet implemented")
  }

  override fun observeSession(sessionId: PlaySessionId): StateFlow<Board> {
    TODO("Not yet implemented")
  }

  override fun applyAction(sessionId: PlaySessionId, action: Action) {
    TODO("Not yet implemented")
  }
}
