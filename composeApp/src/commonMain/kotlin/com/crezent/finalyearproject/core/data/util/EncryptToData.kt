package com.crezent.finalyearproject.core.data.util

import com.crezent.finalyearproject.core.data.security.encryption.CryptographicOperation
import com.crezent.finalyearproject.data.dto.EncryptedModel
import kotlinx.serialization.json.Json

inline fun <reified T> EncryptedModel.toData(
    cryptographicOperation: CryptographicOperation
): T? {


    return try {
        val isVerified = cryptographicOperation.verifySignature(
            signature = signature,
            dataToVerify = encryptedData,
            publicKey = ecKey
        )
        if (!isVerified) {
            // Data tampered
            println("Data is not verified")
            return null
        }
        val decryptedString = cryptographicOperation.decryptData(
            encryptedData = encryptedData,
            encryptedAesKey = aesKey
        )

        if (decryptedString != null) {
            Json.decodeFromString<T>(decryptedString)
        } else {
            return null
        }

    } catch (e: Exception) {

        e.printStackTrace()
        return null
    }
}