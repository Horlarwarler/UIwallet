package com.crezent.finalyearproject.core.data.security.encryption

import com.crezent.finalyearproject.models.EncryptionKeyValue

interface CryptographicOperation {

    fun encryptData(
        serverPublicKey: String,

        data: String
    ): EncryptionKeyValue?

    fun decryptData(
        encryptedData: String,
        encryptedAesKey: String
    ): String?

    fun signData(
        dataToSign: String
    ): String?


    fun verifySignature(
        signature: String,
        dataToVerify: String,
        publicKey: String
    ): Boolean


}