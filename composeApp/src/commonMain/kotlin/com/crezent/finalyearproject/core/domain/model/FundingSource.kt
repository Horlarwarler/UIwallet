package com.crezent.finalyearproject.core.domain.model

sealed class FundingSource(val channel: String) {
    data class BankTransfer(
        val accountName: String,
        val accountNumber: String,
    ) : FundingSource(channel = "Bank Transfer")

    data class CardPayment(
        val cardNumber: String,
    ) : FundingSource(channel = "Card Payment")

    data class UssdPayment(
        val bank: String
    ) : FundingSource(
        channel = "Ussd Payment"
    )
    data class Bank(
        val bank: String,
        val lastFourDigit: String
    ) :  FundingSource(
        channel = "Bank"
    )

}