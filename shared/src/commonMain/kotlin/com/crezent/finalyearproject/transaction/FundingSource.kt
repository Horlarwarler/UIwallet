package com.crezent.finalyearproject.transaction

import kotlinx.serialization.Serializable

@Serializable
sealed class FundingSource(val name: String)

@Serializable
data class BankTransfer(
    val accountName: String,
    val accountNumber: String,
) : FundingSource(name = "Bank Transfer")
@Serializable
data class CardPayment(
    val cardNumber: String,
) : FundingSource(name = "Card Payment")
@Serializable
data class UssdPayment(
    val phoneNumber: String
) : FundingSource(
    name = "Ussd Payment"
)



