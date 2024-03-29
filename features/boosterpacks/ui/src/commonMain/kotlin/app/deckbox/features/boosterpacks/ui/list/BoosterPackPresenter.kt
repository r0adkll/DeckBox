package app.deckbox.features.boosterpacks.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import app.deckbox.common.screens.BoosterPackBuilderScreen
import app.deckbox.common.screens.BoosterPackScreen
import app.deckbox.common.screens.DeckBuilderScreen
import app.deckbox.common.screens.SettingsScreen
import app.deckbox.common.settings.DeckBoxSettings
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.settings.SortOption
import app.deckbox.core.settings.orderBoosterPacksBy
import app.deckbox.features.boosterpacks.api.BoosterPackRepository
import app.deckbox.features.decks.api.builder.DeckBuilderRepository
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, BoosterPackScreen::class)
@Inject
class BoosterPackPresenter(
  @Assisted private val navigator: Navigator,
  private val boosterPackRepository: BoosterPackRepository,
  private val deckBuilderRepository: DeckBuilderRepository,
  private val settings: DeckBoxSettings,
) : Presenter<BoosterPackUiState> {

  @Composable
  override fun present(): BoosterPackUiState {
    val boosterPackLoadState by remember {
      boosterPackRepository.observeBoosterPacks()
        .map { packs ->
          val sorted = packs.sortedBy { it.updatedAt }
          BoosterPackLoadState.Loaded(sorted)
        }
        .catch { BoosterPackLoadState.Error }
    }.collectAsState(BoosterPackLoadState.Loading)

    val packSortOrder by remember {
      settings.observeBoosterPackSortOrder()
    }.collectAsState(SortOption.UpdatedAt)

    val sortedPacks by remember {
      snapshotFlow {
        boosterPackLoadState.map {
          it.orderBoosterPacksBy(packSortOrder)
        }
      }
    }.collectAsState(BoosterPackLoadState.Loading)

    return BoosterPackUiState(
      sortOption = packSortOrder,
      packState = sortedPacks,
    ) { event ->
      when (event) {
        BoosterPackUiEvent.OpenAppSettings -> navigator.goTo(SettingsScreen())
        BoosterPackUiEvent.CreateNew -> navigator.goTo(BoosterPackBuilderScreen(boosterPackRepository.createSession()))
        BoosterPackUiEvent.NewDeck -> navigator.goTo(DeckBuilderScreen(deckBuilderRepository.createSession()))
        is BoosterPackUiEvent.BoosterPackClick -> navigator.goTo(BoosterPackBuilderScreen(event.pack.id))
        is BoosterPackUiEvent.Delete -> boosterPackRepository.delete(event.pack.id)
        is BoosterPackUiEvent.Duplicate -> boosterPackRepository.duplicate(event.pack.id)
        is BoosterPackUiEvent.AddToDeck -> deckBuilderRepository.addBoosterPack(event.deck.id, event.pack.id)
        is BoosterPackUiEvent.ChangeSortOption -> settings.boosterPackSortOrder = event.sortOption
      }
    }
  }
}
