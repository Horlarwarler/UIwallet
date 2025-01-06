package com.crezent.finalyearproject.utility.security.encryption

import java.security.PrivateKey
import java.security.PublicKey

interface EncryptService {

    fun encryptData(value: String, publicKeyString: String): String?

    fun decryptData(value: String, privateKeyString: String): String?
}