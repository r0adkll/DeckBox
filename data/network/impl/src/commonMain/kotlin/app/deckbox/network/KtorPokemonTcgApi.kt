package app.deckbox.network

import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.Card
import app.deckbox.core.model.Expansion
import app.deckbox.network.api.CardModel
import app.deckbox.network.api.CardResponse
import app.deckbox.network.api.CardSetModel
import app.deckbox.network.api.CardSetResponse
import app.deckbox.network.api.ModelMapper
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.appendPathSegments
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

typealias PokemonTcgApiKey = String

@Inject
@ContributesBinding(MergeAppScope::class)
class KtorPokemonTcgApi(
  private val apiKey: PokemonTcgApiKey = BuildConfig.POKEMON_TCG_API_KEY,
) : PokemonTcgApi{
  private val client = HttpClient {
    install(ContentNegotiation) {
      json(
        Json {
          isLenient = true
          ignoreUnknownKeys = true
        },
      )
    }

    install(Logging) {
      level = LogLevel.ALL
      logger = object : Logger {
        override fun log(message: String) {
          println(message)
        }
      }
    }

    defaultRequest {
      header("X-Api-Key", apiKey)
      url(BASE_URL)
    }
  }

  override suspend fun getCard(id: String): Result<Card> {
    val response = client.get("cards") {
      url { appendPathSegments(id) }
    }

    return if (response.status.isSuccess()) {
      val cardResponse = response.body<CardModel>()
      Result.success(ModelMapper.to(cardResponse))
    } else {
      Result.failure(ApiException("Unable to fetch card($id): ${response.status}"))
    }
  }

  override suspend fun getCards(filters: Map<String, String>?): Result<List<Card>> {
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

  override suspend fun getExpansion(id: String): Result<Expansion> {
    val response = client.get("sets") {
      url { appendPathSegments(id) }
    }

    return if (response.status.isSuccess()) {
      val responseBody = response.body<CardSetModel>()
      Result.success(ModelMapper.to(responseBody))
    } else {
      Result.failure(ApiException("Unable to fetch Expansion($id): ${response.status}"))
    }
  }

  override suspend fun getExpansions(): Result<List<Expansion>> {
    val response = try {
      client.get("sets")
    } catch (e: Exception) {
      e.printStackTrace()
      return Result.failure(e)
    }

    println("getExpansions::Result[${response.status.value}]")

    return if (response.status.isSuccess()) {
      try {
        val responseBody = response.body<CardSetResponse>()
        Result.success(ModelMapper.toExpansions(responseBody.sets))
      } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
      }
    } else {
      Result.failure(ApiException("Unable to fetch Expansions: ${response.status}"))
    }
  }

  companion object {
    private const val BASE_URL = "https://api.pokemontcg.io/v2/"
  }
}
