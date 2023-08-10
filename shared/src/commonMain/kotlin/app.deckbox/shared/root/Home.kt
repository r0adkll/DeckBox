package app.deckbox.shared.root

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.LocalWindowSizeClass
import app.deckbox.common.compose.PlatformBackHandler
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.icons.filled.Browse
import app.deckbox.common.compose.icons.filled.Collection
import app.deckbox.common.compose.icons.filled.Decks
import app.deckbox.common.compose.icons.outline.Browse
import app.deckbox.common.compose.icons.outline.Collection
import app.deckbox.common.compose.icons.outline.Decks
import app.deckbox.common.resources.strings.DeckBoxStrings
import app.deckbox.common.screens.BrowseScreen
import app.deckbox.common.screens.DecksScreen
import app.deckbox.common.screens.ExpansionsScreen
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.navigationBars
import com.moriatsushi.insetsx.safeContentPadding
import com.moriatsushi.insetsx.statusBars
import com.moriatsushi.insetsx.systemBars
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.foundation.screen
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.overlay.rememberOverlayHost
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Home(
  backstack: SaveableBackStack,
  navigator: Navigator,
  modifier: Modifier = Modifier,
) {
  val windowSizeClass = LocalWindowSizeClass.current
  val navigationType = remember(windowSizeClass) {
    NavigationType.forWindowSizeSize(windowSizeClass)
  }

  val rootScreen by remember {
    derivedStateOf { backstack.last().screen }
  }

  val strings = LocalStrings.current
  val navigationItems = remember { buildNavigationItems(strings) }

  val overlayHost = rememberOverlayHost()
  PlatformBackHandler(overlayHost.currentOverlayData != null) {
    overlayHost.currentOverlayData?.finish(Unit)
  }

  ContentWithOverlays(
    overlayHost = overlayHost,
  ) {
    Scaffold(
      bottomBar = {
        if (navigationType == NavigationType.BOTTOM_NAVIGATION) {
          HomeNavigationBar(
            selectedNavigation = rootScreen,
            navigationItems = navigationItems,
            onNavigationSelected = { navigator.resetRoot(it) },
            modifier = Modifier.fillMaxWidth(),
          )
        } else {
          Spacer(
            Modifier
              .windowInsetsBottomHeight(WindowInsets.navigationBars)
              .fillMaxWidth(),
          )
        }
      },
      // We let content handle the status bar
      contentWindowInsets = WindowInsets.systemBars.exclude(WindowInsets.statusBars),
      modifier = modifier,
    ) { paddingValues ->
      Row(
        modifier = Modifier
          .fillMaxSize()
          .then(
            if (navigationType != NavigationType.BOTTOM_NAVIGATION) Modifier.padding(paddingValues) else Modifier,
          ),
      ) {
        if (navigationType == NavigationType.RAIL) {
          HomeNavigationRail(
            selectedNavigation = rootScreen,
            navigationItems = navigationItems,
            onNavigationSelected = { navigator.resetRoot(it) },
            modifier = Modifier.fillMaxHeight(),
          )

          Divider(
            Modifier
              .fillMaxHeight()
              .width(1.dp),
          )
        } else if (navigationType == NavigationType.PERMANENT_DRAWER) {
          HomeNavigationDrawer(
            selectedNavigation = rootScreen,
            navigationItems = navigationItems,
            onNavigationSelected = { navigator.resetRoot(it) },
            modifier = Modifier.fillMaxHeight(),
          )
        }

        NavigableCircuitContentWithPrevious(
          navigator = navigator,
          backstack = backstack,
          decoration = GestureNavDecoration(navigator),
          modifier = Modifier
            .weight(1f)
            .fillMaxHeight(),
        )
      }
    }
  }
}

@Composable
private fun HomeNavigationBar(
  selectedNavigation: Screen,
  navigationItems: List<HomeNavigationItem>,
  onNavigationSelected: (Screen) -> Unit,
  modifier: Modifier = Modifier,
) {
  NavigationBar(
    modifier = modifier,
    windowInsets = WindowInsets.navigationBars,
  ) {
    for (item in navigationItems) {
      NavigationBarItem(
        icon = {
          HomeNavigationItemIcon(
            item = item,
            selected = selectedNavigation == item.screen,
          )
        },
        label = { Text(text = item.label) },
        selected = selectedNavigation == item.screen,
        onClick = { onNavigationSelected(item.screen) },
      )
    }
  }
}

@Composable
private fun HomeNavigationRail(
  selectedNavigation: Screen,
  navigationItems: List<HomeNavigationItem>,
  onNavigationSelected: (Screen) -> Unit,
  modifier: Modifier = Modifier,
) {
  NavigationRail(modifier = modifier) {
    for (item in navigationItems) {
      NavigationRailItem(
        icon = {
          HomeNavigationItemIcon(
            item = item,
            selected = selectedNavigation == item.screen,
          )
        },
        alwaysShowLabel = false,
        label = { Text(text = item.label) },
        selected = selectedNavigation == item.screen,
        onClick = { onNavigationSelected(item.screen) },
      )
    }
  }
}

@Composable
private fun HomeNavigationDrawer(
  selectedNavigation: Screen,
  navigationItems: List<HomeNavigationItem>,
  onNavigationSelected: (Screen) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .safeContentPadding()
      .padding(16.dp)
      .widthIn(max = 280.dp),
  ) {
    for (item in navigationItems) {
      @OptIn(ExperimentalMaterial3Api::class)
      NavigationDrawerItem(
        icon = {
          Icon(
            imageVector = item.iconImageVector,
            contentDescription = item.contentDescription,
          )
        },
        label = { Text(text = item.label) },
        selected = selectedNavigation == item.screen,
        onClick = { onNavigationSelected(item.screen) },
      )
    }
  }
}

@Composable
private fun HomeNavigationItemIcon(item: HomeNavigationItem, selected: Boolean) {
  if (item.selectedImageVector != null) {
    Crossfade(targetState = selected) { s ->
      Icon(
        imageVector = if (s) item.selectedImageVector else item.iconImageVector,
        contentDescription = item.contentDescription,
      )
    }
  } else {
    Icon(
      imageVector = item.iconImageVector,
      contentDescription = item.contentDescription,
    )
  }
}

@Immutable
private data class HomeNavigationItem(
  val screen: Screen,
  val label: String,
  val contentDescription: String,
  val iconImageVector: ImageVector,
  val selectedImageVector: ImageVector? = null,
)

internal enum class NavigationType {
  BOTTOM_NAVIGATION,
  RAIL,
  PERMANENT_DRAWER,
  ;

  companion object {
    fun forWindowSizeSize(windowSizeClass: WindowSizeClass): NavigationType = when {
      windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact -> BOTTOM_NAVIGATION
      windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact -> BOTTOM_NAVIGATION
      windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium -> RAIL
      else -> PERMANENT_DRAWER
    }
  }
}

private fun buildNavigationItems(strings: DeckBoxStrings): List<HomeNavigationItem> {
  return listOf(
    HomeNavigationItem(
      screen = DecksScreen(),
      label = strings.decks,
      contentDescription = strings.decksTabContentDescription,
      iconImageVector = DeckBoxIcons.Outline.Decks,
      selectedImageVector = DeckBoxIcons.Filled.Decks,
    ),
    HomeNavigationItem(
      screen = ExpansionsScreen(),
      label = strings.expansions,
      contentDescription = strings.expansionsTabContentDescription,
      iconImageVector = DeckBoxIcons.Outline.Collection,
      selectedImageVector = DeckBoxIcons.Filled.Collection,
    ),
    HomeNavigationItem(
      screen = BrowseScreen(),
      label = strings.browse,
      contentDescription = strings.browseTabContentDescription,
      iconImageVector = DeckBoxIcons.Outline.Browse,
      selectedImageVector = DeckBoxIcons.Filled.Browse,
    ),
  )
}
