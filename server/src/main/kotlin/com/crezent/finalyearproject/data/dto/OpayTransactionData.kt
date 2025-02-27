package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class OpayTransactionData(
    val total: Long,
    val list: List<OpayTransaction>
)

@Serializable
data class OpayTransaction(
    val merchantId: String,
    val depositCode: String,
    val orderNo: String,
    val depositTime: Long,
    val depositAmount: String,
    val currency: String,
    val orderStatus: String,
    val formatDateTime: String,
    val transactionId: String,
    val notes: String,
)