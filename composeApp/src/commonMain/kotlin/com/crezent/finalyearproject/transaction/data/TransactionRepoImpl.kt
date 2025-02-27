package com.crezent.finalyearproject.transaction.data

import com.crezent.finalyearproject.EC_ALIAS
import com.crezent.finalyearproject.RSA_ALIAS
import com.crezent.finalyearproject.authentication.data.network.AuthenticationRemote
import com.crezent.finalyearproject.core.data.mapper.toTransaction
import com.crezent.finalyearproject.core.data.security.encryption.CryptographicOperation
import com.crezent.finalyearproject.core.data.security.encryption.KeyPairGenerator
import com.crezent.finalyearproject.core.domain.BaseAppRepo
import com.crezent.finalyearproject.core.domain.model.Card
import com.crezent.finalyearproject.core.domain.model.Transaction
import com.crezent.finalyearproject.core.domain.preference.EncryptedSharePreference
import com.crezent.finalyearproject.core.domain.preference.SharedPreference
import com.crezent.finalyearproject.data.dto.CardDto
import com.crezent.finalyearproject.data.dto.CvvVerification
import com.crezent.finalyearproject.data.dto.InitiateTransactionBody
import com.crezent.finalyearproject.domain.util.RemoteError
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.domain.util.map
import com.crezent.finalyearproject.domain.util.onSuccess
import com.crezent.finalyearproject.transaction.TransactionDto
import com.crezent.finalyearproject.transaction.domain.TransactionRepo
import com.crezent.finalyearproject.transaction.domain.model.InitiateTransactionResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TransactionRepoImpl(
    private val transactionApi: TransactionApi,
    private val keyPairGenerator: KeyPairGenerator,
    private val cryptographicOperation: CryptographicOperation,
    private val encryptedSharePreference: EncryptedSharePreference,
    private val baseAppRepo: BaseAppRepo
) : TransactionRepo {
    override suspend fun verifyCvv(
        cvv: String,
        id: String
    ): Result<Unit, RemoteError> {

        val cvvVerification = CvvVerification(
            cvv = cvv,
            cardId = id
        )
        val cvvVerificationToString = Json.encodeToString(cvvVerification)
        val apiKey = baseAppRepo.getLocalSaveApiKey()
        if (apiKey is Result.Error) {
            return apiKey
        }
        val rsaApiKey = (apiKey as Result.Success).data.publicRsaKey // TODO

        try {
            val clientEcKey = keyPairGenerator.getClientKeyPair(EC_ALIAS)
            val clientRsaKey = keyPairGenerator.getClientKeyPair(RSA_ALIAS)
            val encryptedCvv = cryptographicOperation.encryptData(
                serverPublicKey = rsaApiKey,
                data = cvvVerificationToString
            )
            val signature = encryptedCvv?.aesEncryptedString?.let {
                cryptographicOperation.signData(
                    dataToSign = it
                )
            } ?: return Result.Error(error = RemoteError.EncryptDecryptError)
            return transactionApi.verifyCvv(
                encryptedData = encryptedCvv.aesEncryptedString,
                clientEcKey = clientEcKey,
                clientRsaKey = clientRsaKey,
                signature = signature,
                aesKey = encryptedCvv.rsaEncryptedKey,
                bearerToken = encryptedSharePreference.getAuthToken!!

            )
                .map {
                }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(RemoteError.ServerError)
        }
    }

    override suspend fun createCard(
        cardCvv: String,
        cardHolderName: String,
        cardExpirationDate: String,
        cardNumber: String
    ): Result<Unit, RemoteError> {
        val card = CardDto(
            holderName = cardHolderName,
            cardNumber = cardNumber,
            cardCvv = cardCvv,
            expirationDate = cardExpirationDate
        )
        val cardToString = Json.encodeToString(card)
        val apiKey = baseAppRepo.getLocalSaveApiKey()
        if (apiKey is Result.Error) {
            return apiKey
        }
        val rsaApiKey = (apiKey as Result.Success).data.publicRsaKey // TODO


        try {

            val clientEcKey = keyPairGenerator.getClientKeyPair(EC_ALIAS)
            val clientRsaKey = keyPairGenerator.getClientKeyPair(RSA_ALIAS)
            val encryptedCard = cryptographicOperation.encryptData(
                serverPublicKey = rsaApiKey,
                data = cardToString
            )
            val signature = encryptedCard?.aesEncryptedString?.let {
                cryptographicOperation.signData(
                    dataToSign = it
                )
            } ?: return Result.Error(error = RemoteError.EncryptDecryptError)
            return transactionApi.createCard(
                encryptedData = encryptedCard.aesEncryptedString,
                clientEcKey = clientEcKey,
                clientRsaKey = clientRsaKey,
                signature = signature,
                aesKey = encryptedCard.rsaEncryptedKey,
                bearerToken = encryptedSharePreference.getAuthToken!!
            )
                .map {

                }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(RemoteError.ServerError)
        }
    }

    override suspend fun verifyPayment(
        emailAddress: String,
        reference: String
    ): Result<Transaction, RemoteError> {
        println("Payment REPO reference is $reference , logged in email is $emailAddress")

        return transactionApi
            .verifyTransaction(
                reference = reference,
                emailAddress = emailAddress,
                bearerToken = encryptedSharePreference.getAuthToken!!
            )
            .onSuccess {
                val signature = it.data!!.signature
                val data = it.data!!.data
                val ecKey = it.data!!.ecKey
                val isValid = cryptographicOperation.verifySignature(
                    signature = signature,
                    dataToVerify = data,
                    publicKey = ecKey
                )
                if (!isValid) {
                    return Result.Error(
                        error = RemoteError.InvalidSignature
                    )
                }

            }

            .map {
                val data = it.data!!.data
                Json.decodeFromString<TransactionDto>(data).toTransaction()

            }

    }

    override suspend fun initiatePayment(initiateTransactionBody: InitiateTransactionBody): Result<InitiateTransactionResponse, RemoteError> {

        return transactionApi.initiatePayment(
            initiateTransactionBody = initiateTransactionBody,
            bearerToken = encryptedSharePreference.getAuthToken!!
        )
    }

}