package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class WalletDetailsData(
    val depositCode: String,
    val name: String,
    val refId: String,
    val email: String,
    val phone: String,
    val createType: String,
    val formatDateTime: String,
    val accountType: String,
)
