package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EncryptedModel(
    val signature: String,
    @SerialName("encrypted_data")
    val encryptedData: String,
    @SerialName("ec_key") // For verification information send
    val ecKey: String,
    @SerialName("rsa_key") // For returning information to
    val rsaKey: String,
    @SerialName("aes_key") // For returning information to
    val aesKey: String,
)


