package com.crezent.finalyearproject.core.data.network

import com.crezent.finalyearproject.core.data.util.network.safeRequest
import com.crezent.finalyearproject.core.domain.model.FundingSource
import com.crezent.finalyearproject.core.domain.model.Transaction
import com.crezent.finalyearproject.core.domain.model.User
import com.crezent.finalyearproject.core.domain.model.Wallet
import com.crezent.finalyearproject.core.domain.util.ApiRoute
import com.crezent.finalyearproject.data.dto.EncryptedModel
import com.crezent.finalyearproject.data.dto.LoggedInUser
import com.crezent.finalyearproject.data.dto.PublicKey
import com.crezent.finalyearproject.data.dto.ServerResponse
import com.crezent.finalyearproject.data.dto.WalletDto
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.domain.util.map
import com.crezent.finalyearproject.domain.util.onError
import com.crezent.finalyearproject.domain.util.onSuccess
import com.crezent.finalyearproject.domain.util.toErrorMessage
import com.crezent.finalyearproject.transaction.FundingSourceDto
import com.crezent.finalyearproject.transaction.TransactionDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders

class KtorBaseApi(
    private val client: HttpClient
) : BaseApi {
    override suspend fun getServerPublicKey(): Result<ServerResponse<PublicKey>, RemoteError> {
        return safeRequest<ServerResponse<PublicKey>> {
            client.get(ApiRoute.API_KEY)
        }
            .onSuccess {
            }
            .onError {
            }
    }

    override suspend fun getAuthenticatedUser(
        bearerToken: String,
        rsaPublicKey: String
    ): Result<ServerResponse<EncryptedModel>, RemoteError> {
        return safeRequest<ServerResponse<EncryptedModel>> {
            client.post(ApiRoute.AUTHENTICATED_USER) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $bearerToken")
                }
                setBody(rsaPublicKey)
            }
        }


    }
}
