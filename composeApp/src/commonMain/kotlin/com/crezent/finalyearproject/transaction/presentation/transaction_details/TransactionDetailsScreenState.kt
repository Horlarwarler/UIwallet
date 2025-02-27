package com.crezent.finalyearproject.transaction.presentation.transaction_details

import com.crezent.finalyearproject.core.domain.model.Transaction

data class TransactionDetailsScreenState(
    val transaction: Transaction,
    val name: String,
    val email: String
)
