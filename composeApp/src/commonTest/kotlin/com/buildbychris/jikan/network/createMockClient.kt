package com.buildbychris.jikan.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.json.Json

object MockHttpClientFactory {

   fun createMockHttpClient(response: String, code:  HttpStatusCode) : HttpClient {
       val mockEngine = MockEngine {
           respond(
               content = ByteReadChannel(response),
               status = code,
               headers = headersOf("Content-Type" to listOf("application/json"))
           )
       }

       return HttpClient(mockEngine).config {
           expectSuccess = true
           install(ContentNegotiation) {
               json(
                   Json {
                       prettyPrint = true
                       isLenient = true
                       ignoreUnknownKeys = true
                   }
               )
           }

       }
   }

}