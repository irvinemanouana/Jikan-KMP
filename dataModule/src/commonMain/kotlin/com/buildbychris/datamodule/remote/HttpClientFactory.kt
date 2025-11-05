package com.buildbychris.datamodule.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {
    /**
     * @param engine Okhttp for android and Darwin for IOS
     * @return HttpClient
     */
    fun getHttpClientInstance(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine = engine) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
                url("https://api.jikan.moe/v4/")
            }

            expectSuccess = true

            install(Logging) {
                level = LogLevel.ALL
                logger = object  : Logger {
                    override fun log(message: String) {
                        println("Ktor Logger: $message")
                    }
                }
            }
        }
    }
}