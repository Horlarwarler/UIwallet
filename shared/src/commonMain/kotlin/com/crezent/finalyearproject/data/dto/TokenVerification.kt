package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenVerification(
    val token: String,
    @SerialName("user_id")
    val userId: String
)