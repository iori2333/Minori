package me.iori.minori.utils

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*

object Network {
  lateinit var client: HttpClient

  fun load() {
    client = HttpClient()
  }

  fun dispose() {
    client.close()
  }

  suspend inline fun <reified T> get(urlString: String): T {
    return client.get(urlString)
  }


}
