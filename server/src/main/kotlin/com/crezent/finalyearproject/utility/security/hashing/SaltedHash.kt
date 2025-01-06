package com.crezent.finalyearproject.utility.security.hashing

data class SaltedHash(
    val salt: String,
    val hashedValue: String
)
