package com.crezent.finalyearproject.home.presentation

import com.crezent.finalyearproject.core.domain.model.Transaction
import com.crezent.finalyearproject.core.domain.model.User
import com.crezent.finalyearproject.transaction.TransactionDto

data class HomeScreenState(
    val lastBalance: Double? = null,
    val transactionDto: List<Transaction> = emptyList(),
    val user: User? = null,
    val isLoading: Boolean = false
)
