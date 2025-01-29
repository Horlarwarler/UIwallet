package com.crezent.finalyearproject.transaction.domain

import com.crezent.finalyearproject.core.domain.model.Card
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result

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
}