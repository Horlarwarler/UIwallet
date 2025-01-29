package com.crezent.finalyearproject.core.domain.model

import kotlinx.serialization.Serializable

sealed class FundingSource(val name: String) {
    data class BankTransfer(
        val accountName: String,
        val accountNumber: String,
    ) : FundingSource(name = "Bank Transfer")

    data class CardPayment(
        val cardNumber: String,
    ) : FundingSource(name = "Card Payment")

    data class UssdPayment(
        val phoneNumber: String
    ) : FundingSource(
        name = "Ussd Payment"
    )
}