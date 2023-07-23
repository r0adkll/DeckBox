package app.deckbox.network

import app.deckbox.core.model.Card
import app.deckbox.network.api.CardModel
import app.deckbox.network.api.CardResponse
import app.deckbox.network.api.ModelMapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.appendPathSegments
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class PokemonTcgApi(
  private val apiKey: String,
) {
  private val client = HttpClient {
    install(ContentNegotiation) {
      json(
        Json {
          isLenient = true
          ignoreUnknownKeys = true
        },
      )
    }

    defaultRequest {
      header("X-Api-Key", apiKey)
      url(BASE_URL)
    }
  }

  suspend fun getCards(filters: Map<String, String>? = null): Result<List<Card>> {
    val response = client.get("cards") {
      url {
        filters?.forEach { (key, value) ->
          parameters.append(key, value)
        }
      }
    }

    return if (response.status.isSuccess()) {
      val cardResponse = response.body<CardResponse>()
      Result.success(ModelMapper.toCards(cardResponse.cards))
    } else {
      Result.failure(ApiException("Unable to fetch cards: ${response.status}"))
    }
  }

  suspend fun getCard(id: String): Result<Card> {
    val response = client.get("cards") {
      url { appendPathSegments(id) }
    }

    return if (response.status.isSuccess()) {
      val cardResponse = response.body<CardModel>()
      Result.success(ModelMapper.to(cardResponse))
    } else {
      Result.failure(ApiException("Unable to fetch card for $id: ${response.status}"))
    }
  }

  companion object {
    private const val BASE_URL = "https://api.pokemontcg.io/v2/"
  }
}
