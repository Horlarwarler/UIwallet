package com.crezent.finalyearproject.core.data.util.network

import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.util.network.UnresolvedAddressException


suspend inline fun <reified T> safeRequest(
    httpRequest: () -> HttpResponse
): Result<T, RemoteError> {

    val response = try {
        httpRequest()
    } catch (e: ConnectTimeoutException) {
        return Result.Error(error = RemoteError.TimeOutException)
    } catch (e: UnresolvedAddressException) {
        return Result.Error(error = RemoteError.NoInternetConnection)
    }

    catch (e: Exception) {
        return Result.Error(
            error = RemoteError.UnKnownError(
                message = e.message ?: "Unknown Error"
            )
        )
    }
    return responseToResult(response)

}

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, RemoteError> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                Result.Error(RemoteError.EncryptDecryptError)
            }
        }

        408 -> Result.Error(RemoteError.TimeOutException)
        429 -> Result.Error(RemoteError.TooManyRequest)
        401 -> Result.Error(RemoteError.UnAuthorised)
        in 500..599 -> Result.Error(RemoteError.ServerError)
        else -> Result.Error(RemoteError.UnKnownError(response.bodyAsText()))
    }
}