package com.crezent.finalyearproject.domain.util

interface EncryptionError : Error {

    data object BadEncryption : EncryptionError

    data object BadDecryption : EncryptionError
}