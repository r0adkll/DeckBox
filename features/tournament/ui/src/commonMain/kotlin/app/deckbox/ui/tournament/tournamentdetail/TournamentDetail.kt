package app.deckbox.ui.tournament.tournamentdetail

import DeckBoxAppBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.icons.Snorlax
import app.deckbox.common.compose.widgets.ContentLoadingSize
import app.deckbox.common.compose.widgets.DefaultIconSize
import app.deckbox.common.compose.widgets.EmptyView
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.common.screens.TournamentScreen
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.tournament.api.model.Participant
import app.deckbox.ui.tournament.tournamentdetail.composables.ParticipantListItem
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.navigationBars
import com.moriatsushi.insetsx.systemBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(MergeActivityScope::class, TournamentScreen::class)
@Composable
fun TournamentDetail(
  state: TournamentDetailUiState,
  modifier: Modifier = Modifier,
) {
  val eventSink = state.eventSink

  val lazyListState = rememberLazyListState()
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

  Scaffold(
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    contentWindowInsets = WindowInsets.systemBars.exclude(WindowInsets.navigationBars),
    topBar = {
      DeckBoxAppBar(
        title = state.tournamentName,
        navigationIcon = {
          IconButton(
            onClick = { eventSink(TournamentDetailUiEvent.NavigateBack) },
          ) {
            Icon(
              Icons.AutoMirrored.Rounded.ArrowBack,
              contentDescription = null,
            )
          }
        },
        scrollBehavior = scrollBehavior,
      )
    },
  ) { paddingValues ->
    when (val participantsState = state.participants) {
      LoadState.Loading -> LoadingContent(Modifier.padding(paddingValues))
      LoadState.Error -> ErrorContent(Modifier.padding(paddingValues))
      is LoadState.Loaded -> LoadedContent(
        participants = participantsState.data,
        onParticipantClick = { participant ->
          eventSink(TournamentDetailUiEvent.ParticipantClick(participant))
        },
        state = lazyListState,
        paddingValues = paddingValues,
      )
    }
  }
}

@Composable
private fun LoadingContent(
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    SpinningPokeballLoadingIndicator(
      size = ContentLoadingSize,
    )
  }
}

@Composable
private fun ErrorContent(
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(64.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Icon(
      Icons.Outlined.Error,
      contentDescription = null,
    )
    Spacer(Modifier.height(16.dp))
    Text(LocalStrings.current.tournamentsErrorMessage)
  }
}

@Composable
private fun EmptyState(
  modifier: Modifier = Modifier,
) {
  EmptyView(
    label = { Text(LocalStrings.current.genericEmptyCardsMessage) },
    image = {
      Image(
        DeckBoxIcons.Snorlax,
        contentDescription = null,
        modifier = Modifier.size(DefaultIconSize),
      )
    },
    modifier = modifier.fillMaxSize(),
  )
}

@Composable
private fun LoadedContent(
  participants: List<Participant>,
  onParticipantClick: (Participant) -> Unit,
  state: LazyListState,
  paddingValues: PaddingValues,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    state = state,
    contentPadding = paddingValues,
    modifier = modifier,
  ) {
    items(
      items = participants,
      key = { it.id },
    ) { participant ->
      ParticipantListItem(
        participant = participant,
        modifier = Modifier.clickable {
          onParticipantClick(participant)
        },
      )
    }
  }

  if (participants.isEmpty()) {
    EmptyState()
  }
}
