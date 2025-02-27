package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CardPaymentData(
    val reference: String,
    val orderNo: String,
    val nextAction: NextAction,
    val status: String,
    val amount: Amount,
    val vat: Vat,
)

@Serializable
data class NextAction(
    val actionType: String,
    val redirectUrl: String,
)

@Serializable
data class Vat(
    val total: Int,
    val currency: String,
)
