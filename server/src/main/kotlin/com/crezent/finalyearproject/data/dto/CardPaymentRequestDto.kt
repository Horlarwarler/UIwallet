package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serial

@Serializable
data class CardPaymentRequestDto(
    val amount: Amount,
    val bankcard: Bankcard,
    val callbackUrl: String,
    val country: String,
    val product: Product,
    val reference: String,
    val payMethod: String,
    val returnUrl: String,
)

@Serializable

data class Amount(
    val currency: String,
    val total: Int,
)

@Serializable

data class Bankcard(
    val cardHolderName: String,
    val cardNumber: String,
    val cvv: String,
    @SerialName("enable3DS")
    val enable3Ds: Boolean,
    val expiryMonth: String,
    val expiryYear: String,
)

@Serializable

data class Product(
    val description: String,
    val name: String,
)