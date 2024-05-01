package app.deckbox.ui.expansions.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.deckbox.common.screens.ExpansionDetailScreen
import app.deckbox.common.screens.ExpansionsScreen
import app.deckbox.common.settings.DeckBoxSettings
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Collection
import app.deckbox.core.model.Expansion
import app.deckbox.core.model.collected
import app.deckbox.expansions.ExpansionsRepository
import app.deckbox.features.cards.public.CardRepository
import app.deckbox.features.collection.api.CollectionRepository
import app.deckbox.ui.expansions.list.extensions.collectExpansionCardStyle
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@CircuitInject(MergeActivityScope::class, ExpansionsScreen::class)
class ExpansionsPresenter(
  @Assisted private val navigator: Navigator,
  private val expansionsRepository: ExpansionsRepository,
  private val cardRepository: CardRepository,
  private val collectionRepository: CollectionRepository,
  private val settings: DeckBoxSettings,
) : Presenter<ExpansionsUiState> {

  @Composable
  override fun present(): ExpansionsUiState {
    val expansionsLoadState by remember {
      expansionsRepository.observeExpansions()
        .map { expansions ->
          val sorted = expansions.sortedByDescending { it.releaseDate }
          ExpansionsLoadState.Loaded(sorted)
        }
        .catch {
          ExpansionsLoadState.Error("Unable to load expansions")
        }
    }.collectAsState(ExpansionsLoadState.Loading)

    val collectionState by remember {
      collectionRepository.observeCollection()
    }.collectAsState(Collection.empty())

    val expansionCardStyle by settings.collectExpansionCardStyle()

    var searchQuery by rememberSaveable { mutableStateOf<String?>(null) }
    val filteredLoadState by remember {
      derivedStateOf {
        if (searchQuery.isNullOrBlank()) return@derivedStateOf expansionsLoadState
        when (val state = expansionsLoadState) {
          is ExpansionsLoadState.Loaded -> {
            state.copy(
              expansions = state.expansions.filter {
                it.name.contains(searchQuery!!, ignoreCase = true) ||
                  it.series.contains(searchQuery!!, ignoreCase = true) ||
                  it.ptcgoCode?.contains(searchQuery!!, ignoreCase = true) == true
              },
            )
          }
          else -> expansionsLoadState
        }
      }
    }

    val groupedExpansionState = when (val state = filteredLoadState) {
      ExpansionsLoadState.Loading -> ExpansionState.Loading
      is ExpansionsLoadState.Error -> ExpansionState.Error(state.message)
      is ExpansionsLoadState.Loaded -> ExpansionState.Loaded(
        state.expansions
          .groupBy { it.series }
          .mapValues { (_, expansions) ->
            expansions.map { expansion ->
              expansion.collected(collectionState[expansion.id])
            }
          }
          .map { (series, expansions) ->
            ExpansionSeries(series, expansions)
          }
          .sortedByDescending {
            it.expansions.sumOf { it.item.releaseDate.toEpochDays() } / it.expansions.size
          },
      )
    }

    val hasFavorites by remember {
      cardRepository.observeFavorites()
        .map { it.any { it.value } }
    }.collectAsState(false)

    return ExpansionsUiState(
      expansionState = groupedExpansionState,
      expansionCardStyle = expansionCardStyle,
      query = searchQuery,
      hasFavorites = hasFavorites,
    ) { event ->
      when (event) {
        is ExpansionsUiEvent.ChangeCardStyle -> settings.expansionCardStyle = event.style
        is ExpansionsUiEvent.ExpansionClicked -> {
          navigator.goTo(ExpansionDetailScreen(event.expansion.id))
        }
        is ExpansionsUiEvent.SearchUpdated -> searchQuery = event.query
        ExpansionsUiEvent.SearchCleared -> searchQuery = null
        ExpansionsUiEvent.FavoritesClick -> navigator.goTo(ExpansionDetailScreen(Expansion.FAVORITES))
      }
    }
  }
}

sealed interface ExpansionsLoadState {
  data object Loading : ExpansionsLoadState

  @Stable
  data class Loaded(val expansions: List<Expansion>) : ExpansionsLoadState

  @Stable
  data class Error(val message: String) : ExpansionsLoadState
}
