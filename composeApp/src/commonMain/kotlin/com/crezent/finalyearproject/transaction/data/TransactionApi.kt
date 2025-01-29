package com.crezent.finalyearproject.transaction.data

import com.crezent.finalyearproject.core.domain.model.Card
import com.crezent.finalyearproject.data.dto.ServerResponse
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result

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
}