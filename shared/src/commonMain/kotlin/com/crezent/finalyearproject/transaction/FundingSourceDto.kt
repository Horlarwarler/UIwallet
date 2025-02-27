package com.crezent.finalyearproject.transaction

import kotlinx.serialization.Serializable

@Serializable
sealed class FundingSourceDto(val channel: String) {
    @Serializable
    data class BankTransfer(
        val accountName: String,
        val accountNumber: String,
    ) : FundingSourceDto(channel = "Bank Transfer")

    @Serializable
    data class CardPayment(
        val lastFourDigit: String,
        val bank: String,
        val cardType: String
    ) : FundingSourceDto(channel = "Card")

    @Serializable
    data class UssdPayment(
        val bank: String
    ) : FundingSourceDto(
        channel = "Ussd"
    )

    @Serializable
    data class Bank(
        val bank: String,
        val lastFourDigit: String
    ) :  FundingSourceDto(
    channel = "Bank"
    )

}

