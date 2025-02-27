package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SweepStatusData(
    val status: String,
    val amount: String,
    val currency: String,
    val errorMessage: String
)
