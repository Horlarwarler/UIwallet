package com.crezent.finalyearproject.transaction.domain

import com.crezent.finalyearproject.core.domain.model.Card
import com.crezent.finalyearproject.core.domain.model.Transaction
import com.crezent.finalyearproject.data.dto.InitiateTransactionBody
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.transaction.TransactionDto
import com.crezent.finalyearproject.transaction.domain.model.InitiateTransactionResponse

interface TransactionRepo {
    suspend fun verifyCvv(
        cvv: String,
        id: String
    ): Result<Unit, RemoteError>

    suspend fun createCard(
        cardCvv: String,
        cardHolderName: String,
        cardExpirationDate: String,
        cardNumber: String
    ): Result<Unit, RemoteError>

    suspend fun verifyPayment(
        emailAddress: String,
        reference: String
    ): Result<Transaction, RemoteError>

    suspend fun initiatePayment(
        initiateTransactionBody: InitiateTransactionBody
    ): Result<InitiateTransactionResponse, RemoteError>
}