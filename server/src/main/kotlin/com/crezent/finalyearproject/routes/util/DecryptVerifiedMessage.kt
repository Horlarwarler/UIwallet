package com.crezent.finalyearproject.routes.util

import com.crezent.finalyearproject.data.dto.EncryptedModel
import com.crezent.finalyearproject.domain.util.*
import com.crezent.finalyearproject.models.PubicKeyWithDecrypted
import com.crezent.finalyearproject.utility.security.encryption.EncryptService
import com.crezent.finalyearproject.utility.security.encryption.SigningService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

suspend inline fun <reified T> ApplicationCall.decryptVerifiedMessage(
    rsaPrivateKeyString: String, // Use for decrypting value for android
    signingService: SigningService,
    encryptService: EncryptService,
    ecPrivateKeyString :String
): Result<PubicKeyWithDecrypted<T>, RemoteError> {
    return try {
        val encryptedDataReceive = receive<EncryptedModel>()

        val signature = encryptedDataReceive.signature
        val encryptedData = encryptedDataReceive.encryptedData
        val clientPublicEcKey = encryptedDataReceive.ecKey
        val aesKey = encryptedDataReceive.aesKey
        println("Reached Here ")
        val isVerified = signingService.verifySignature(
            signatureString = signature,
            data = encryptedData,
            ecPublicKeyString = clientPublicEcKey
        )
        println("After verify Reached Here ")


        if (!isVerified) {
            respond(status = HttpStatusCode.BadRequest, message = "Please check your device ")
            return Result.Error(error = RemoteError.InvalidSignature)
        }

        val decryptedMessage = encryptService.decryptData(
            aesEncryptedString = encryptedData,
            rsaPrivateKeyString = rsaPrivateKeyString,
            rsaEncryptedKey = aesKey,
        ) ?: run {
            respond(status = HttpStatusCode.BadRequest, message = "Bad body attached")
            return Result.Error(error = RemoteError.EncryptDecryptError)
        }

        val decodedData = Json.decodeFromString<T>(decryptedMessage)

        println("DATA IS $decodedData")

        Result.Success(
            data = PubicKeyWithDecrypted(
                data = decodedData,
                //  ecPublicKey = encryptedDataReceive.ecKey,
                rsaPublicKey = encryptedDataReceive.rsaKey
            )
        )

    } catch (e: SerializationException) {
        e.printStackTrace()
        Result.Error(error = RemoteError.SerializationException)
    } catch (e: ContentTransformationException) {
        e.printStackTrace()
        Result.Error(error = RemoteError.BadTransformation)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(error = RemoteError.UnKnownError(message = e.message ?: "Unknown Error"))

    }

}