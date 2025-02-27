package com.crezent.finalyearproject.core.data.security.encryption

typealias KeyGenerator = KeyPairGenerator
interface KeyPairGenerator {

    fun generateKeyStore()

    fun getClientKeyPair(alias: String): String
}

