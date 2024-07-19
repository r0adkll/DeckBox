package app.deckbox.playtest.api

import app.deckbox.playtest.api.model.Action
import app.deckbox.playtest.api.model.Board
import kotlinx.coroutines.flow.StateFlow

interface PlayTestRepository {

  /**
   * Create a new play test session. Creating, caching, updating the play test game
   */
  fun newSession(): PlaySessionId

  /**
   * Observe a given session, getting updates as you take actions against the current board
   */
  fun observeSession(sessionId: PlaySessionId): StateFlow<Board>

  /**
   * Apply an action to the current session
   */
  fun applyAction(sessionId: PlaySessionId, action: Action)
}
