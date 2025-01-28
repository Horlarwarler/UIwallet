package com.crezent.finalyearproject.routes.util

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

suspend fun RoutingContext.getEmailFromJwt(): String? {

    val jwtPrincipal = call.principal<JWTPrincipal>()
    val emailAddress = jwtPrincipal?.payload?.getClaim("email")?.asString()

    if (emailAddress == null) {
        call.respond(HttpStatusCode.NotAcceptable, "User Email is required")
        return null
    }
    return emailAddress
}