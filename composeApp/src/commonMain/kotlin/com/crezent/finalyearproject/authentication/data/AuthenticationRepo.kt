package com.crezent.finalyearproject.authentication.data

import com.crezent.finalyearproject.data.dto.LoggedInUser
import com.crezent.finalyearproject.data.dto.PublicKey
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result

interface AuthenticationRepo {

    suspend fun login(
        emailAddress: String,
        password: String
    ): Result<Unit, RemoteError>

    suspend fun signUp(
        emailAddress: String,
        password: String,
        fullName: String,
        gender: String,
        phoneNumber: String,
        matricNumber: String
    ): Result<String, RemoteError>


    suspend fun requestOtp(
        emailAddress: String,
        purpose: String
    ): Result<String, RemoteError>

    suspend fun verifyEmail(
    ): Result<Unit, RemoteError>

    suspend fun resetPassword(
        newPassword: String,
    ): Result<Unit, RemoteError>

    suspend fun verifyOtp(
        emailAddress: String,
        purpose: String,
        otp: String,
    ): Result<Unit, RemoteError>
}