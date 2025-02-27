package com.crezent.finalyearproject.transaction.presentation.deposit

data class DepositScreenState(
    val isLoading: Boolean = false,
    val currentIndex: Int = 0,
    val amount: String? = null
)
