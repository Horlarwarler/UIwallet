package com.crezent.finalyearproject.transaction.data

import com.crezent.finalyearproject.core.data.util.network.safeRequest
import com.crezent.finalyearproject.core.domain.model.Card
import com.crezent.finalyearproject.core.domain.util.ApiRoute
import com.crezent.finalyearproject.data.dto.CardResponse
import com.crezent.finalyearproject.data.dto.EncryptedModel
import com.crezent.finalyearproject.data.dto.ServerResponse
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.domain.util.map
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders

class KtorTransactionApi(
    private val client: HttpClient
) : TransactionApi {
    override suspend fun verifyCvv(
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
            client.post(ApiRoute.VERIFY_CVV) {
                setBody(postBody)
                headers {
                    append(HttpHeaders.Authorization, "Bearer $bearerToken")
                }
            }
        }
    }

    override suspend fun createCard(
        encryptedData: String,
        signature: String,
        clientEcKey: String,
        clientRsaKey: String,
        aesKey: String,
        bearerToken: String

    ): Result<String, RemoteError> {
        val postBody = EncryptedModel(
            signature = signature,
            encryptedData = encryptedData,
            ecKey = clientEcKey,
            rsaKey = clientRsaKey,
            aesKey = aesKey
        )

        return safeRequest<ServerResponse<String>> {
            client.post(ApiRoute.CREATE_CARD) {
                setBody(postBody)
                headers {
                    append(HttpHeaders.Authorization, "Bearer $bearerToken")
                }
            }
        }
            .map {
                it.data!!
            }
    }


}