package app.deckbox.sharing

import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.ShareAction
import app.deckbox.features.decks.api.export.DeckExporter
import app.deckbox.sharing.api.ShareManager
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import me.tatarka.inject.annotations.Inject

@ContributesBinding(MergeActivityScope::class)
@Inject
class DesktopShareManager(
  private val deckExporter: DeckExporter,
) : ShareManager {

  override suspend fun share(shareAction: ShareAction) {
    TODO("Not yet implemented")
  }
}
