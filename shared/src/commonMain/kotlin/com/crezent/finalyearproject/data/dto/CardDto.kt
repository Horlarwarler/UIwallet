package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardDto(

    val holderName: String,
    val cardNumber: String,
    val cardCvv: String,
    val expirationDate: String
)
