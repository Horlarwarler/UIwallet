package com.crezent.finalyearproject.core.domain.model

import com.crezent.finalyearproject.transaction.TransactionStatus
import com.crezent.finalyearproject.transaction.TransactionType

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
