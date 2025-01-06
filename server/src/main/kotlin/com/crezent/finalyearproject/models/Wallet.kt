package com.crezent.finalyearproject.models

import com.crezent.finalyearproject.transaction.Transaction
import kotlinx.serialization.Serializable

@Serializable
data class Wallet(
    val walletId: String,
    val accountBalance: Double,
    val transactions: List<Transaction> = emptyList(),
    val connectedCards: List<Card> = emptyList(),
    )
