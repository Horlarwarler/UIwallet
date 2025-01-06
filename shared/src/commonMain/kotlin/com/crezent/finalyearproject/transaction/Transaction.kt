package com.crezent.finalyearproject.transaction

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val transactionId: String,
    val transactionTitle: String,
    val transactionDescription: String,
    val transactionAmount: Double,
    val transactionStatus: TransactionStatus,
    val transactionType: TransactionType,
    val transactionDate: String,
    val emailId: String,
    val fundingSource: FundingSource
)