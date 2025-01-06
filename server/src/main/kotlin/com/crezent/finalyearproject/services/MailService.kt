package com.crezent.finalyearproject.services

import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.domain.util.RemoteError

object MailService {

    fun sendVerificationToken(
        userEmail: String,
        token: String
    ): Result<Unit, RemoteError> {
        try {
            return Result.Success(Unit)
        } catch (e: Error) {
            return Result.Error(error = RemoteError.UnKnownError(message = e.message ?: ""))

        }
    }
}