package com.crezent.finalyearproject.routes

import com.crezent.finalyearproject.data.dto.*
import com.crezent.finalyearproject.data.repo.PayStackRepository
import com.crezent.finalyearproject.data.repo.PaymentRepository
import com.crezent.finalyearproject.data.repo.UIWalletRepository
import com.crezent.finalyearproject.domain.util.DatabaseErrorToType
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.domain.util.toErrorMessage
import com.crezent.finalyearproject.routes.util.decryptVerifiedMessage
import com.crezent.finalyearproject.routes.util.getEmailFromJwt
import com.crezent.finalyearproject.transaction.FundingSourceDto
import com.crezent.finalyearproject.transaction.TransactionDto
import com.crezent.finalyearproject.transaction.TransactionStatus
import com.crezent.finalyearproject.transaction.TransactionType
import com.crezent.finalyearproject.utility.security.encryption.EncryptService
import com.crezent.finalyearproject.utility.security.encryption.SigningService
import com.crezent.finalyearproject.utility.security.hashing.HashingService
import com.crezent.finalyearproject.utility.security.hashing.SaltedHash
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
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
    encryptService: EncryptService,

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

fun Route.initiateDeposit(
    payStackSecretKey: String,
    payStackRepository: PayStackRepository,
    paymentRepository: PaymentRepository,
) {
    authenticate() {
        post("initiate-transaction") {
            try {

                val initiateTransactionBody = call.receive<InitiateTransactionBody>()


                println("-------------------")
                println("Transaction Transaction Body $initiateTransactionBody")

                println("-------------------")

                val initiateTransactionResult = payStackRepository.initializeTransaction(
                    secretKey = payStackSecretKey,
                    initiateTransactionBody = initiateTransactionBody
                )

                println("-------------------")
                println("Transaction Transaction Body Result $initiateTransactionResult")

                println("-------------------")
                if (initiateTransactionResult is Result.Error) {
                    val error = initiateTransactionResult.error.toErrorMessage()
                    call.respond(HttpStatusCode.Forbidden, error)
                    return@post
                }

                val response = (initiateTransactionResult as Result.Success).data

                if (!response.status) {
                    call.respond(HttpStatusCode.NotModified, response.message)
                    return@post
                }

                val initiateTransactionResponseDto = InitiateTransactionResponseDto(
                    referenceCode = response.data.reference,
                    authorizationUrl = response.data.authorization_url
                )

                call.respond(HttpStatusCode.OK, ServerResponse(data = initiateTransactionResponseDto))


            } catch (error: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
            }

        }
    }

}


fun Route.depositPayment(
    payStackSecretKey: String,
    payStackRepository: PayStackRepository,
    paymentRepository: PaymentRepository,
) {
    //TODO
}

fun Route.verifyDeposit(
    payStackSecretKey: String,
    payStackRepository: PayStackRepository,
    paymentRepository: PaymentRepository,
    signingService: SigningService,
    serverPrivateKeyString: String, // Use for decrypting value
    serverPublicKeyString: String, // Use for verifying
) {
    authenticate() {
        get("verify-deposit") {

            val paymentRef = call.queryParameters["reference"] ?: run {
                println("VERIFY DEPOSIT IS NOT REFERENCE")
                call.respond(HttpStatusCode.BadRequest, "Payment Ref needed")
                return@get
            }
            val emailAddress = call.queryParameters["email"] ?: run {
                println("VERIFY DEPOSIT IS NOT EMAIL")
                call.respond(HttpStatusCode.BadRequest, "Email Address Needed")
                return@get
            }
            val depositResult = payStackRepository.verifyTransactionStatus(
                secretKey = payStackSecretKey,
                paymentRef = paymentRef
            )
            if (depositResult is Result.Error) {
                val error = depositResult.error.toErrorMessage()
                println("Error $error")
                call.respond(HttpStatusCode.NotFound, error)
                return@get
            }

            val transactionData = (depositResult as Result.Success).data.data
            println("----------Transaction Result ----------")
            println(transactionData)
            println("----------End of Transaction Result ----------")

            // val transactionData = transactionResult.data.data
            val amount = transactionData.amount
            val channel = transactionData.channel
            val status = transactionData.status
            if (status != "success") {
                call.respond(HttpStatusCode.NotFound, "Transaction $status")
                return@get
            }


            val fundingSource: FundingSourceDto = when (channel) {
                "bank_transfer" -> FundingSourceDto.BankTransfer(
                    accountNumber = transactionData.authorization.last4!!,
                    accountName = transactionData.authorization.account_name!!
                )

                "card" -> FundingSourceDto.CardPayment(
                    lastFourDigit = transactionData.authorization.last4!!,
                    bank = transactionData.authorization.bank!!,
                    cardType = transactionData.authorization.card_type!!
                )

                "ussd" -> FundingSourceDto.UssdPayment(
                    bank = transactionData.authorization.bank!!
                )

                "bank" -> FundingSourceDto.Bank(
                    bank = transactionData.authorization.bank!!,
                    lastFourDigit = transactionData.authorization.last4!!
                )

                else -> return@get
            }
            val transactionTitle = if (status == "success") "Transaction Successful" else "Transaction Failed"

            val transactionDto = TransactionDto(
                transactionTitle = transactionTitle,
                transactionDescription = "Deposit $amount via ${fundingSource.channel}",
                transactionAmount = amount.toDouble() / 100,
                transactionStatus = TransactionStatus.valueOf(status.capitalize()),
                transactionType = TransactionType.Credit,
                paidAt = transactionData.paidAt!!,
                fundingSourceDto = fundingSource,
                createdDate = transactionData.created_at,
                reference = transactionData.reference
            )

            val transactionResult =
                paymentRepository.addTransaction(transactionDto = transactionDto, email = emailAddress)

            if (transactionResult is Result.Error) {

                call.respond(
                    HttpStatusCode.NotModified,
                    transactionResult.error.toErrorMessage(DatabaseErrorToType.TransactionError)
                )
                return@get
            }

            val transactionId = (transactionResult as Result.Success)

            val transaction = transactionDto.copy(transactionId = transactionId.data)
            val transactionJson = Json.encodeToString(transaction)
            val signature = signingService.signData(
                data = transactionJson,
                privateKeyString = serverPrivateKeyString
            )

            val signatureModel = SignatureModel(
                signature = signature,
                data = transactionJson,
                ecKey = serverPublicKeyString
            )
            call.respond(HttpStatusCode.OK, ServerResponse(data = signatureModel))

        }
    }

}


fun Route.payStackPaymentSuccessCallBack(
    //  payStackSecretKey: String
) {

    get("callback") {
        println("----------Transaction Result  Success----------")

        println("----------End of Transaction Result ----------")
        //val queryParameters = call.queryParameters["email"] ?: "guest"
    }
}

fun Route.payStackPaymentCancelCallBack() {
    get("cancel-payment") {
        println("----------Transaction Result  ERROR----------")

        println("----------End of Transaction Result ----------")
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


fun Route.submitFeeRequest(
    payStackSecretKey: String,
    payStackRepository: PayStackRepository,
    paymentRepository: PaymentRepository,
    signingService: SigningService,
    serverPrivateKeyString: String, // Use for decrypting value
    serverPublicKeyString: String, // Use for verifying
    uiWalletRepository: UIWalletRepository
) {
    // Submit Reuest
    // val feeRequest = ca
    // Fee Amount, Session, Semester
    //Check if user has that amount in his wallet
    //Check for all transaction made by the user on paystack
    // Check for all deposit transaction amount
    //verify all the three
    // deduct the amount
    //submit request to the admin
    // return corresponding message
    authenticate() {

        post("submit-fee") {
            val userEmail = getEmailFromJwt() ?: run {
                call.respond(HttpStatusCode.NotFound, "Email is required")
                return@post
            }
            val feeRequest = call.receive<FeeRequest>()
            val userResult = uiWalletRepository.getUserById(emailAddress = userEmail)
            if (userResult is Result.Error) {
                call.respond(HttpStatusCode.NotFound, userResult.error.toErrorMessage(DatabaseErrorToType.UserError))
                return@post
            }

            val user = userResult as Result.Success
            //Check if user has that amount in his wallet
            val depositAmount = user.data.wallet?.accountBalance ?: 0.0

            if (depositAmount <= 0 || depositAmount < feeRequest.feeAmount) {
                call.respond(HttpStatusCode.BadRequest, "Account Balance is low")
                return@post
            }

            //Check for all transaction made by the user on paystack

            val paymentTransactionByUser = payStackRepository.getTransactions(
                secretKey = payStackSecretKey,
                customerId =
            )




        }
    }

}

