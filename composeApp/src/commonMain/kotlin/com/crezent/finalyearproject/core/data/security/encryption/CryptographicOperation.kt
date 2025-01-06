package com.crezent.finalyearproject.core.data.security.encryption

expect object CryptographicOperation {

    fun encryptData(
        serverPublicKey: String,

        data: String
    ): String

    fun decryptData(
        encryptedData: String
    ): String

    fun signData(
        dataToSign: String
    ): String



    fun verifySignature(
        signature:String,
        dataToVerify:String,
        publicKey:String
    ):Boolean



}