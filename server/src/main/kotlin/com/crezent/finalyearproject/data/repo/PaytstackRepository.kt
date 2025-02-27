package com.crezent.finalyearproject.data.repo

import com.crezent.finalyearproject.data.dto.*
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.utility.Constant.INITIALIZE_TRANSACTION_PATH
import com.crezent.finalyearproject.utility.Constant.PAY_STACK_TRANSACTION
import com.crezent.finalyearproject.utility.Constant.VERIFY_TRANSACTION
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException

class PayStackRepository(
    private val client: HttpClient

) {

    suspend fun initializeTransaction(
        initiateTransactionBody: InitiateTransactionBody,
        secretKey: String
    ): Result<PayStackResponse<PayStackInitializeTransactionData>, RemoteError> {

        return makePayStackSafeRequest {
            client.post(INITIALIZE_TRANSACTION_PATH) {
                setBody(initiateTransactionBody)
                contentType(ContentType.Application.Json)

                headers {
                    append(HttpHeaders.Authorization, "Bearer $secretKey")
                }
            }

        }

    }

    suspend fun verifyTransactionStatus(
        secretKey: String,
        paymentRef: String
    ): Result<PayStackResponse<PayStackTransaction>, RemoteError> {
        return makePayStackSafeRequest {
            client.get("$VERIFY_TRANSACTION/$paymentRef") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $secretKey")
                }
            }

        }
    }

    suspend fun getTransactions(
        secretKey: String,
        perPage: Int?,
        pageNumber: Int?,
        customerId: Int?,
    ): Result<PayStackResponse<List<PayStackTransaction>>, RemoteError> {
        return makePayStackSafeRequest {
            client.get(PAY_STACK_TRANSACTION) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $secretKey")
                }
                parameter(
                    "perPage", perPage
                )
                parameter(
                    "page", pageNumber
                )
                parameter(
                    "customer", customerId
                )
            }
        }
    }

    suspend fun getTransactionById(
        secretKey: String,
        id: String
    ): Result<PayStackResponse<PayStackTransaction>, RemoteError> {
        return makePayStackSafeRequest {
            client.get("$PAY_STACK_TRANSACTION/$id") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $secretKey")
                }

            }
        }
    }


    private suspend inline fun <reified T> makePayStackSafeRequest(
        httpResponse: () -> HttpResponse
    ): Result<T, RemoteError> {
        try {
            val response = httpResponse()
            if (response.status.value == 400) {
                val message = response.body<PayStackErrorResponse>().message

                return Result.Error(error = RemoteError.UnKnownError(message = message))
            }
            if (response.status.value != 200) {
                return Result.Error(error = RemoteError.UnKnownError(message = response.status.description))

            }
            try {
                val payStackResponse = response.body<T>()
                return Result.Success(data = payStackResponse)
            } catch (error: SerializationException) {
                return Result.Error(error = RemoteError.SerializationException)
            } //catch (error: io.ktor.serialization.JsonConvertException) {
            // return Result.Error(error = RemoteError.SerializationException)
            // }


        } catch (error: Exception) {
            return Result.Error(error = RemoteError.UnKnownError(message = error.message ?: "Unknown Error"))
        }

    }


}