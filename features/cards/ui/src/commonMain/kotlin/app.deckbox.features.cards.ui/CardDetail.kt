package app.deckbox.features.cards.ui

import DeckBoxAppBar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import app.deckbox.common.compose.extensions.applyHoloAndDragEffect
import app.deckbox.common.compose.icons.Bulbasaur
import app.deckbox.common.compose.icons.Charmander
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.icons.Squirtle
import app.deckbox.common.compose.icons.rounded.AddCard
import app.deckbox.common.compose.icons.rounded.AddDeck
import app.deckbox.common.compose.icons.rounded.SubtractCard
import app.deckbox.common.compose.overlays.showBottomSheetScreen
import app.deckbox.common.compose.theme.PokemonTypeColor.toBackgroundColor
import app.deckbox.common.compose.widgets.CardAspectRatio
import app.deckbox.common.compose.widgets.ContentLoadingSize
import app.deckbox.common.compose.widgets.OutlinedIconButton
import app.deckbox.common.compose.widgets.SizedIcon
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.common.screens.DeckPickerScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Deck
import app.deckbox.core.model.SuperType
import app.deckbox.features.cards.ui.CardDetailUiEvent.AddToDeck
import app.deckbox.features.cards.ui.CardDetailUiEvent.CardClick
import app.deckbox.features.cards.ui.CardDetailUiEvent.DecrementCount
import app.deckbox.features.cards.ui.CardDetailUiEvent.IncrementCount
import app.deckbox.features.cards.ui.CardDetailUiEvent.NavigateBack
import app.deckbox.features.cards.ui.CardDetailUiEvent.OpenUrl
import app.deckbox.features.cards.ui.composables.CardMarketPriceCard
import app.deckbox.features.cards.ui.composables.InfoCard
import app.deckbox.features.cards.ui.composables.RelatedCards
import app.deckbox.features.cards.ui.composables.TcgPlayerPriceCard
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.navigationBars
import com.moriatsushi.insetsx.systemBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(MergeActivityScope::class, CardDetailScreen::class)
@Composable
internal fun CardDetail(
  state: CardDetailUiState,
  modifier: Modifier = Modifier,
) {
  val coroutineScope = rememberCoroutineScope()
  val overlayHost = LocalOverlayHost.current
  val eventSink = state.eventSink

  val snackbarHostState = remember { SnackbarHostState() }

  val dismissSnackbarState = rememberDismissState(
    confirmValueChange = { value ->
      if (value != DismissValue.Default) {
        snackbarHostState.currentSnackbarData?.dismiss()
        true
      } else {
        false
      }
    }
  )

  state.snackbarMessage?.let { message ->
    LaunchedEffect(message) {
      snackbarHostState.showSnackbar(message)
      // Notify the view model that the message has been dismissed
      eventSink(CardDetailUiEvent.ClearSnackBar)
    }
  }

  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
  Scaffold(
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      DeckBoxAppBar(
        title = state.deckState?.let {
          LocalStrings.current.cardCountInDeck(it.count)
        } ?: "",
        navigationIcon = {
          IconButton(
            onClick = { eventSink(NavigateBack) },
          ) {
            Icon(Icons.Rounded.ArrowBack, contentDescription = null)
          }
        },
        actions = {
          state.deckState?.let { deckState ->
            IconButton(
              enabled = deckState.count > 0,
              onClick = { eventSink(DecrementCount) },
            ) {
              Icon(
                Icons.Rounded.SubtractCard,
                contentDescription = null,
              )
            }
            IconButton(
              onClick = { eventSink(IncrementCount) },
            ) {
              Icon(
                Icons.Rounded.AddCard,
                contentDescription = null,
              )
            }
          } ?: run {
            OutlinedIconButton(
              icon = { SizedIcon(Icons.Rounded.AddDeck, contentDescription = null,) },
              label = { Text(LocalStrings.current.cardDetailAddToDeck) },
              colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary,
              ),
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
              onClick = {
                coroutineScope.launch {
                  val result = overlayHost.showBottomSheetScreen<Deck>(DeckPickerScreen())
                  if (result != null) {
                    eventSink(AddToDeck(result))
                  }
                }
              },
              modifier = Modifier.padding(horizontal = 16.dp)
            )
          }
        },
        scrollBehavior = scrollBehavior,
      )
    },
    floatingActionButton = {
      val isFavorited = state.isFavorited
      FloatingActionButton(
        onClick = {
          eventSink(CardDetailUiEvent.Favorite(!isFavorited))
        },
        containerColor = if (isFavorited) {
          MaterialTheme.colorScheme.tertiaryContainer
        } else {
          MaterialTheme.colorScheme.surfaceVariant
        },
        contentColor = if (isFavorited) {
          MaterialTheme.colorScheme.tertiary
        } else {
          MaterialTheme.colorScheme.onSurfaceVariant
        },
      ) {
        Icon(
          if (isFavorited) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
          contentDescription = null,
        )
      }
    },
    snackbarHost = {
      SnackbarHost(hostState = snackbarHostState) { data ->
        SwipeToDismiss(
          state = dismissSnackbarState,
          background = {},
          dismissContent = { Snackbar(snackbarData = data) },
          modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        )
      }
    },
    contentWindowInsets = WindowInsets.systemBars.exclude(WindowInsets.navigationBars),
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .padding(paddingValues)
        .verticalScroll(rememberScrollState()),
    ) {
      CardImage(
        url = state.cardImageUrl,
        loadingContainerColor = state.pokemonCard?.types?.firstOrNull()?.toBackgroundColor() ?: Color.Unspecified,
        contentDescription = state.cardName,
        modifier = Modifier
          .fillMaxWidth()
          .padding(
            horizontal = 32.dp,
          )
          .zIndex(2f)
          .applyHoloAndDragEffect(),
      )

      Spacer(Modifier.height(16.dp))

      InfoCard(
        name = state.cardName,
        card = state.pokemonCard,
        modifier = Modifier.padding(horizontal = 16.dp),
      )

      state.pokemonCard?.tcgPlayer?.let { tcgPlayer ->
        Spacer(Modifier.height(16.dp))
        TcgPlayerPriceCard(
          tcgPlayer = tcgPlayer,
          onBuyClick = { eventSink(OpenUrl(tcgPlayer.url)) },
          modifier = Modifier.padding(horizontal = 16.dp),
        )
      }

      state.pokemonCard?.cardMarket?.let { cardMarket ->
        Spacer(Modifier.height(16.dp))
        CardMarketPriceCard(
          cardMarket = cardMarket,
          onBuyClick = { eventSink(OpenUrl(cardMarket.url)) },
          modifier = Modifier.padding(horizontal = 16.dp),
        )
      }

      Spacer(Modifier.height(16.dp))

      RelatedCards(
        loadState = state.similar,
        title = { Text(LocalStrings.current.similarCardsLabel) },
        errorLabel = { Text(LocalStrings.current.similarCardsErrorLabel) },
        emptyLabel = { Text(LocalStrings.current.similarCardsEmptyLabel) },
        emptyImage = DeckBoxIcons.Charmander,
        onCardClick = { eventSink(CardClick(it)) },
      )

      Spacer(Modifier.height(16.dp))

      if (state.pokemonCard?.supertype == SuperType.POKEMON) {
        if (state.pokemonCard?.subtypes?.contains("Basic") != true) {
          RelatedCards(
            loadState = state.evolvesFrom,
            title = { Text(LocalStrings.current.evolvesFromLabel) },
            errorLabel = { Text(LocalStrings.current.evolvesFromErrorLabel) },
            emptyLabel = { Text(LocalStrings.current.evolvesFromEmptyLabel) },
            emptyImage = DeckBoxIcons.Squirtle,
            onCardClick = { eventSink(CardClick(it)) },
          )

          Spacer(Modifier.height(16.dp))
        }

        RelatedCards(
          loadState = state.evolvesTo,
          title = { Text(LocalStrings.current.evolvesToLabel) },
          errorLabel = { Text(LocalStrings.current.evolvesToErrorLabel) },
          emptyLabel = { Text(LocalStrings.current.evolvesToEmptyLabel) },
          emptyImage = DeckBoxIcons.Bulbasaur,
          onCardClick = { eventSink(CardClick(it)) },
        )
      }

      Spacer(Modifier.height(88.dp))
    }
  }
}

@Composable
private fun CardImage(
  url: String,
  contentDescription: String,
  modifier: Modifier = Modifier,
  loadingContainerColor: Color = Color.Unspecified,
) {
  val imageAction by key(url) { rememberImageAction(url) }
  Box(
    modifier = modifier,
  ) {
    Image(
      painter = rememberImageActionPainter(imageAction),
      contentDescription = contentDescription,
      contentScale = ContentScale.FillWidth,
      modifier = Modifier.fillMaxWidth(),
    )

    if (imageAction is ImageEvent) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(CardAspectRatio)
          .background(
            color = if (loadingContainerColor == Color.Unspecified) {
              MaterialTheme.colorScheme.secondaryContainer
            } else {
              loadingContainerColor
            },
            shape = RoundedCornerShape(32.dp),
          ),
      ) {
        SpinningPokeballLoadingIndicator(
          size = ContentLoadingSize,
          modifier = Modifier.align(Alignment.Center),
        )
      }
    }
  }
}
