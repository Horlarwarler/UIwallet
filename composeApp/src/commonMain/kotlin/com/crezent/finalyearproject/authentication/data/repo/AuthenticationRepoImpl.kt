package com.crezent.finalyearproject.authentication.data.repo

import com.crezent.finalyearproject.EC_ALIAS
import com.crezent.finalyearproject.RSA_ALIAS
import com.crezent.finalyearproject.authentication.data.AuthenticationRepo
import com.crezent.finalyearproject.authentication.data.network.AuthenticationRemote
import com.crezent.finalyearproject.core.data.security.encryption.CryptographicOperation
import com.crezent.finalyearproject.core.data.security.encryption.KeyPairGenerator
import com.crezent.finalyearproject.core.data.util.toData
import com.crezent.finalyearproject.core.domain.BaseAppRepo
import com.crezent.finalyearproject.core.domain.preference.EncryptedSharePreference
import com.crezent.finalyearproject.core.domain.preference.EncryptedSharePreference.Companion.OTP_TOKEN
import com.crezent.finalyearproject.core.domain.preference.SharedPreference
import com.crezent.finalyearproject.data.dto.LoggedInUser
import com.crezent.finalyearproject.data.dto.LoginDetails
import com.crezent.finalyearproject.data.dto.PublicKey
import com.crezent.finalyearproject.data.dto.ResetPassword
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
    private val authenticationRemote: AuthenticationRemote,
    private val keyPairGenerator: KeyPairGenerator,
    private val cryptographicOperation: CryptographicOperation,
    private val baseAppRepo: BaseAppRepo
) : AuthenticationRepo {

    override suspend fun login(
        emailAddress: String,
        password: String
    ): Result<Unit, RemoteError> {


        val loginDto = LoginDetails(
            emailAddress = emailAddress,
            password = password
        )

        val data = Json.encodeToString(loginDto)

        val apiKey = baseAppRepo.getLocalSaveApiKey()
        if (apiKey is Result.Error) {
            return apiKey
        }
        val rsaApiKey = (apiKey as Result.Success).data.publicRsaKey // TODO

        try {
            val encryptedData = cryptographicOperation.encryptData(
                serverPublicKey = rsaApiKey,
                data = data
            )
            val signature = encryptedData?.aesEncryptedString?.let {
                cryptographicOperation.signData(
                    dataToSign = it
                )
            } ?: return Result.Error(error = RemoteError.EncryptDecryptError)
            // ClientEC for signature
            //ServerRSA for decryption

            val clientEcKey = keyPairGenerator.getClientKeyPair(EC_ALIAS)
            val clientRsaKey = keyPairGenerator.getClientKeyPair(RSA_ALIAS)
            println("Encrypted Data $encryptedData, Signature is $signature")

            return authenticationRemote.login(
                encryptedData = encryptedData.aesEncryptedString,
                signature = signature,
                clientEcKey = clientEcKey,
                clientRsaKey = clientRsaKey,
                aesKey = encryptedData.rsaEncryptedKey
            )
                .onSuccess {
                    println("token is ${it.data}")
                    encryptedPref.editAuthToken(it.data!!)
                }
                .map {

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

        val apiKey = baseAppRepo.getLocalSaveApiKey()
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

            val encryptedSignUpDetails = cryptographicOperation.encryptData(
                serverPublicKey = rsaApiKey,
                data = decodedSignUpDetails
            )
            val signature = cryptographicOperation.signData(
                dataToSign = encryptedSignUpDetails?.aesEncryptedString
                    ?: return Result.Error(error = RemoteError.EncryptDecryptError)
            )
            // ClientEC for signature
            //ServerRSA for decryption
            return authenticationRemote.signUp(
                encryptedData = encryptedSignUpDetails.aesEncryptedString,
                signature = signature!!,
                clientEcKey = keyPairGenerator.getClientKeyPair(EC_ALIAS),
                clientRsaKey = keyPairGenerator.getClientKeyPair(RSA_ALIAS),
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
            return Result.Error(
                error = RemoteError.UnKnownError(
                    message = e.message ?: "Unknown Error"
                )
            )
        }
    }

    override suspend fun verifyEmail(

    ): Result<Unit, RemoteError> {
        try {

            val clientRsaKey = keyPairGenerator.getClientKeyPair(RSA_ALIAS)
            return authenticationRemote.verifyEmail(
                bearerToken = encryptedPref.otpToken!!,
                clientRsaKey = clientRsaKey,
            )
                .onSuccess {
                    encryptedPref.editAuthToken(it.data!!)
                    encryptedPref.deleteKey(OTP_TOKEN)
                }
                .map {

                }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(RemoteError.ServerError)
        }
    }

    override suspend fun verifyOtp(
        emailAddress: String,
        purpose: String,
        otp: String
    ): Result<Unit, RemoteError> {
        try {

            return authenticationRemote.verifyOtp(
                emailAddress = emailAddress,
                otp = otp,
                purpose = purpose
            )
                .map {
                    val token = it.data!!
                    encryptedPref.saveOtpToken(token)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(RemoteError.ServerError)
        }
    }

    override suspend fun resetPassword(newPassword: String): Result<Unit, RemoteError> {
        try {


            val apiKey = baseAppRepo.getLocalSaveApiKey()
            if (apiKey is Result.Error) {
                return apiKey
            }
            val serverRsaKey = (apiKey as Result.Success).data.publicRsaKey
            val resetPassword = ResetPassword(password = newPassword)
            val jsonToString = Json.encodeToString(resetPassword)
            val encryptedData = cryptographicOperation.encryptData(
                serverPublicKey = serverRsaKey,
                data = jsonToString
            )
            val signature = encryptedData?.aesEncryptedString?.let {
                cryptographicOperation.signData(
                    dataToSign = it
                )
            } ?: return Result.Error(error = RemoteError.InvalidSignature)
            val clientEcKey = keyPairGenerator.getClientKeyPair(EC_ALIAS)
            val clientRsaKey = keyPairGenerator.getClientKeyPair(RSA_ALIAS)


            return authenticationRemote.resetPassword(
                bearerToken = encryptedPref.otpToken!!,
                clientRsaKey = clientRsaKey,
                encryptedData = encryptedData.aesEncryptedString,
                signature = signature,
                clientEcKey = clientEcKey,
                aesKey = encryptedData.rsaEncryptedKey
            )
                .map {

                }
                .onSuccess {
                    encryptedPref.deleteKey(OTP_TOKEN)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(RemoteError.ServerError)
        }
    }


}