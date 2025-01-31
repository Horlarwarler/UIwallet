package com.crezent.finalyearproject.core.data.security.encryption

typealias KeyGenerator = KeyPairGenerator
interface KeyPairGenerator {

    fun generateKeyStore()

    fun getClientKeyPair(alias: String): String
}

data class KeyPair(
    val privateKey: String,
    val publicKey: String
)