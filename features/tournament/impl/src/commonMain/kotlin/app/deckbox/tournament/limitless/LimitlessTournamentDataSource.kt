package app.deckbox.tournament.limitless

import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.MergeAppScope
import app.deckbox.tournament.api.model.DeckList
import app.deckbox.tournament.api.model.Participant
import app.deckbox.tournament.api.model.Tournament
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import me.tatarka.inject.annotations.Inject
import nl.adaptivity.xmlutil.EventType
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.XmlReader
import nl.adaptivity.xmlutil.core.KtXmlReader
import nl.adaptivity.xmlutil.core.impl.multiplatform.StringReader
import nl.adaptivity.xmlutil.core.impl.multiplatform.use

@Inject
@ContributesBinding(MergeAppScope::class)
class LimitlessTournamentDataSource(
  val httpClient: HttpClient,
  val dispatcherProvider: DispatcherProvider,
) : TournamentDataSource {

  @OptIn(ExperimentalXmlUtilApi::class)
  override suspend fun getTournaments(): List<Tournament> {
    val response = httpClient.get {
      url(TOURNAMENTS_URL)
    }

    if (response.status.isSuccess()) {
      val bodyText = response.bodyAsText()



    } else {

    }

    TODO("Not yet implemented")
  }

  override suspend fun getParticipants(tournament: Tournament): List<Participant> {
    TODO("Not yet implemented")
  }

  override suspend fun getDeckList(tournament: Tournament, participant: Participant): DeckList {
    TODO("Not yet implemented")
  }
}

private const val TOURNAMENTS_URL = "https://limitlesstcg.com/tournaments"
