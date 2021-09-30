package me.iori.minori.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*

object Network {
  lateinit var client: HttpClient

  fun load() {
    client = HttpClient(CIO) {
      install(JsonFeature) {
        serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
          prettyPrint = true
          isLenient = true
          ignoreUnknownKeys = true
        })
      }
    }
  }

  fun dispose() {
    client.close()
  }

  suspend inline fun <reified T> get(urlString: String, block: HttpRequestBuilder.() -> Unit = {}): T {
    return client.get(urlString, block)
  }
}
