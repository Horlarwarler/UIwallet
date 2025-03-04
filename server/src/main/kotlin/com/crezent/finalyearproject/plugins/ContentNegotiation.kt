package com.crezent.finalyearproject.plugins

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

fun Application.configureContentNegotiation() {
    install(ContentNegotiation) {
        json(
            Json {
                isLenient = true
                prettyPrint = true
                ignoreUnknownKeys = true
            },
            contentType = ContentType.Application.Json
        )
    }
}