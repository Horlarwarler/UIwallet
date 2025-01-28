package com.crezent.finalyearproject.data.dto

import com.crezent.finalyearproject.transaction.TransactionDto
import kotlinx.serialization.Serializable

@Serializable
data class WalletDto(
    val walletId: String,
    val accountBalance: Double,
    val transactionDtos: List<TransactionDto> = emptyList(),
    //val connectedCards: List<Card> = emptyList(),
    )
