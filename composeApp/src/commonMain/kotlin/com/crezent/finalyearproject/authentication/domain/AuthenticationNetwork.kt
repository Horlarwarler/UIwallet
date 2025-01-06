package com.crezent.finalyearproject.authentication.domain

interface AuthenticationNetwork {
    fun login(
        encryptedData: String,
        signature: String,
        clientPublicKey: String
    )
}