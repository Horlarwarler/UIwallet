package com.crezent.finalyearproject.utility.security.encryption

data class EncryptionKeyValue(
    val aesEncryptedString :String,
    val rsaEncryptedKey: String
)
