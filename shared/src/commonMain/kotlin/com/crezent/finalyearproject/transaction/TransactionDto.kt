package com.crezent.finalyearproject.transaction

import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    val transactionId: String? = null,
    val reference: String? = null,
    val transactionTitle: String,
    val transactionDescription: String,
    val transactionAmount: Double,
    val transactionStatus: TransactionStatus,
    val transactionType: TransactionType,
    val paidAt: String,
    // val emailId: String,
    val createdDate: String,
    val fundingSourceDto: FundingSourceDto
)