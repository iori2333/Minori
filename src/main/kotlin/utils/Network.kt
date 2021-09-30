package me.iori.minori.utils

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object Network {
  lateinit var client: HttpClient

  fun load() {
    client = HttpClient()
  }

  fun dispose() {
    client.close()
  }

  suspend inline fun <reified T> get(urlString: String, block: HttpRequestBuilder.() -> Unit = {}): T {
    return client.get(urlString, block)
  }

  val jsonBuilder = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
  }

  @OptIn(ExperimentalSerializationApi::class)
  suspend inline fun <reified T> json(urlString: String, block: HttpRequestBuilder.() -> Unit = {}): T {
    val res: String = client.get(urlString, block)
    return jsonBuilder.decodeFromString(res)
  }
}
