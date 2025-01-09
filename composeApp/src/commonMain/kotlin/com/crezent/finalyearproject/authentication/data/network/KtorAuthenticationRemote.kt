package com.crezent.finalyearproject.authentication.data.network

import com.crezent.finalyearproject.core.data.util.network.safeRequest
import com.crezent.finalyearproject.core.domain.util.ApiRoute
import com.crezent.finalyearproject.data.dto.EncryptedModel
import com.crezent.finalyearproject.data.dto.PublicKey
import com.crezent.finalyearproject.data.dto.ServerResponse
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.domain.util.onError
import com.crezent.finalyearproject.domain.util.onSuccess
import com.crezent.finalyearproject.domain.util.toErrorMessage
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.request

class KtorAuthenticationRemote(
    private val client: HttpClient
) : AuthenticationRemote {
    override suspend fun login(
        encryptedData: String,
        signature: String,
        clientEcKey: String,
        clientRsaKey: String,
        aesKey: String


    ): Result<ServerResponse<EncryptedModel>, RemoteError> {
        val postBody = EncryptedModel(
            signature = signature,
            encryptedData = encryptedData,
            ecKey = clientEcKey,
            rsaKey = clientRsaKey,
            aesKey = aesKey
        )
        return safeRequest<ServerResponse<EncryptedModel>> {
            client.post(ApiRoute.LOGIN) {
                setBody(postBody)

            }
        }
            .onSuccess {
                println("Successfull login ${it.data}")
            }
            .onError {
                println("Error happened ${it.toErrorMessage()} ")
            }

    }

    override suspend fun signUp(
        encryptedData: String,
        signature: String,
        clientEcKey: String,
        clientRsaKey: String,
        aesKey: String
    ): Result<ServerResponse<String>, RemoteError> {
        val postBody = EncryptedModel(
            signature = signature,
            encryptedData = encryptedData,
            ecKey = clientEcKey,
            rsaKey = clientRsaKey,
            aesKey = aesKey
        )
        return safeRequest<ServerResponse<String>> {
            client.post(ApiRoute.SIGNUP) {
                setBody(postBody)

            }
        }
            .onSuccess {
                println("Successfull login ${it.data}")
            }
            .onError {
                println("Error happened ${it.toErrorMessage()} ")
            }

    }

    override suspend fun forgotPassword(
        encryptedData: String,
        signature: String,
        clientPublicKey: String
    ): Result<ServerResponse<String>, RemoteError> {
        TODO("Not yet implemented")
    }

    override suspend fun verifyEmail(
        encryptedData: String,
        signature: String,
        clientPublicKey: String
    ): Result<ServerResponse<String>, RemoteError> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(
        encryptedData: String,
        signature: String,
        clientPublicKey: String
    ): Result<ServerResponse<String>, RemoteError> {
        TODO("Not yet implemented")
    }

    override suspend fun getServerPublicKey(): Result<ServerResponse<PublicKey>, RemoteError> {
        return safeRequest<ServerResponse<PublicKey>> {
            client.get(ApiRoute.API_KEY)
        }
            .onSuccess {
                println("Successfull login ${it.data}")
            }
            .onError {
                println("Error happened ${it.toErrorMessage()} ")
            }
    }

    override suspend fun sendOtp(
        emailAddress: String,
        purpose: String
    ): Result<ServerResponse<String>, RemoteError> {
        return safeRequest<ServerResponse<String>> {
            client.get(ApiRoute.REQUEST_OTP){
                url {
                    parameters.append("email",emailAddress)
                    parameters.append("purpose",purpose)
                }
            }
        }

    }
}