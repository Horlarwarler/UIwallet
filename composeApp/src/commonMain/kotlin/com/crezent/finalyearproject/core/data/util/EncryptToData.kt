package com.crezent.finalyearproject.core.data.util

import com.crezent.finalyearproject.core.data.security.encryption.CryptographicOperation
import com.crezent.finalyearproject.data.dto.EncryptedModel
import kotlinx.serialization.json.Json

inline fun <reified T> EncryptedModel.toData(): T? {


    return try {
        val isVerified = CryptographicOperation.verifySignature(
            signature = signature,
            dataToVerify = encryptedData,
            publicKey = ecKey
        )
        if (!isVerified) {
            // Data tampered
            println("Data is not verified")
            return null
        }
        val decryptedString = CryptographicOperation.decryptData(
            encryptedData = encryptedData,
            encryptedAesKey = aesKey
        )

        Json.decodeFromString<T>(decryptedString)

    } catch (e: Exception) {

        e.printStackTrace()
        return null
    }
}