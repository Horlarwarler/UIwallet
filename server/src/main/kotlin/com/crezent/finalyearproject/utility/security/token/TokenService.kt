package com.crezent.finalyearproject.utility.security.token

interface TokenService {
    fun generateToken(
        vararg claims: TokenClaim,
        config: TokenConfig
    ): String
}