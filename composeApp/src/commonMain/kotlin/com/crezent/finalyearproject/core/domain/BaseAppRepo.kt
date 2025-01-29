package com.crezent.finalyearproject.core.domain

import com.crezent.finalyearproject.core.domain.model.User
import com.crezent.finalyearproject.data.dto.PublicKey
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result

interface BaseAppRepo {
    suspend fun getApiKey(): Result<PublicKey, RemoteError>

    suspend fun getLocalSaveApiKey(): Result<PublicKey, RemoteError>

    suspend fun getAndCacheAuthenticatedUser(): Result<User, RemoteError>
}