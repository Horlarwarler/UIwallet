package com.crezent.finalyearproject.authentication.data.network

import com.crezent.finalyearproject.data.dto.EncryptedModel
import com.crezent.finalyearproject.data.dto.PublicKey
import com.crezent.finalyearproject.data.dto.ServerResponse
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result

interface AuthenticationRemote {

    suspend fun login(
        encryptedData: String,
        signature: String,
        clientEcKey: String,
        clientRsaKey: String,
        aesKey: String
    ): Result<ServerResponse<String>, RemoteError>

    suspend fun signUp(
        encryptedData: String,
        signature: String,
        clientEcKey: String,
        clientRsaKey: String,
        aesKey: String
    ): Result<ServerResponse<String>, RemoteError>


    suspend fun forgotPassword(
        encryptedData: String,
        signature: String,
        clientPublicKey: String
    ): Result<ServerResponse<String>, RemoteError>


    suspend fun resetPassword(
        encryptedData: String,
        signature: String,
        clientEcKey: String,
        clientRsaKey: String,
        aesKey: String,
        bearerToken: String
    ): Result<ServerResponse<String>, RemoteError>

    suspend fun sendOtp(
        emailAddress: String,
        purpose: String
    ): Result<ServerResponse<String>, RemoteError>

    suspend fun verifyEmail(
        bearerToken: String,
        clientRsaKey: String,
    ): Result<ServerResponse<String>, RemoteError>


    suspend fun verifyOtp(
        emailAddress: String,
        otp: String,
        purpose: String,
    ): Result<ServerResponse<String>, RemoteError>


}