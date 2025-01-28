package com.crezent.finalyearproject.utility.security.token

import com.auth0.jwt.JWTVerifier

interface TokenService {
    fun generateToken(
        vararg claims: TokenClaim,
        config: TokenConfig
    ): String

    fun verifyToken(
        audience :String,
        secret :String,
        issuer :String,
    ): JWTVerifier
}