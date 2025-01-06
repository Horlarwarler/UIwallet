package com.crezent.finalyearproject.utility.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtTokenService : TokenService {
    override fun generateToken(
        vararg claims: TokenClaim,
        config: TokenConfig
    ): String {
        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresAt))


        claims.forEach { claim ->
            token = token.withClaim(
                claim.name,
                claim.value
            )
        }
        return token.sign(Algorithm.HMAC256(config.secret))

    }

}