package com.crezent.finalyearproject.authentication.data

import com.crezent.finalyearproject.data.dto.LoggedInUser
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result

interface AuthenticationRepo {

    suspend fun login(
        emailAddress: String,
        password: String
    ): Result<LoggedInUser, RemoteError>

    suspend fun getApiKey(): Result<String, RemoteError>
}