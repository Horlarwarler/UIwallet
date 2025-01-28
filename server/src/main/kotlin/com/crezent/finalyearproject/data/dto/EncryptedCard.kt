package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class EncryptedCard(
    val cardNumber: String,
    val holderName: String,
    val expirationDate: String
)