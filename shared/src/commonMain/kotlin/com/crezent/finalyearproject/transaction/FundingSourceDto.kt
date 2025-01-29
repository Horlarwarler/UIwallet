package com.crezent.finalyearproject.transaction

import kotlinx.serialization.Serializable

@Serializable
sealed class FundingSourceDto(val name: String)

@Serializable
data class BankTransfer(
    val accountName: String,
    val accountNumber: String,
) : FundingSourceDto(name = "Bank Transfer")
@Serializable
data class CardPayment(
    val cardNumber: String,
) : FundingSourceDto(name = "Card Payment")
@Serializable
data class UssdPayment(
    val phoneNumber: String
) : FundingSourceDto(
    name = "Ussd Payment"
)