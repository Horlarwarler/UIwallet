package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class FeeRequest(
    val feeAmount: Double,
    val session: String,
    val semester: String
)
