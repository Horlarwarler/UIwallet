package com.crezent.finalyearproject.domain.util

sealed interface RemoteError : Error {
    data object TimeOutException : RemoteError

    data object NoInternetConnection : RemoteError

    data object SerializationException : RemoteError

    data object InvalidSignature : RemoteError

    data object EncryptDecryptError : RemoteError

    data object BadTransformation : RemoteError

    data object TooManyRequest :RemoteError

    data object ServerError :RemoteError
    data class UnKnownError(val message: String) : RemoteError

}

fun RemoteError.toErrorMessage(): String {

    return when (this) {


        RemoteError.EncryptDecryptError -> "Unable to encrypt or decrypt Body"
        RemoteError.BadTransformation -> "Content Received not applicable"
        RemoteError.InvalidSignature -> "Please check your device"
        RemoteError.NoInternetConnection -> "No internet Connection"
        RemoteError.SerializationException -> "Unable to Serialize Data"
        RemoteError.TimeOutException -> "Time Out Exception Occurs"
        is RemoteError.UnKnownError -> this.message
        RemoteError.TooManyRequest -> "Too many request"
        RemoteError.ServerError -> "Server Error"
    }

}