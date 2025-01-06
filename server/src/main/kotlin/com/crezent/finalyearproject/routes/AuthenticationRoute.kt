package com.crezent.finalyearproject.routes

import com.crezent.finalyearproject.data.database.entity.TokenEntity
import com.crezent.finalyearproject.data.database.entity.UserEntity
import com.crezent.finalyearproject.data.dto.*
import com.crezent.finalyearproject.data.repo.UIWalletRepository
import com.crezent.finalyearproject.domain.util.*
import com.crezent.finalyearproject.services.MailService
import com.crezent.finalyearproject.utility.security.encryption.EncryptService
import com.crezent.finalyearproject.utility.security.encryption.SigningService
import com.crezent.finalyearproject.utility.security.hashing.HashingService
import com.crezent.finalyearproject.utility.security.hashing.SaltedHash
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.types.ObjectId


fun Route.singIn(
    ecPrivateKeyString: String, // Use for signing value
    rsaPrivateKeyString: String, // Use for decrypting value
    signingService: SigningService,
    encryptService: EncryptService,
    uiWalletRepository: UIWalletRepository,
    hashingService: HashingService,
    serverEcPublicKey : String,
    serverRsaPublicKey :String

) {
    post("login") {


        val decryptVerifiedMessage = call.decryptVerifiedMessage<LoginDetails>(
            rsaPrivateKeyString = rsaPrivateKeyString,
            signingService = signingService,
            encryptService = encryptService
        )

        if (decryptVerifiedMessage !is Result.Success) {
            return@post
        }
        val loginDetails = decryptVerifiedMessage.data.data
        val rsaPublicKey = decryptVerifiedMessage.data.rsaPublicKey // Needed for encryption

        val userResult = uiWalletRepository.getUserById(
            matricNumber = null,
            emailAddress = loginDetails.emailAddress,
            objectId = null

        )

        if (userResult is Result.Error) {

            call.respond(
                HttpStatusCode.NotFound, ServerResponse(message = userResult.error.toErrorMessage(), data = null)
            )
            return@post
        }


        val user = (userResult as Result.Success).data


        //   call.respond(HttpStatusCode.OK, message = ServerResponse(data = user))  TO verification only

        val passwordMatch = hashingService.inputIsCorrect(
            value = loginDetails.password,
            saltedHash = SaltedHash(salt = "", hashedValue = user.hashedPassword)
        )

        if (!passwordMatch) {
            call.respond(HttpStatusCode.NotFound, ServerResponse(message = "Email or password not valid", data = null))
            return@post
        }

        val userString = Json.encodeToString(user.toLoggedInUser())


        val encryptUser = encryptService.encryptData(
            publicKeyString = rsaPublicKey,
            value = userString
        ) ?: run {
            call.respond(HttpStatusCode.Forbidden, "Unable to get user data, please check the device ")
            return@post
        }

        val signature = signingService.signData(
            data = encryptUser,
            privateKeyString = ecPrivateKeyString
        )

        //
        val encryptedModel = EncryptedModel(
            signature = signature,
            encryptedData = encryptUser,
            ecKey = serverEcPublicKey, // server  eckey needed ,
            rsaKey = serverRsaPublicKey // TODO
        )
        val serverResponse = ServerResponse(
            data = encryptedModel,
        )

        call.respond(HttpStatusCode.OK, serverResponse)

        println("Login Details $loginDetails")

    }
}


fun Route.getApiKey(
    serverPublicKey: String
) {
    println("Server Public key $serverPublicKey")
    get("public-key") {
        call.respond(HttpStatusCode.OK, ServerResponse(data = serverPublicKey))
    }
}

fun Route.signUp(
    serverPrivateKeyString: String, // Use for decrypting value
    signingService: SigningService,
    encryptService: EncryptService,
    uiWalletRepository: UIWalletRepository,
    hashingService: HashingService

) {
    post("sign-up") {

//        val decryptVerifiedMessage = call.decryptVerifiedMessage<SignUpDetails>(
//            serverPrivateKeyString = serverPrivateKeyString,
//            signingService = signingService,
//            encryptService = encryptService
//        )
//
//        if (decryptVerifiedMessage !is Result.Success) {
//
//            val errorMessage = (decryptVerifiedMessage as Result.Error).error.toErrorMessage()
//            call.respond(HttpStatusCode.NotAcceptable, errorMessage)
//            return@post
//        }
//        val signUpDetails = decryptVerifiedMessage.data

        val signUpDetails = call.receive<SignUpDetails>()


        val isEmailValid = ValidationUtils.isValidEmail(signUpDetails.emailAddress)
        if (!isEmailValid) {
            call.respond(HttpStatusCode.NotAcceptable, "Email Not Valid".toServerResponse())
            return@post
        }

        val phoneNumberValid = ValidationUtils.isValidPhoneNumber(signUpDetails.phoneNumber)
        if (!phoneNumberValid) {
            call.respond(HttpStatusCode.NotAcceptable, "Phone Number Not valid".toServerResponse())
        }

        val passwordValid = ValidationUtils.isPasswordStrong(signUpDetails.password)

        if (!passwordValid) {
            call.respond(
                HttpStatusCode.NotAcceptable,
                "Password Must of minimum of 8 letter, contain Capital Letter, number, special character".toServerResponse()
            )
            return@post

        }

        val userFromDatabaseResult = uiWalletRepository.getUserById(
            matricNumber = signUpDetails.matricNumber,
            objectId = null,
            emailAddress = signUpDetails.emailAddress
        )

        if (userFromDatabaseResult is Result.Success) {
            println("User With this matric already Exist")
            call.respond(HttpStatusCode.NotAcceptable, ServerResponse(message = "User Already Exist", data = null))
            return@post
        }

        println("User Result $userFromDatabaseResult")

        val resultError = (userFromDatabaseResult as Result.Error).error

        if (resultError !is DatabaseError.ItemNotFound) {
            call.respond(HttpStatusCode.NotModified, "Error Creating Account".toServerResponse())
            return@post
        }

        val token = (100000..999999).random().toString()

        val mailSendResult = MailService.sendVerificationToken(
            userEmail = signUpDetails.emailAddress,
            token = token
        )
        if (mailSendResult is Result.Error) {
            call.respond(HttpStatusCode.NotAcceptable, "Unable to send mail to your email address".toServerResponse())
            return@post
            //
        }


        val hashedPassword = hashingService.hashValue(value = signUpDetails.password, saltLength = 30)

        println("Finish Hashing")


        val userEntity = UserEntity(
            id = ObjectId(),
            userName = signUpDetails.userName,
            matricNumber = signUpDetails.matricNumber,
            wallet = null,
            gender = signUpDetails.gender,
            phoneNumberString = signUpDetails.phoneNumber,
            emailAddress = signUpDetails.emailAddress,
            emailAddressVerified = "false",
            firstName = signUpDetails.firstName,
            lastName = signUpDetails.lastName,
            middleName = signUpDetails.middleName,
            hashedPassword = hashedPassword.hashedValue,
            accountStatus = "in-active",
        )

        //


        val newUserResult = uiWalletRepository.addUser(user = userEntity)


        if (newUserResult is Result.Error) {
            call.respond(HttpStatusCode.NotImplemented, "Error Creating Account".toServerResponse())
            return@post
        }

        println("Finish Adding User")


        val addedUser = newUserResult as Result.Success

        val regex = Regex("\\w+}")
        val userId = regex.find(addedUser.data.toString())!!.value.dropLast(1)
        val tokenEntity = TokenEntity(
            userId = userId,
            hashedToken = token,
            purpose = "Verification",
            expiresAt = System.currentTimeMillis() + 5L * 60L * 1000L
        )

        uiWalletRepository.addToken(token = tokenEntity)

        println("Finish Adding Token")


        call.respond(HttpStatusCode.OK, "Please verify your email".toServerResponse())


    }
}


//Get User

fun Route.getUser(
    serverPrivateKeyString: String, // Use for decrypting value
    serverPublicKeyString: String, // Use for decrypting value
    signingService: SigningService,
    encryptService: EncryptService,
    uiWalletRepository: UIWalletRepository,
) {
    get("user") {
        //id ,, matric ,, email address
        val queryParameters = call.queryParameters

        val matric = queryParameters["matric"]

        val email = queryParameters["email"]

        val id = queryParameters["id"]


        val publicKey = call.request.header("public_key") ?: run {
            call.respond(HttpStatusCode.Forbidden, "Public key needed, please check the device ")
            return@get
        }

        val objectId = id?.let { ObjectId(id) }

        println("matric $matric")

        val userResult = uiWalletRepository.getUserById(
            matricNumber = matric,
            emailAddress = email,
            objectId = objectId

        )

        if (userResult is Result.Error) {

            call.respond(
                HttpStatusCode.NotFound, ServerResponse(message = userResult.error.toErrorMessage(), data = null)
            )
            return@get
        }


        val user = (userResult as Result.Success).data.toResponseUser()
        //   call.respond(HttpStatusCode.OK, message = ServerResponse(data = user))  TO verification only

        val userString = Json.encodeToString(user)


        val encryptUser = encryptService.encryptData(
            publicKeyString = publicKey,
            value = userString
        ) ?: run {
            call.respond(HttpStatusCode.Forbidden, "Unable to get user data, please check the device ")
            return@get
        }

        val signature = signingService.signData(
            data = encryptUser,
            privateKeyString = serverPrivateKeyString
        )

        val encryptedModel = EncryptedModel(
            signature = signature,
            encryptedData = encryptUser,
            ecKey = serverPublicKeyString,
            rsaKey = ""

        )
        val serverResponse = ServerResponse(
            data = encryptedModel,
        )

        call.respond(HttpStatusCode.OK, serverResponse)


    }
}


fun Route.verifyEmail(
    serverPrivateKeyString: String, // Use for decrypting value
    serverPublicKeyString: String, // Use for decrypting value
    signingService: SigningService,
    encryptService: EncryptService,
    uiWalletRepository: UIWalletRepository,
) {
    put("verify-mail") {

//        val decryptVerifiedMessage = call.decryptVerifiedMessage<TokenVerification>(
//            serverPrivateKeyString = serverPrivateKeyString,
//            signingService = signingService,
//            encryptService = encryptService,
//        )
//
//        if (decryptVerifiedMessage is Result.Error) {
//            val errorMessage = decryptVerifiedMessage.error.toErrorMessage()
//            call.respond(HttpStatusCode.NotAcceptable, errorMessage)
//            return@put
//        }
//
//        val publicKey = call.request.header("public_key") ?: run {
//            call.respond(HttpStatusCode.Forbidden, "Public key needed, please check the device ")
//            return@put
//        }
//        val tokenVerification = (decryptVerifiedMessage as Result.Success).data

        val tokenVerification = call.receive<TokenVerification>()

        val tokenEntityResult = uiWalletRepository.getTokenById(
            token = tokenVerification.token,
            userId = tokenVerification.userId
        )

        if (tokenEntityResult is Result.Error) {
            val errorMessage = tokenEntityResult.error.toErrorMessage()
            call.respond(HttpStatusCode.NotFound, errorMessage.toServerResponse())
            return@put
        }

        val token = (tokenEntityResult as Result.Success).data
        val tokenExpired = token.expiresAt < System.currentTimeMillis()
        if (tokenExpired) {
            call.respond(HttpStatusCode.BadRequest, ServerResponse(data = "Token Expired"))
            return@put
        }


        val tokenRemoveResult = uiWalletRepository.removeToken(
            objectId = token.id
        )

        if (tokenRemoveResult is Result.Error) {
            call.respond(
                HttpStatusCode.BadRequest,
                ServerResponse(message = "Issue from our side, please try again", data = null)
            )
            return@put
        }

        val userUpdateResult = uiWalletRepository.updateUserEmailVerify(
            isVerified = true,
            userId = ObjectId(tokenVerification.userId)
        )
        if (userUpdateResult is Result.Error) {
            call.respond(
                HttpStatusCode.BadRequest,
                ServerResponse(message = "Issue from our side, please try again", data = null)
            )
            return@put
        }
        val user = (userUpdateResult as Result.Success).data.toResponseUser()
        call.respond(HttpStatusCode.OK, message = ServerResponse(data = user)) // TO verification only

        //val userString = Json.encodeToString(user)

//
//        val encryptUser = encryptService.encryptData(
//            publicKeyString = publicKey,
//            value = userString
//        ) ?: run {
//            call.respond(HttpStatusCode.Forbidden, "Unable to get user data, please check the device ")
//            return@put
//        }
//
//        val signature = signingService.signData(
//            data = encryptUser,
//            privateKeyString = serverPrivateKeyString
//        )
//
//        val encryptedModel = EncryptedModel(
//            signature = signature,
//            encryptedData = encryptUser,
//            publicKey = serverPublicKeyString
//
//        )
//        val serverResponse = ServerResponse(
//            data = encryptedModel,
//        )
//
//        call.respond(HttpStatusCode.OK, serverResponse)


    }
}