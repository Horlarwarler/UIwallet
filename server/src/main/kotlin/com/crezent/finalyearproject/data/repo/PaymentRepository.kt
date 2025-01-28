package com.crezent.finalyearproject.data.repo

import com.crezent.finalyearproject.domain.util.DatabaseError
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.models.Card

interface PaymentRepository {

    suspend fun getCardById(cardId: String, email: String): Result<Card, DatabaseError>

    suspend fun addCard(
        email: String,
        aesEncryptedString: String,
        iv :String
    ): Result<String, DatabaseError>

}