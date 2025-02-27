package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class PayStackInitializeTransactionData(
    val access_code: String,
    val authorization_url: String,
    val reference: String
)
