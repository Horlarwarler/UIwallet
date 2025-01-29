package com.crezent.finalyearproject.home.presentation

import com.crezent.finalyearproject.core.domain.model.User
import com.crezent.finalyearproject.transaction.TransactionDto

data class HomeScreenState(
    val lastBalance: Double? = null,
    val transactionDto: List<TransactionDto> = emptyList(),
    val user: User? = null,
    val isLoading: Boolean = false
)
