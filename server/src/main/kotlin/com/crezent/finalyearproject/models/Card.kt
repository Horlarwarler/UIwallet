package com.crezent.finalyearproject.models

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val cardId: String,
    val encryptedCardNumber: String,
    val encryptedExpirationMonth: String,
    val encryptedExpirationYear: String,
    val hashedCvv: String,
    val encryptedCardHolderName: String,
) {
    val isExpired = false
}