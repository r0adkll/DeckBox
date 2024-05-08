package app.deckbox.tournament.limitless

import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.logging.bark
import app.deckbox.tournament.api.model.DeckList
import app.deckbox.tournament.api.model.Participant
import app.deckbox.tournament.api.model.Tournament
import app.deckbox.tournament.limitless.TournamentDataSource.TournamentException
import app.deckbox.tournament.xml.Skraper
import app.deckbox.tournament.xml.builders.CompletedTournamentsBuilder
import app.deckbox.tournament.xml.builders.DeckListBuilder
import app.deckbox.tournament.xml.builders.ParticipantListBuilder
import app.deckbox.tournament.xml.parsers.CompletedTournamentsParser
import app.deckbox.tournament.xml.parsers.DeckListParser
import app.deckbox.tournament.xml.parsers.ParticipantListParser
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
@ContributesBinding(MergeAppScope::class)
class LimitlessTournamentDataSource(
  val httpClient: HttpClient,
  val dispatcherProvider: DispatcherProvider,
) : TournamentDataSource {

  override suspend fun getTournaments(): Result<List<Tournament>> = withContext(dispatcherProvider.io) {
    val result = fetchHtmlDom("$TOURNAMENTS_URL?format=standard")

    if (result.isSuccess) {
      val bodyText = result.getOrThrow()
      bark { "Fetched Tournaments Dom: Len(${bodyText.length})" }
      try {
        val tournamentsBuilder = CompletedTournamentsBuilder()
        val parser = CompletedTournamentsParser(tournamentsBuilder)

        // Scrape the HTML dom text for the tournament information that we want
        Skraper.analyze(parser, bodyText)

        val tournaments = tournamentsBuilder.build()
        Result.success(tournaments)
      } catch (e: Exception) {
        bark(throwable = e) { "Error analyzing html" }
        Result.failure(TournamentException.ParseError)
      }
    } else {
      bark { "Error fetching tournaments" }
      Result.failure(result.exceptionOrNull()!!)
    }
  }

  override suspend fun getParticipants(tournamentId: String): Result<List<Participant>> = withContext(
    dispatcherProvider.io,
  ) {
    val result = fetchHtmlDom("$TOURNAMENTS_URL/$tournamentId")

    if (result.isSuccess) {
      val bodyText = result.getOrThrow()
      bark { "Fetched Tournament($tournamentId) Dom: Len(${bodyText.length})" }
      try {
        val participantListBuilder = ParticipantListBuilder()
        val parser = ParticipantListParser(participantListBuilder)

        // Scrape the HTML dom text for the tournament information that we want
        Skraper.analyze(parser, bodyText)

        val participants = participantListBuilder.build()
        Result.success(participants)
      } catch (e: Exception) {
        bark(throwable = e) { "Error analyzing html" }
        Result.failure(TournamentException.ParseError)
      }
    } else {
      bark { "Error fetching participant list" }
      Result.failure(result.exceptionOrNull()!!)
    }
  }

  override suspend fun getDeckList(deckListId: String): Result<DeckList> = withContext(dispatcherProvider.io) {
    val result = fetchHtmlDom("$DECK_LIST_URL/$deckListId")

    if (result.isSuccess) {
      val bodyText = result.getOrThrow()
      bark { "Fetched DeckList($deckListId) Dom: Len(${bodyText.length})" }
      try {
        val deckListBuilder = DeckListBuilder()
        val parser = DeckListParser(deckListBuilder)

        // Scrape the HTML dom text for the tournament information that we want
        Skraper.analyze(parser, bodyText)

        val deckList = deckListBuilder.build()
        if (deckList != null) {
          // Now load and sync all the cards from the API.
          Result.success(deckList)
        } else {
          bark { "Error parsing deck list for $deckListId" }
          Result.failure(TournamentException.ParseError)
        }
      } catch (e: Exception) {
        bark(throwable = e) { "Error analyzing html" }
        Result.failure(TournamentException.ParseError)
      }
    } else {
      bark { "Error fetching participant list" }
      Result.failure(result.exceptionOrNull()!!)
    }
  }

  private suspend fun fetchHtmlDom(url: String): Result<String> {
    val response = httpClient.get {
      url(url)
    }

    if (response.status.isSuccess()) {
      try {
        val bodyText = response.bodyAsText()
        return Result.success(bodyText)
      } catch (e: Exception) {
        return Result.failure(TournamentException.NetworkError(response.status, e))
      }
    } else {
      return Result.failure(TournamentException.NetworkError(response.status))
    }
  }
}

private const val TOURNAMENTS_URL = "https://limitlesstcg.com/tournaments"
private const val DECK_LIST_URL = "https://limitlesstcg.com/decks/list"
