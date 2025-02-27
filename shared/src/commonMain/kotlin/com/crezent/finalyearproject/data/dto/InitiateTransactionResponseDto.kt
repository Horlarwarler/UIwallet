package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class InitiateTransactionResponseDto(
    val referenceCode:String,
    val authorizationUrl:String
)