package com.crezent.finalyearproject.core.domain.model


data class Wallet(
    val walletId: String,
    val accountBalance: Double,
    val transactions: List<Transaction> = emptyList(),
)
