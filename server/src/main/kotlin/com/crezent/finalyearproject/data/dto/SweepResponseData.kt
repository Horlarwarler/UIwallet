package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SweepResponseData(
    val orderNo: String,
    val status: String,
    val amount: String,
    val currency: String,
    val tradeTime: String,
    val errorMessage: String
)
