package app.deckbox.ui.decks.importer

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import app.deckbox.common.compose.widgets.builder.model.CardUiModel
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.common.screens.DeckBuilderScreen
import app.deckbox.common.screens.DeckTextImporterScreen
import app.deckbox.common.screens.UrlScreen
import app.deckbox.core.coroutines.map
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.extensions.prependIfNotEmpty
import app.deckbox.core.model.Card
import app.deckbox.core.model.Evolution
import app.deckbox.core.model.Stacked
import app.deckbox.core.model.SuperType
import app.deckbox.features.decks.api.DeckRepository
import app.deckbox.features.decks.api.import.CardSpec
import app.deckbox.features.decks.api.import.DeckSpec
import app.deckbox.features.decks.api.import.DeckTextParser
import app.deckbox.ui.decks.importer.util.addStyleCardSpec
import cafe.adriel.lyricist.LocalStrings
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.ui
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(MergeActivityScope::class, DeckTextImporterScreen::class)
@Inject
class DeckTextImporterPresenter(
  @Assisted private val navigator: Navigator,
  @Assisted private val screen: DeckTextImporterScreen,
  private val deckTextParser: DeckTextParser,
  private val deckRepository: DeckRepository,
) : Presenter<DeckTextImporterUiState> {

  @Composable
  override fun present(): DeckTextImporterUiState {
    val coroutineScope = rememberCoroutineScope()
    var textFieldState by remember { mutableStateOf(TextFieldValue()) }

    val deckSpec by remember {
      snapshotFlow { textFieldState }
        .map { deckTextParser.parse(it.text) }
    }.collectAsState(DeckSpec())

    val richTextFieldState = enrichTextFieldValue(textFieldState, deckSpec)

    return DeckTextImporterUiState(
      textField = richTextFieldState,
      isLoading = textFieldState.text != deckSpec.originalText,
      isImportEnabled = deckSpec.canImport,
      previewModels = convertToUiModels(deckSpec.cards),
    ) { event ->
      when (event) {
        DeckTextImporterUiEvent.NavigateBack -> navigator.pop()
        DeckTextImporterUiEvent.Import -> coroutineScope.launch {
          importDeck(deckSpec)
        }

        is DeckTextImporterUiEvent.TextFieldUpdated -> textFieldState = event.field
        is DeckTextImporterUiEvent.OpenUrl -> navigator.goTo(UrlScreen(event.url))
        DeckTextImporterUiEvent.ClearInput -> textFieldState = TextFieldValue()
        is DeckTextImporterUiEvent.CardClick -> navigator.goTo(CardDetailScreen(event.card))
      }
    }
  }

  @Composable
  private fun enrichTextFieldValue(
    value: TextFieldValue,
    deckSpec: DeckSpec,
  ): TextFieldValue {
    val syntaxString = buildAnnotatedString {
      // append the full raw text
      append(value.text)

      // If the processed text doesn't match the input, cancel this processing
      if (deckSpec.originalText != value.text) {
        addStyle(DisabledSpanStyle, 0, value.text.length)
        return@buildAnnotatedString
      }

      when (val deckContent = deckSpec.content) {
        DeckSpec.Content.Empty -> {
          // Do nothing
        }

        DeckSpec.Content.Invalid -> {
          // Do nothing
        }

        is DeckSpec.Content.Parsed -> {
          var currentOffset = 0
          deckContent.cards.forEach { spec ->
            when (spec.result) {
              CardSpec.ParseResult.Error.Invalid -> Unit
              CardSpec.ParseResult.Error.MissingCount -> Unit
              is CardSpec.ParseResult.Success -> when (spec.validation) {
                CardSpec.Validation.None -> Unit
                is CardSpec.Validation.Failure.InvalidNumber -> {
                  addStyleCardSpec(
                    lineOffset = currentOffset,
                    line = spec.line,
                    countStyle = DisabledSpanStyle,
                    nameStyle = DisabledSpanStyle,
                    expansionStyle = DisabledSpanStyle,
                    numberStyle = SpanStyle(
                      color = MaterialTheme.colorScheme.error,
                      fontWeight = FontWeight.Bold,
                      textDecoration = TextDecoration.Underline,
                    ),
                  )
                }

                is CardSpec.Validation.Failure.InvalidSetCode -> {
                  addStyleCardSpec(
                    lineOffset = currentOffset,
                    line = spec.line,
                    countStyle = DisabledSpanStyle,
                    nameStyle = DisabledSpanStyle,
                    numberStyle = DisabledSpanStyle,
                    expansionStyle = SpanStyle(
                      color = MaterialTheme.colorScheme.error,
                      fontWeight = FontWeight.Bold,
                      textDecoration = TextDecoration.Underline,
                    ),
                  )
                }

                is CardSpec.Validation.Success -> {
                  addStyleCardSpec(
                    lineOffset = currentOffset,
                    line = spec.line,
                    countStyle = SpanStyle(color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Bold),
                    nameStyle = SpanStyle(fontStyle = FontStyle.Italic),
                    expansionStyle = SpanStyle(
                      color = MaterialTheme.colorScheme.primary,
                      fontWeight = FontWeight.SemiBold,
                    ),
                    numberStyle = SpanStyle(color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold),
                  )
                }
              }
            }

            // Increment the offset so the next line processing can offset styling appropriately
            currentOffset += spec.line.length + 1 // Accounting for the newline character
          }
        }
      }
    }
    return TextFieldValue(
      annotatedString = syntaxString,
      selection = value.selection,
      composition = value.composition,
    )
  }

  @Composable
  private fun convertToUiModels(
    cards: List<Stacked<Card>>,
  ): ImmutableList<CardUiModel> {
    val pokemon = mutableListOf<CardUiModel>()
    val trainers = mutableListOf<CardUiModel>()
    val energy = mutableListOf<CardUiModel>()

    val evolutions = Evolution.create(cards)
    evolutions.forEach { evolution ->
      if (evolution.size == 1) {
        evolution.firstNodeCards().forEach { card ->
          when (card.card.supertype) {
            SuperType.ENERGY -> energy += CardUiModel.Single(card)
            SuperType.TRAINER -> trainers += CardUiModel.Single(card)
            else -> pokemon += CardUiModel.Single(card)
          }
        }
      } else {
        pokemon += CardUiModel.EvolutionLine(evolution)
      }
    }

    pokemon.apply {
      sortBy {
        when (it) {
          is CardUiModel.EvolutionLine -> it.evolution.firstNodeCards().first().card.name
          is CardUiModel.Single -> "zzzz${it.card.card.name}"
          else -> BottomSortName
        }
      }
    }

    trainers.apply {
      sortBy {
        when (it) {
          is CardUiModel.Single -> it.card.card.name
          else -> BottomSortName
        }
      }
    }

    energy.apply {
      sortBy {
        when (it) {
          is CardUiModel.Single -> it.card.card.name
          else -> BottomSortName
        }
      }
    }

    // Concatenate the models
    val uiModels = pokemon.prependIfNotEmpty(
      CardUiModel.SectionHeader(
        superType = SuperType.POKEMON,
        count = pokemon.sumOf { it.size },
        title = { LocalStrings.current.deckListHeaderPokemon },
      ),
    ) + trainers.prependIfNotEmpty(
      CardUiModel.SectionHeader(
        superType = SuperType.TRAINER,
        count = trainers.sumOf { it.size },
        title = { LocalStrings.current.deckListHeaderTrainer },
      ),
    ) + energy.prependIfNotEmpty(
      CardUiModel.SectionHeader(
        superType = SuperType.ENERGY,
        count = energy.sumOf { it.size },
        title = { LocalStrings.current.deckListHeaderEnergy },
      ),
    )

    return uiModels.toImmutableList()
  }

  private suspend fun importDeck(deckSpec: DeckSpec) {
    val cards = deckSpec.cards
    if (cards.isNotEmpty()) {
      val newDeckId = deckRepository.importDeck(
        name = "",
        cards = cards,
      )

      navigator.pop()
      navigator.goTo(DeckBuilderScreen(newDeckId))
    }
  }
}

private val DisabledSpanStyle
  @Composable get() = SpanStyle(
    fontStyle = FontStyle.Italic,
    color = MaterialTheme.typography.bodyLarge.color.copy(alpha = 0.5f),
  )

private const val BottomSortName = "zzzzzzzzzzzz"
