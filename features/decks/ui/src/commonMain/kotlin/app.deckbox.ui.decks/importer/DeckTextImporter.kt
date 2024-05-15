package app.deckbox.ui.decks.importer

import DeckBoxAppBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.ClearAll
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import app.deckbox.common.compose.icons.rounded.Import
import app.deckbox.common.compose.widgets.PokemonCardGrid
import app.deckbox.common.compose.widgets.builder.CardBuilder
import app.deckbox.common.compose.widgets.builder.composables.CardList
import app.deckbox.common.screens.DeckTextImporterScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.features.decks.api.import.DeckTextParser
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.ExperimentalSoftwareKeyboardApi
import com.moriatsushi.insetsx.imePadding
import com.moriatsushi.insetsx.navigationBars
import com.moriatsushi.insetsx.systemBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSoftwareKeyboardApi::class, ExperimentalTextApi::class)
@CircuitInject(MergeActivityScope::class, DeckTextImporterScreen::class)
@Composable
fun DeckTextImporter(
  state: DeckTextImporterUiState,
  modifier: Modifier = Modifier,
) {
  val eventSink = state.eventSink

  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

  Scaffold(
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    contentWindowInsets = WindowInsets.systemBars,
    topBar = {
      DeckBoxAppBar(
        title = LocalStrings.current.importTextTitle,
        navigationIcon = {
          IconButton(
            onClick = { eventSink(DeckTextImporterUiEvent.NavigateBack) },
          ) {
            Icon(
              Icons.AutoMirrored.Rounded.ArrowBack,
              contentDescription = null,
            )
          }
        },
        actions = {
          AnimatedVisibility(state.textField.text.isNotEmpty()) {
            IconButton(
              onClick = { eventSink(DeckTextImporterUiEvent.ClearInput) },
            ) {
              Icon(
                Icons.Rounded.ClearAll,
                contentDescription = null,
              )
            }
          }
        },
        scrollBehavior = scrollBehavior,
      )
    },
    floatingActionButton = {
      AnimatedVisibility(state.isImportEnabled) {
        ExtendedFloatingActionButton(
          text = { Text(LocalStrings.current.fabActionImport) },
          icon = { Icon(Icons.Rounded.Import, contentDescription = null) },
          onClick = { eventSink(DeckTextImporterUiEvent.Import) },
        )
      }
    },
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .padding(paddingValues)
        .padding(horizontal = 16.dp)
        .imePadding(),
    ) {
      Box(
        modifier = Modifier
          .weight(1f),
      ) {
        val focusRequester = remember { FocusRequester() }

        BasicTextField(
          value = state.textField,
          onValueChange = { eventSink(DeckTextImporterUiEvent.TextFieldUpdated(it)) },
          textStyle = MaterialTheme.typography.bodyLarge,
          modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxSize(),
        )

        if (state.textField.text.isEmpty()) {
          val annotatedString = buildAnnotatedString {
            append("Paste your decklist from Pokemon Trading Card Game Online or other sources such as ")

            val linkColor = MaterialTheme.colorScheme.tertiary
            withAnnotation(UrlAnnotation("https://limitlesstcg.com")) {
              withStyle(
                SpanStyle(
                  color = linkColor,
                  textDecoration = TextDecoration.Underline,
                ),
              ) {
                append("LimitlessTCG.com")
              }
            }
            append(" here. The items in your decklist must follow this format to import correctly otherwise it will be ignored: ")

            appendLine()
            appendLine()

            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
              append("[COUNT] [CARD NAME] [SET CODE] [SET NUMBER]")
            }

            append(
              """
              For example:

              1 Charmander PAF 7
              1 Charmander OBF 26
              2 Charizard ex PAF 54
              """.trimIndent()
            )
          }

          ClickableText(
            text = annotatedString,
            style = MaterialTheme.typography.bodyLarge.copy(
              color = MaterialTheme.typography.bodyLarge.color.copy(alpha = .5f),
            ),
            onClick = { offset ->
              val urlAnnotations = annotatedString.getUrlAnnotations(offset, offset)
              urlAnnotations.firstOrNull()?.let { urlAnnotation ->
                eventSink(DeckTextImporterUiEvent.OpenUrl(urlAnnotation.item.url))
              } ?: focusRequester.requestFocus()
            },
            modifier = Modifier
              .fillMaxSize()
              .zIndex(1f),
          )
        }
      }
    }
  }
}
