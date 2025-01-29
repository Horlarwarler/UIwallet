package com.crezent.finalyearproject.core.data.network

import com.crezent.finalyearproject.core.domain.model.User
import com.crezent.finalyearproject.data.dto.EncryptedModel
import com.crezent.finalyearproject.data.dto.PublicKey
import com.crezent.finalyearproject.data.dto.ServerResponse
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result

interface BaseApi {
    suspend fun getServerPublicKey(): Result<ServerResponse<PublicKey>, RemoteError>
    suspend fun getAuthenticatedUser(
        bearerToken: String,
        rsaPublicKey : String
    ): Result<ServerResponse<EncryptedModel>, RemoteError>
}