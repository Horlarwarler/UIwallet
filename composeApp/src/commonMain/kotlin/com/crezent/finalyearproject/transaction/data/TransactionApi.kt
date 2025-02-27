package com.crezent.finalyearproject.transaction.data

import com.crezent.finalyearproject.core.domain.model.Card
import com.crezent.finalyearproject.data.dto.InitiateTransactionBody
import com.crezent.finalyearproject.data.dto.InitiateTransactionResponseDto
import com.crezent.finalyearproject.data.dto.ServerResponse
import com.crezent.finalyearproject.data.dto.SignatureModel
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.transaction.TransactionDto
import com.crezent.finalyearproject.transaction.domain.model.InitiateTransactionResponse

interface TransactionApi {

    suspend fun verifyCvv(
        encryptedData: String,
        signature: String,
        clientEcKey: String,
        clientRsaKey: String,
        aesKey: String,
        bearerToken: String


    ): Result<ServerResponse<String>, RemoteError>

    suspend fun createCard(
        encryptedData: String,
        signature: String,
        clientEcKey: String,
        clientRsaKey: String,
        aesKey: String,
        bearerToken: String

    ): Result<String, RemoteError>

    suspend fun initiatePayment(
        initiateTransactionBody: InitiateTransactionBody,
        bearerToken: String
    ): Result<InitiateTransactionResponse, RemoteError>

    suspend fun verifyTransaction(
        reference: String,
        emailAddress: String,
        bearerToken: String
    ): Result<ServerResponse<SignatureModel>, RemoteError>
}