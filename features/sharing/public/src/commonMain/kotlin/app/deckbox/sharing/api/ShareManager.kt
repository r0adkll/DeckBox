package app.deckbox.sharing.api

import app.deckbox.core.model.ShareAction

interface ShareManager {

  suspend fun share(shareAction: ShareAction)
}
