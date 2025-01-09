package com.crezent.finalyearproject.authentication.data.repo

import com.crezent.finalyearproject.EC_ALIAS
import com.crezent.finalyearproject.RSA_ALIAS
import com.crezent.finalyearproject.authentication.data.AuthenticationRepo
import com.crezent.finalyearproject.authentication.data.network.AuthenticationRemote
import com.crezent.finalyearproject.core.data.security.encryption.CryptographicOperation
import com.crezent.finalyearproject.core.data.security.encryption.KeyPairGenerator
import com.crezent.finalyearproject.core.data.util.toData
import com.crezent.finalyearproject.core.domain.preference.EncryptedSharePreference
import com.crezent.finalyearproject.core.domain.preference.SharedPreference
import com.crezent.finalyearproject.data.dto.LoggedInUser
import com.crezent.finalyearproject.data.dto.LoginDetails
import com.crezent.finalyearproject.data.dto.PublicKey
import com.crezent.finalyearproject.data.dto.SignUpDetails
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.domain.util.map
import com.crezent.finalyearproject.domain.util.onSuccess
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AuthenticationRepoImpl(
    private val encryptedPref: EncryptedSharePreference,
    private val pref: SharedPreference,
    private val authenticationRemote: AuthenticationRemote
) : AuthenticationRepo {

    override suspend fun login(
        emailAddress: String,
        password: String
    ): Result<LoggedInUser, RemoteError> {


        val loginDto = LoginDetails(
            emailAddress = emailAddress,
            password = password
        )

        val data = Json.encodeToString(loginDto)

        val apiKey = getLocalSaveApiKey()
        if (apiKey is Result.Error) {
            return apiKey
        }
        val rsaApiKey = (apiKey as Result.Success).data.publicRsaKey
        try {
            val encryptedData = CryptographicOperation.encryptData(
                serverPublicKey = rsaApiKey,
                data = data
            )
            val signature = CryptographicOperation.signData(
                dataToSign = encryptedData.aesEncryptedString
            )
            // ClientEC for signature
            //ServerRSA for decryption

            val clientEcKey = KeyPairGenerator.getClientKeyPair(EC_ALIAS)
            val clientRsaKey = KeyPairGenerator.getClientKeyPair(RSA_ALIAS)

            return authenticationRemote.login(
                encryptedData = encryptedData.aesEncryptedString,
                signature = signature,
                clientEcKey = clientEcKey,
                clientRsaKey = clientRsaKey,
                aesKey = encryptedData.rsaEncryptedKey

            )
                .map {
                    it.data!!.toData<LoggedInUser>()!!
                }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(RemoteError.ServerError)
        }


    }

    override suspend fun signUp(
        emailAddress: String,
        password: String,
        fullName: String,
        gender: String,
        phoneNumber: String,
        matricNumber: String
    ): Result<String, RemoteError> {

        val apiKey = getLocalSaveApiKey()
        if (apiKey is Result.Error) {
            return apiKey
        }


        try {

            val rsaApiKey = (apiKey as Result.Success).data.publicRsaKey

            println("full name $fullName")

            val splitName = fullName.split(" ")

            val firstName = splitName[0]
            val lastName = splitName[1]
            val middleName = try {
                splitName[2]
            } catch (error: Exception) {
                null
            }


            val signUpDetails = SignUpDetails(
                emailAddress = emailAddress,
                userName = "",
                password = password,
                firstName = firstName,
                lastName = lastName,
                middleName = middleName,
                matricNumber = matricNumber,
                phoneNumber = phoneNumber,
                gender = gender
            )

            val decodedSignUpDetails = Json.encodeToString(signUpDetails)

            val encryptedSignUpDetails = CryptographicOperation.encryptData(
                serverPublicKey = rsaApiKey,
                data = decodedSignUpDetails
            )
            val signature = CryptographicOperation.signData(
                dataToSign = encryptedSignUpDetails.aesEncryptedString
            )
            // ClientEC for signature
            //ServerRSA for decryption
            return authenticationRemote.signUp(
                encryptedData = encryptedSignUpDetails.aesEncryptedString,
                signature = signature,
                clientEcKey = KeyPairGenerator.getClientKeyPair(EC_ALIAS),
                clientRsaKey = KeyPairGenerator.getClientKeyPair(RSA_ALIAS),
                aesKey = encryptedSignUpDetails.rsaEncryptedKey

            )
                .onSuccess {
                    println("Successfull ${it.data}")
                    //
                }
                .map {
                    it.data!!
                }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(RemoteError.ServerError)
        }
    }

    override suspend fun getApiKey(): Result<PublicKey, RemoteError> {
        //MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEarQCbxeSJwdvJULg5gfr/M0SO8uZ 3+QJR1yaFVPgFg8I1uusJMBkvgi6twi5HGnrxRh8YvxH4D2LM39hMTzv5Q==
        //MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEarQCbxeSJwdvJULg5gfr/M0SO8uZ 3+QJR1yaFVPgFg8I1uusJMBkvgi6twi5HGnrxRh8YvxH4D2LM39hMTzv5Q==


        return authenticationRemote
            .getServerPublicKey()
            .onSuccess {
                val apiKey = it.data!!
                saveApiKey(apiKey)
            }
            .map {
                it.data!!
            }

    }

    override suspend fun requestOtp(
        emailAddress: String,
        purpose: String
    ): Result<String, RemoteError> {
        try {
            return authenticationRemote.sendOtp(
                emailAddress = emailAddress,
                purpose = purpose
            )
                .map {
                    it.data!!
                }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(error = RemoteError.UnKnownError(message = e.message?:"Unknown Error"))
        }
    }

    suspend fun getLocalSaveApiKey(): Result<PublicKey, RemoteError> {
        val savedRsa = pref.publicRsaKey

        val savedEc = pref.publicEcKey

        if (savedEc != null && savedRsa != null) {
            return Result.Success(data = PublicKey(publicEcKey = savedEc, publicRsaKey = savedRsa))
        }
        return getApiKey()

    }

    private fun saveApiKey(publicKey: PublicKey) {
        val existingEcKey = pref.publicEcKey
        if (publicKey.publicEcKey != existingEcKey) {
            pref.editPublicEcKey(publicKey.publicEcKey)

        }

        val existingRsaKey = pref.publicRsaKey
        if (publicKey.publicRsaKey != existingRsaKey) {
            pref.editPublicRsaKey(publicKey.publicRsaKey)


        }
    }

}