package com.crezent.finalyearproject.authentication.data.network

import com.crezent.finalyearproject.core.data.util.network.safeRequest
import com.crezent.finalyearproject.core.domain.util.ApiRoute
import com.crezent.finalyearproject.data.dto.EncryptedModel
import com.crezent.finalyearproject.data.dto.PublicKey
import com.crezent.finalyearproject.data.dto.ServerResponse
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.domain.util.map
import com.crezent.finalyearproject.domain.util.onError
import com.crezent.finalyearproject.domain.util.onSuccess
import com.crezent.finalyearproject.domain.util.toErrorMessage
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders

class KtorAuthenticationRemote(
    private val client: HttpClient
) : AuthenticationRemote {
    override suspend fun login(
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
        bearerToken: String,
        clientRsaKey: String
    ): Result<ServerResponse<String>, RemoteError> {
        return safeRequest<ServerResponse<String>> {
            client.put(ApiRoute.VERIFY_MAIL) {
                setBody(clientRsaKey)
                headers {
                    append(HttpHeaders.Authorization, "Bearer $bearerToken")
                }
                //header("rsa_client_key", clientRsaKey)
            }
        }
            .onSuccess {
                println("Successfull Verify Email ${it.data}")
            }
            .onError {
                println("Error happened ${it.toErrorMessage()} ")
            }

    }

    override suspend fun resetPassword(
        encryptedData: String,
        signature: String,
        clientEcKey: String,
        clientRsaKey: String,
        aesKey: String,
        bearerToken: String

    ): Result<ServerResponse<String>, RemoteError> {
        val postBody = EncryptedModel(
            signature = signature,
            encryptedData = encryptedData,
            ecKey = clientEcKey,
            rsaKey = clientRsaKey,
            aesKey = aesKey
        )
        return safeRequest<ServerResponse<String>> {
            client.put(ApiRoute.RESET_PASSWORD) {
                setBody(postBody)
                headers {
                    append(HttpHeaders.Authorization, "Bearer $bearerToken")
                }
            }
        }
            .onSuccess {
                println("Successfull Reset Password ${it.data}")
            }

            .onError {
                println("Error happened ${it.toErrorMessage()} ")
            }

    }


    override suspend fun sendOtp(
        emailAddress: String,
        purpose: String,
    ): Result<ServerResponse<String>, RemoteError> {
        println("Sending otp with $emailAddress")
        return safeRequest<ServerResponse<String>> {
            client.get(ApiRoute.REQUEST_OTP) {
                url {
                    parameters.append("email", emailAddress)
                    parameters.append("purpose", purpose)
                }

            }
        }

    }

    override suspend fun verifyOtp(
        emailAddress: String,
        otp: String,
        purpose: String
    ): Result<ServerResponse<String>, RemoteError> {
        return safeRequest<ServerResponse<String>> {
            client.get(ApiRoute.VERIFY_OTP) {
                url {
                    parameters.append("email", emailAddress)
                    parameters.append("token", otp)
                    parameters.append("purpose", purpose)
                }
            }
        }
    }
}