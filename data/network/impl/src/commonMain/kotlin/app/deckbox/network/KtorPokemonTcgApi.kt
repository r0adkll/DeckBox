package app.deckbox.network

import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.logging.bark
import app.deckbox.core.model.Card
import app.deckbox.core.model.Expansion
import app.deckbox.network.api.CardModel
import app.deckbox.network.api.CardResponse
import app.deckbox.network.api.CardSetModel
import app.deckbox.network.api.CardSetResponse
import app.deckbox.network.api.ModelMapper
import app.deckbox.network.api.SimpleResponse
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.appendPathSegments
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import me.tatarka.inject.annotations.Inject

typealias PokemonTcgApiKey = String

@Inject
@ContributesBinding(MergeAppScope::class)
class KtorPokemonTcgApi(
  private val httpClient: HttpClient,
  private val apiKey: PokemonTcgApiKey = BuildConfig.POKEMON_TCG_API_KEY,
) : PokemonTcgApi {

  private val client by lazy {
    httpClient.config {
      defaultRequest {
        header("X-Api-Key", apiKey)
        url(BASE_URL)
      }
    }
  }

  override suspend fun getCard(id: String): Result<Card> {
    val response = try {
      client.get("cards") {
        url { appendPathSegments(id) }
      }
    } catch (e: Throwable) {
      bark(throwable = e) { "Error fetching card($id)" }
      return Result.failure(e)
    }

    return if (response.status.isSuccess()) {
      try {
        val cardResponse = response.body<CardModel>()
        Result.success(ModelMapper.to(cardResponse))
      } catch (e: Throwable) {
        bark(throwable = e) { "Error fetching card($id)" }
        Result.failure(e)
      }
    } else {
      Result.failure(ApiException("Unable to fetch card($id): ${response.status}"))
    }
  }

  override suspend fun getCards(filters: Map<String, String>?): Result<PagedResponse<Card>> {
    val response = try {
      client.get("cards") {
        url {
          filters?.forEach { (key, value) ->
            parameters.append(key, value)
          }
        }
      }
    } catch (e: Exception) {
      bark(throwable = e) { "Error fetching cards($filters)" }
      return Result.failure(e)
    }

    return if (response.status.isSuccess()) {
      try {
        val cardResponse = response.body<CardResponse>()
        bark {
          """
            PageInfo(
              page = ${cardResponse.page},
              pageSize = ${cardResponse.pageSize},
              count = ${cardResponse.count},
              totalCount = ${cardResponse.totalCount},
            )
          """.trimIndent()
        }
        Result.success(ModelMapper.toPagedResponse(cardResponse))
      } catch (e: Throwable) {
        bark(throwable = e) { "Error fetching cards($filters)" }
        Result.failure(e)
      }
    } else {
      Result.failure(ApiException("Unable to fetch cards: ${response.status}"))
    }
  }

  override suspend fun getExpansion(id: String): Result<Expansion> {
    val response = try {
      client.get("sets") {
        url { appendPathSegments(id) }
      }
    } catch (e: Throwable) {
      bark(throwable = e) { "Error fetching expansion($id)" }
      return Result.failure(e)
    }

    return if (response.status.isSuccess()) {
      try {
        val responseBody = response.body<CardSetModel>()
        Result.success(ModelMapper.to(responseBody))
      } catch (e: Throwable) {
        bark(throwable = e) { "Error fetching expansion($id)" }
        Result.failure(e)
      }
    } else {
      Result.failure(ApiException("Unable to fetch Expansion($id): ${response.status}"))
    }
  }

  override suspend fun getExpansions(): Result<List<Expansion>> {
    val response = try {
      client.get("sets")
    } catch (e: Throwable) {
      bark(throwable = e) { "Error fetching expansions" }
      return Result.failure(e)
    }

    return if (response.status.isSuccess()) {
      try {
        val responseBody = response.body<CardSetResponse>()
        val expansions = ModelMapper.toExpansions(responseBody.sets)
        Result.success(expansions)
      } catch (e: Throwable) {
        bark(throwable = e) { "Error fetching expansions" }
        Result.failure(e)
      }
    } else {
      Result.failure(ApiException("Unable to fetch Expansions: ${response.status}"))
    }
  }

  override suspend fun getExpansions(ptcgCodes: Set<String>): Result<List<Expansion>> {
    val response = try {
      client.get("sets") {
        url {
          parameters {
            append("q", ptcgCodes.joinToString(" OR ") { "ptcgoCode:$it" })
          }
        }
      }
    } catch (e: Throwable) {
      bark(throwable = e) { "Error fetching expansions" }
      return Result.failure(e)
    }

    return if (response.status.isSuccess()) {
      try {
        val responseBody = response.body<CardSetResponse>()
        val expansions = ModelMapper.toExpansions(responseBody.sets)
        Result.success(expansions)
      } catch (e: Throwable) {
        bark(throwable = e) { "Error fetching expansions" }
        Result.failure(e)
      }
    } else {
      Result.failure(ApiException("Unable to fetch Expansions: ${response.status}"))
    }
  }

  override suspend fun getRarities(): Result<List<String>> {
    val response = try {
      client.get("rarities")
    } catch (e: Throwable) {
      bark(throwable = e) { "Error fetching rarities" }
      return Result.failure(e)
    }

    return if (response.status.isSuccess()) {
      val responseBody = response.body<SimpleResponse>()
      Result.success(responseBody.data)
    } else {
      Result.failure(ApiException("Unable to fetch Rarities: ${response.status}"))
    }
  }

  override suspend fun getSubtypes(): Result<List<String>> {
    val response = try {
      client.get("subtypes")
    } catch (e: Throwable) {
      bark(throwable = e) { "Error fetching subtypes" }
      return Result.failure(e)
    }

    return if (response.status.isSuccess()) {
      val responseBody = response.body<SimpleResponse>()
      Result.success(responseBody.data)
    } else {
      Result.failure(ApiException("Unable to fetch subtypes: ${response.status}"))
    }
  }

  companion object {
    private const val BASE_URL = "https://api.pokemontcg.io/v2/"
  }
}
