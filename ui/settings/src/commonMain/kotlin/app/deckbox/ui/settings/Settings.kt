package app.deckbox.ui.settings

import DeckBoxAppBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.LiveHelp
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.rounded.DataObject
import androidx.compose.material.icons.rounded.Merge
import androidx.compose.material.icons.rounded.NewReleases
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.icons.outline.Browse
import app.deckbox.common.compose.icons.outline.Collection
import app.deckbox.common.compose.icons.outline.Decks
import app.deckbox.common.screens.SettingsScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.ui.settings.composables.Header
import app.deckbox.ui.settings.composables.MenuDivider
import cafe.adriel.lyricist.LocalStrings
import com.r0adkll.kotlininject.merge.annotations.CircuitInject

@Suppress("UNUSED_PARAMETER")
@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(MergeActivityScope::class, SettingsScreen::class)
@Composable
fun Settings(
  state: SettingsUiState,
  modifier: Modifier = Modifier,
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
  Scaffold(
    topBar = {
      DeckBoxAppBar(
        title = LocalStrings.current.settings,
        navigationIcon = {
          IconButton(
            onClick = { },
          ) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null)
          }
        },
        actions = {
        },
        scrollBehavior = scrollBehavior,
      )
    },
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .padding(paddingValues)
        .verticalScroll(rememberScrollState()),
    ) {
      Header("Account")
      ListItem(
        headlineContent = {
          Text("test.example@email.com")
        },
        supportingContent = {
          Text("Premium User")
        },
        leadingContent = {
          Box(
            modifier = Modifier
              .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape,
              )
              .padding(8.dp),
            contentAlignment = Alignment.Center,
          ) {
            Icon(
              Icons.Rounded.Person,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
          }
        },
        trailingContent = {
          Icon(
            Icons.Rounded.NewReleases,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
          )
        },
      )

      MenuDivider()
      Header("Theme")
      ListItem(
        headlineContent = { Text("Deck card style") },
        supportingContent = { Text("Change the appearance of decks in the app") },
        leadingContent = { Icon(DeckBoxIcons.Outline.Decks, contentDescription = null) },
        modifier = Modifier.clickable {
        },
      )
      ListItem(
        headlineContent = { Text("Expansion card style") },
        supportingContent = { Text("Change the appearance of expansions in the app") },
        leadingContent = { Icon(DeckBoxIcons.Outline.Collection, contentDescription = null) },
        modifier = Modifier.clickable {
        },
      )
      ListItem(
        headlineContent = { Text("Browse grid style") },
        supportingContent = { Text("Change the density of cards in the browse screens") },
        leadingContent = { Icon(DeckBoxIcons.Outline.Browse, contentDescription = null) },
        modifier = Modifier.clickable {
        },
      )
      ListItem(
        headlineContent = { Text("Expansion grid style") },
        supportingContent = { Text("Change the density of cards in an expansion") },
        leadingContent = { Icon(DeckBoxIcons.Outline.Collection, contentDescription = null) },
        modifier = Modifier.clickable {
        },
      )

      MenuDivider()
      Header("Cache")
      ListItem(
        headlineContent = { Text("Manage offline data") },
        supportingContent = { Text("Delete or edit the cached data") },
        leadingContent = { Icon(Icons.Outlined.Cloud, contentDescription = null) },
        modifier = Modifier.clickable {
        },
      )

      MenuDivider()
      Header("Help")
      ListItem(
        headlineContent = { Text("Feedback") },
        supportingContent = { Text("Provide feedback on issues, features, etc") },
        leadingContent = { Icon(Icons.AutoMirrored.Outlined.LiveHelp, contentDescription = null) },
        modifier = Modifier.clickable {
        },
      )

      MenuDivider()
      Header("About")
      ListItem(
        headlineContent = { Text("Privacy Policy") },
        leadingContent = { Icon(Icons.Outlined.Policy, contentDescription = null) },
        modifier = Modifier.clickable {
        },
      )
      // TODO: Better icon
      ListItem(
        headlineContent = { Text("Open source licenses") },
        leadingContent = { Icon(Icons.Rounded.DataObject, contentDescription = null) },
        modifier = Modifier.clickable {
        },
      )
      // TODO: Better icon
      ListItem(
        headlineContent = { Text("Contribute") },
        leadingContent = { Icon(Icons.Rounded.Merge, contentDescription = null) },
        modifier = Modifier.clickable {
        },
      )
      ListItem(
        headlineContent = { Text("Developed by") },
        supportingContent = { Text("r0adkll") },
        modifier = Modifier.clickable {
        },
      )
      ListItem(
        headlineContent = { Text("Version") },
        supportingContent = { Text("0.1") },
      )
    }
  }
}
