package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class WalletBalanceData(
    val name: String,
    val refId: String,
    val amount: String,
    val currency: String,
    val queryTime: Long
)
