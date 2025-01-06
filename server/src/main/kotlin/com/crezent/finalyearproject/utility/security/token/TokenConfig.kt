package com.crezent.finalyearproject.utility.security.token

data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresAt: Long,
    val secret: String

)