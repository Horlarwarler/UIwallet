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
    serverEcPublicKey: String,
    serverRsaPublicKey: String

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
                HttpStatusCode.NotFound, message = userResult.error.toErrorMessage()
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
            call.respond(HttpStatusCode.NotFound, message = "Invalid Username or Password")
            return@post
        }

        val userString = Json.encodeToString(user.toLoggedInUser())


        val encryptUser = encryptService.encryptData(
            clientRsapublicKey = rsaPublicKey,
            value = userString
        ) ?: run {
            call.respond(HttpStatusCode.Forbidden, "Unable to get user data, please check the device")
            return@post
        }

        val signature = signingService.signData(
            data = encryptUser.aesEncryptedString,
            privateKeyString = ecPrivateKeyString
        )

        //
        val encryptedModel = EncryptedModel(
            signature = signature,
            encryptedData = encryptUser.aesEncryptedString,
            ecKey = serverEcPublicKey, // server  eckey needed ,
            rsaKey = serverRsaPublicKey,
            aesKey = encryptUser.rsaEncryptedKey
        )
        val serverResponse = ServerResponse(
            data = encryptedModel,
        )

        call.respond(HttpStatusCode.OK, serverResponse)

        println("Login Details $loginDetails")

    }
}


fun Route.getApiKey(
    rsaPublicKey: String,
    ecPublicKey: String
) {
    println("Server RSA Public key $rsaPublicKey EC  $ecPublicKey")
    get("public-key") {
        call.respond(
            HttpStatusCode.OK,
            ServerResponse(data = PublicKey(publicEcKey = ecPublicKey, publicRsaKey = rsaPublicKey))
        )
    }
}

fun Route.signUp(
    rsaPrivateKeyString: String, // Use for decrypting value
    signingService: SigningService,
    encryptService: EncryptService,
    uiWalletRepository: UIWalletRepository,
    hashingService: HashingService,


    ) {
    post("sign-up") {

        val decryptVerifiedMessage = call.decryptVerifiedMessage<SignUpDetails>(
            rsaPrivateKeyString = rsaPrivateKeyString,
            signingService = signingService,
            encryptService = encryptService
        )
        if (decryptVerifiedMessage !is Result.Success) {

            val errorMessage = (decryptVerifiedMessage as Result.Error).error.toErrorMessage()
            call.respond(HttpStatusCode.NotAcceptable, errorMessage)
            return@post
        }
        val signUpDetails = decryptVerifiedMessage.data.data

//        val signUpDetails = call.receive<SignUpDetails>()


        val isEmailValid = ValidationUtils.isValidEmail(signUpDetails.emailAddress)
        if (!isEmailValid) {
            call.respond(HttpStatusCode.NotAcceptable, "Email Address Not valid")
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
            call.respond(
                HttpStatusCode.NotAcceptable,
                message = "User With this email, matric number or phone number already exist"
            )
            return@post
        }

        println("User Result $userFromDatabaseResult")

        val resultError = (userFromDatabaseResult as Result.Error).error

        if (resultError !is DatabaseError.ItemNotFound) {
            call.respond(HttpStatusCode.NotModified, "Error Creating Account")
            return@post
        }

        val token = (100000..999999).random().toString()

        val mailSendResult = MailService.sendVerificationToken(
            userEmail = signUpDetails.emailAddress,
            token = token
        )
        if (mailSendResult is Result.Error) {
            call.respond(HttpStatusCode.NotAcceptable, "Unable to send mail to your email address")
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
            call.respond(HttpStatusCode.NotImplemented, "Error Creating Account")
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



        call.respond(HttpStatusCode.OK, ServerResponse(data = "Please verify your email"))


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
            clientRsapublicKey = publicKey,
            value = userString
        ) ?: run {
            call.respond(HttpStatusCode.Forbidden, "Unable to get user data, please check the device ")
            return@get
        }

        val signature = signingService.signData(
            data = encryptUser.aesEncryptedString,
            privateKeyString = serverPrivateKeyString
        )

        val encryptedModel = EncryptedModel(
            signature = signature,
            encryptedData = encryptUser.aesEncryptedString,
            ecKey = serverPublicKeyString,
            rsaKey = "",
            aesKey = encryptUser.rsaEncryptedKey

        )
        val serverResponse = ServerResponse(
            data = encryptedModel,
        )

        call.respond(HttpStatusCode.OK, serverResponse)


    }
}


fun Route.verifyEmail(

    uiWalletRepository: UIWalletRepository,
) {
    put("verify-mail") {

        val tokenVerification = call.receive<TokenVerification>()

        val token = call.queryParameters["token"]
        val userId = call.queryParameters["user_id"]
        if (token == null || userId == null) {
            call.respond(HttpStatusCode.NotAcceptable, "User ID and token is required")
            return@put
        }

        val tokenEntityResult = uiWalletRepository.getTokenById(
            token = token,
            userId = userId
        )

        if (tokenEntityResult is Result.Error) {
            val errorMessage = tokenEntityResult.error.toErrorMessage()
            call.respond(HttpStatusCode.NotFound, errorMessage)
            return@put
        }

        val tokenEntity = (tokenEntityResult as Result.Success).data
        val tokenExpired = tokenEntity.expiresAt < System.currentTimeMillis()
        if (tokenExpired) {
            call.respond(HttpStatusCode.NotAcceptable, ServerResponse(data = "Token Expired, Please request a new now"))
            return@put
        }


        val tokenRemoveResult = uiWalletRepository.removeToken(
            objectId = tokenEntity.id
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

fun Route.sendOtpToken(
    uiWalletRepository: UIWalletRepository
) {
    get("request-otp") {
        val emailAddress = call.queryParameters["email"]
        val purpose = call.queryParameters["purpose"]
        if (emailAddress == null || purpose == null) {
            call.respond(
                HttpStatusCode.NotAcceptable, "Email Address and purpose is required"
            )
            return@get
        }
        val user = uiWalletRepository.getUserById(
            matricNumber = null,
            emailAddress = emailAddress,
            objectId = null
        )
        if (user is Result.Error) {
            val errorMessage = user.error.toErrorMessage()
            call.respond(
                HttpStatusCode.NotFound, errorMessage
            )
            return@get
        }

        val token = (100000..999999).random().toString()

        val mailSendResult = MailService.sendVerificationToken(
            userEmail = emailAddress,
            token = token
        )
        println("Token is $token")
        if (mailSendResult is Result.Error) {
            call.respond(HttpStatusCode.NotAcceptable, "Unable to send mail to your email address")
            return@get
            //
        }

        val regex = Regex("\\w+}")
        val userId = (user as Result.Success).data.id.toString()
        //println("Added user $addedUserObjectId")
        // val userId = regex.find(addedUserObjectId)!!.value.dropLast(1)

        val tokenEntity = TokenEntity(
            userId = userId,
            hashedToken = token,
            purpose = purpose,
            expiresAt = System.currentTimeMillis() + 5L * 60L * 1000L
        )

        uiWalletRepository.addToken(token = tokenEntity)
        call.respond(HttpStatusCode.OK, ServerResponse(data = "Otp Code sent to $emailAddress"))

    }
}