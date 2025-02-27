package com.crezent.finalyearproject.transaction.domain.model

data class InitiateTransactionResponse(
    val referenceCode: String,
    val authorizationUrl: String
)
