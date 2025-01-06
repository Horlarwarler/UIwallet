package com.crezent.finalyearproject.authentication.data.repo

import com.crezent.finalyearproject.EC_ALIAS
import com.crezent.finalyearproject.RSA_ALIAS
import com.crezent.finalyearproject.authentication.data.AuthenticationRepo
import com.crezent.finalyearproject.authentication.data.network.AuthenticationRemote
import com.crezent.finalyearproject.core.data.security.encryption.CryptographicOperation
import com.crezent.finalyearproject.core.data.security.encryption.KeyPairGenerator
import com.crezent.finalyearproject.core.domain.preference.EncryptedSharePreference
import com.crezent.finalyearproject.core.domain.preference.SharedPreference
import com.crezent.finalyearproject.data.dto.LoggedInUser
import com.crezent.finalyearproject.data.dto.LoginDetails
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
        val rsaApiKey = pref.serverApiKey ?: kotlin.run {
            val result = getApiKey()
            if (result is Result.Error) {
                return result
            }
            (result as Result.Success).data
        }


        val loginDto = LoginDetails(
            emailAddress = emailAddress,
            password = password
        )

        val data = Json.encodeToString(loginDto)
        try {
            val encryptedData = CryptographicOperation.encryptData(
                serverPublicKey = rsaApiKey,
                data = data
            )
            val signature = CryptographicOperation.signData(
                dataToSign = encryptedData
            )
            // ClientEC for signature
            //ServerRSA for decryption
            return authenticationRemote.login(
                encryptedData = encryptedData,
                signature = signature,
                clientEcKey = KeyPairGenerator.getClientKeyPair(EC_ALIAS),
                clientRsaKey = KeyPairGenerator.getClientKeyPair(RSA_ALIAS)
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

    override suspend fun getApiKey(): Result<String, RemoteError> {
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

    private fun saveApiKey(key: String) {
        val existingKey = pref.serverApiKey
        if (key != existingKey) {
            pref.editServerApiKey(key)
            return

        }
    }

}