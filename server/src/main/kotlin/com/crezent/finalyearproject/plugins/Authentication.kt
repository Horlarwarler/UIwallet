package com.crezent.finalyearproject.plugins

import com.crezent.finalyearproject.utility.security.token.JwtTokenService
import com.crezent.finalyearproject.utility.security.token.TokenConfig
import com.crezent.finalyearproject.utility.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.util.reflect.*
import org.koin.ktor.ext.inject

fun Application.configureAuthentication(
    //  tokenConfig: TokenConfig
) {
    val myRealm = environment.config.property("jwt.realm").getString()
    val tokenService = JwtTokenService()

    val secret = System.getenv("jwt_secret")!!
    val audience = environment.config.property("jwt.audience").getString()
    val issuer = environment.config.property("jwt.issuer").getString()

    install(Authentication) {
        jwt {
            realm = myRealm
            verifier(
                tokenService.verifyToken(
                    secret = secret,
                    audience = audience,
                    issuer = issuer
                )
            )
            validate { credential ->
                if (credential.payload.getClaim("email").asString() != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
        jwt("otp-jwt") {
            realm = myRealm
            verifier(
                tokenService.verifyToken(
                    secret = secret,
                    audience = audience,
                    issuer = issuer
                )
            )
            validate { credential ->
                if (credential.payload.getClaim("email").asString() != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    message = "Time out, Restart t "
                )
            }

        }
    }

}