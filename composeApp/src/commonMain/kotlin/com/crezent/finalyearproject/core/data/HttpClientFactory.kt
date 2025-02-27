package com.crezent.finalyearproject.core.data

import com.crezent.finalyearproject.core.domain.util.ApiRoute
import com.crezent.finalyearproject.data.dto.Token
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders.ContentEncoding
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.ContentEncoder
import kotlinx.serialization.json.Json

object HttpClientFactory {
    fun create(clientEngine: HttpClientEngine): HttpClient {
        return HttpClient(
            clientEngine
        ) {

            install(ContentEncoding) {

            }
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                level = LogLevel.ALL
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
                // url(ApiRoute.SAMSUNG_BASE_URL)
                //url(ApiRoute.RENDER_URL)
                url(ApiRoute.BASE_URL)
            }

//            install(Auth) {
//                bearer {
//                    refreshTokens {
//                        val token = client.get {
//                            markAsRefreshTokenRequest()
//                            url("refreshToken")
//                            parameter("refreshToken", "")
//                        }.body<Token>()
//                        BearerTokens(
//                            accessToken = token.bearerToken,
//                            refreshToken = token.refreshToken
//                        )
//                    }
//                }
//            }
        }
    }
}