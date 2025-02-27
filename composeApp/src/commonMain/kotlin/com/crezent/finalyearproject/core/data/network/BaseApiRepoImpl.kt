package com.crezent.finalyearproject.core.data.network

import com.crezent.finalyearproject.RSA_ALIAS
import com.crezent.finalyearproject.core.data.mapper.toTransaction
import com.crezent.finalyearproject.core.data.security.encryption.CryptographicOperation
import com.crezent.finalyearproject.core.data.security.encryption.KeyPairGenerator
import com.crezent.finalyearproject.core.domain.BaseAppRepo
import com.crezent.finalyearproject.core.domain.model.Card
import com.crezent.finalyearproject.core.domain.model.FundingSource
import com.crezent.finalyearproject.core.domain.model.Transaction
import com.crezent.finalyearproject.core.domain.model.User
import com.crezent.finalyearproject.core.domain.model.Wallet
import com.crezent.finalyearproject.core.domain.preference.EncryptedSharePreference
import com.crezent.finalyearproject.core.domain.preference.SharedPreference
import com.crezent.finalyearproject.core.presentation.SharedData
import com.crezent.finalyearproject.data.dto.CardResponse
import com.crezent.finalyearproject.data.dto.LoggedInUser
import com.crezent.finalyearproject.data.dto.PublicKey
import com.crezent.finalyearproject.data.dto.WalletDto
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.domain.util.map
import com.crezent.finalyearproject.domain.util.onSuccess
import com.crezent.finalyearproject.transaction.FundingSourceDto
import com.crezent.finalyearproject.transaction.FundingSourceDto.BankTransfer
import com.crezent.finalyearproject.transaction.FundingSourceDto.CardPayment
import com.crezent.finalyearproject.transaction.FundingSourceDto.UssdPayment
import com.crezent.finalyearproject.transaction.TransactionDto
import kotlinx.serialization.json.Json
import kotlin.collections.map

class BaseApiRepoImpl(
    private val baseApi: BaseApi,
    private val pref: SharedPreference,
    private val encryptedPref: EncryptedSharePreference,
    private val keyPairGenerator: KeyPairGenerator,
    private val cryptographicOperation: CryptographicOperation,

    ) : BaseAppRepo {
    override suspend fun getApiKey(): Result<PublicKey, RemoteError> {
        return baseApi
            .getServerPublicKey()
            .onSuccess {
                val apiKey = it.data!!
                saveApiKey(apiKey)
            }
            .map {
                it.data!!
            }

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

    override suspend fun getAndCacheAuthenticatedUser(

    ): Result<User, RemoteError> {
        val clientRsaKey = keyPairGenerator.getClientKeyPair(RSA_ALIAS)

        try {
            return baseApi.getAuthenticatedUser(
                bearerToken = encryptedPref.getAuthToken ?: "",
                rsaPublicKey = clientRsaKey
            )
                .map {
                    val encrypted = it.data!!.encryptedData
                    val verify = cryptographicOperation.verifySignature(
                        signature = it.data!!.signature,
                        dataToVerify = encrypted,
                        publicKey = it.data!!.ecKey
                    )
                    if (!verify) {
                        return Result.Error(error = RemoteError.InvalidSignature)
                    }
                    val decryptString = cryptographicOperation.decryptData(
                        encryptedData = encrypted,
                        encryptedAesKey = it.data!!.aesKey
                    )!!

                    val loggedInUser = Json.decodeFromString<LoggedInUser>(decryptString).toUser()
                    loggedInUser

                }
                .onSuccess {

                    val fullName = "${it.firstName} ${it.middleName} ${it.lastName}"
                    if (pref.userFullName != fullName) {
                        pref.saveUserFullName(fullName)
                    }
                    pref.updateBalance(it.wallet!!.accountBalance)
                    SharedData.editLoggedInUser(
                        user = it
                    )
                    // Cache the transaction list  in to database

                    //
                }
        } catch (error: Error) {
            error.printStackTrace()
            return Result.Error(error = RemoteError.UnKnownError(error.message ?: "Unknown Error"))
        }


    }


    override suspend fun getLocalSaveApiKey(): Result<PublicKey, RemoteError> {
        val savedRsa = pref.publicRsaKey

        val savedEc = pref.publicEcKey

        if (savedEc != null && savedRsa != null) {
            return Result.Success(data = PublicKey(publicEcKey = savedEc, publicRsaKey = savedRsa))
        }
        return getApiKey()

    }


}


private fun LoggedInUser.toUser(): User {
    return User(
        id = id,
        matricNumber = matricNumber,
        phoneNumberString = phoneNumberString,
        emailAddress = emailAddress,
        firstName = firstName,
        lastName = lastName,
        middleName = middleName,
        emailAddressVerified = emailAddressVerified,
        accountActive = accountActive,
        accountDeactivationReason = accountDeactivationReason,
        wallet = walletDto?.toWallet(),
        connectedCards = connectedCards.map {
            it.toCard()
        }
    )
}

private fun WalletDto.toWallet(): Wallet {
    return Wallet(
        walletId = walletId,
        accountBalance = accountBalance,
        transactions = transactionDtos.map {
            it.toTransaction()
        }
    )
}

private fun CardResponse.toCard(): Card {
    return Card(
        cardId = id,
        lastFourDigit = lastFourCharacter,
        holderName = holderName
    )
}