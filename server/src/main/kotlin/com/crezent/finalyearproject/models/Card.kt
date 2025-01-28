package com.crezent.finalyearproject.models


data class Card(
    val cardId: String,
    val aesEncryptedString: String,
) {
    val isExpired = false
}