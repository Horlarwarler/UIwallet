package com.crezent.finalyearproject.authentication.data.network

import com.crezent.finalyearproject.data.dto.LoggedInUser
import com.crezent.finalyearproject.data.dto.ServerResponse
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result

interface AuthenticationRemote {

    suspend fun login(
        encryptedData: String,
        signature: String,
        clientEcKey: String,
        clientRsaKey: String
    ): Result<ServerResponse<LoggedInUser>, RemoteError>

    suspend fun signUp(
        encryptedData: String,
        signature: String,
        clientPublicKey: String
    ): Result<ServerResponse<String>, RemoteError>


    suspend fun forgotPassword(
        encryptedData: String,
        signature: String,
        clientPublicKey: String
    ): Result<ServerResponse<String>, RemoteError>


    suspend fun verifyEmail(
        encryptedData: String,
        signature: String,
        clientPublicKey: String
    ): Result<ServerResponse<String>, RemoteError>


    suspend fun resetPassword(
        encryptedData: String,
        signature: String,
        clientPublicKey: String
    ): Result<ServerResponse<String>, RemoteError>

    suspend fun getServerPublicKey(): Result<ServerResponse<String>, RemoteError>


}