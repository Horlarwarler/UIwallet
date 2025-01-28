package com.crezent.finalyearproject.utility.security.encryption

import com.crezent.finalyearproject.models.EncryptionKeyValue

interface EncryptService {

    fun encryptData(value: String, clientRsapublicKey: String): EncryptionKeyValue?

    fun decryptData(
        aesEncryptedString: String,
        rsaPrivateKeyString: String,
        rsaEncryptedKey: String,
    ): String?

    fun encryptCardDetails(
        data: String,
        key: String
    ): Pair<String, String>

    fun decryptCardDetails(
        data: String, key: String, encodedIV: String

    ): String?
}