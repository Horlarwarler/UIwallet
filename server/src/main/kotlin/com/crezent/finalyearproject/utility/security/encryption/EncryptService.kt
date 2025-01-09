package com.crezent.finalyearproject.utility.security.encryption

import com.crezent.finalyearproject.models.EncryptionKeyValue

interface EncryptService {

    fun encryptData(value: String, clientRsapublicKey: String): EncryptionKeyValue?

    fun decryptData(aesEncryptedString: String, privateKeyString: String, rsaEncryptedKey :String): String?
}