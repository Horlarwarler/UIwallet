package com.crezent.finalyearproject.routes

import com.crezent.finalyearproject.data.dto.*
import com.crezent.finalyearproject.data.repo.PaymentRepository
import com.crezent.finalyearproject.domain.util.DatabaseErrorToType
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.domain.util.toErrorMessage
import com.crezent.finalyearproject.routes.util.decryptVerifiedMessage
import com.crezent.finalyearproject.routes.util.getEmailFromJwt
import com.crezent.finalyearproject.utility.security.encryption.EncryptService
import com.crezent.finalyearproject.utility.security.encryption.SigningService
import com.crezent.finalyearproject.utility.security.hashing.HashingService
import com.crezent.finalyearproject.utility.security.hashing.SaltedHash
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Route.verifyCvv(
    rsaPrivateKeyString: String, // for decrypting
    paymentRepository: PaymentRepository,
    signingService: SigningService,
    hashingService: HashingService,
    ecPrivateKeyString: String,
    encryptService: EncryptService
) {
    authenticate() {
        post("verify-cvv") {
            val userEmail = getEmailFromJwt() ?: run {
                call.respond(HttpStatusCode.NotFound, "Email is required")
                return@post
            }
            val decryptVerifiedMessage = call.decryptVerifiedMessage<CvvVerification>(
                rsaPrivateKeyString = rsaPrivateKeyString,
                signingService = signingService,
                encryptService = encryptService,
                ecPrivateKeyString = ecPrivateKeyString
            )

            if (decryptVerifiedMessage !is Result.Success) {
                return@post
            }

            val decryptedMessage = decryptVerifiedMessage.data.data

            val cardResult = paymentRepository.getCardById(cardId = decryptedMessage.cardId, email = userEmail)
            if (cardResult is Result.Error) {
                call.respond(
                    HttpStatusCode.NotFound,
                    message = cardResult.error.toErrorMessage(errorToType = DatabaseErrorToType.CardError)
                )
                return@post
            }
            val card = (cardResult as Result.Success).data

            val cvv = decryptedMessage.cvv

            val cvvMatched = hashingService.inputIsCorrect(
                value = cvv,
                saltedHash = SaltedHash(salt = "", hashedValue = "")
            )
            if (!cvvMatched) {
                call.respond(
                    status = HttpStatusCode.NotFound,
                    "Cvv not valid"
                )
                return@post
            }
            call.respond(HttpStatusCode.OK, ServerResponse(data = "Verified"))


            //
        }
    }

}

fun Route.createCard(
    rsaPrivateKeyString: String, // for decrypting
    paymentRepository: PaymentRepository,
    signingService: SigningService,
    key: String,
    ecPrivateKeyString: String,
    encryptService: EncryptService
) {
    authenticate {
        post("create-card") {

            val userEmail = getEmailFromJwt() ?: run {
                call.respond(HttpStatusCode.NotFound, "User with this email not found")
                return@post
            }

            val decryptVerifiedMessage = call.decryptVerifiedMessage<CardDto>(
                rsaPrivateKeyString = rsaPrivateKeyString,
                signingService = signingService,
                encryptService = encryptService,
                ecPrivateKeyString = ecPrivateKeyString
            )

            if (decryptVerifiedMessage !is Result.Success) {
                return@post
            }

            val decryptedMessage = decryptVerifiedMessage.data
            val cardDetails = decryptedMessage.data
            val encryptedCard = EncryptedCard(
                cardNumber = cardDetails.cardNumber,
                holderName = cardDetails.holderName,
                expirationDate = cardDetails.expirationDate
            )

            val toString = Json.encodeToString(encryptedCard)

            val aesEncryptedString = encryptService.encryptCardDetails(
                data = toString,
                key = key
            )


            val result = paymentRepository.addCard(
                aesEncryptedString = aesEncryptedString.first,
                email = userEmail,
                iv = aesEncryptedString.second
            )
            if (result is Result.Error) {
                call.respond(
                    HttpStatusCode.NotModified,
                    message = result.error.toErrorMessage(errorToType = DatabaseErrorToType.CardError)
                )
                return@post
            }

            call.respond(HttpStatusCode.OK, ServerResponse(data = "Card Created"))
        }
    }
}

