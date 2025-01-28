package com.crezent.finalyearproject.routes

import com.crezent.finalyearproject.RESET_PASSWORD_PURPOSE
import com.crezent.finalyearproject.VERIFY_EMAIL_PURPOSE
import com.crezent.finalyearproject.data.database.entity.TokenEntity
import com.crezent.finalyearproject.data.database.entity.UserEntity
import com.crezent.finalyearproject.data.dto.*
import com.crezent.finalyearproject.data.repo.UIWalletRepository
import com.crezent.finalyearproject.domain.util.*
import com.crezent.finalyearproject.routes.util.decryptVerifiedMessage
import com.crezent.finalyearproject.routes.util.getEmailFromJwt
import com.crezent.finalyearproject.services.MailService
import com.crezent.finalyearproject.utility.security.encryption.EncryptService
import com.crezent.finalyearproject.utility.security.encryption.SigningService
import com.crezent.finalyearproject.utility.security.hashing.HashingService
import com.crezent.finalyearproject.utility.security.hashing.SaltedHash
import com.crezent.finalyearproject.utility.security.token.TokenClaim
import com.crezent.finalyearproject.utility.security.token.TokenConfig
import com.crezent.finalyearproject.utility.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
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
    hashingService: HashingService,
    tokenService: TokenService,
    uiWalletRepository: UIWalletRepository,
    audience: String,
    secret: String,
    issuer: String

) {
    post("login") {

        val decryptVerifiedMessage = call.decryptVerifiedMessage<LoginDetails>(
            rsaPrivateKeyString = rsaPrivateKeyString,
            signingService = signingService,
            encryptService = encryptService,
            ecPrivateKeyString = ecPrivateKeyString
        )

        if (decryptVerifiedMessage !is Result.Success) {
            return@post
        }
        val loginDetails = decryptVerifiedMessage.data.data

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
        val oneDay = 24 * 60L * 60L * 1000L
        val tokenConfig = TokenConfig(
            issuer = issuer,
            audience = audience,
            secret = secret,
            expiresAt = oneDay
        )
        val usernameClaim = TokenClaim(name = "email", value = loginDetails.emailAddress)
        val jwtToken = tokenService.generateToken(
            claims = arrayOf(usernameClaim),
            config = tokenConfig
        )
        call.respond(HttpStatusCode.OK, ServerResponse(data = jwtToken))

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
    ecPrivateKeyString: String


) {
    post("sign-up") {

        val decryptVerifiedMessage = call.decryptVerifiedMessage<SignUpDetails>(
            rsaPrivateKeyString = rsaPrivateKeyString,
            signingService = signingService,
            encryptService = encryptService,
            ecPrivateKeyString = ecPrivateKeyString

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


        val userId = addedUser.data
        val tokenEntity = TokenEntity(
            userId = userId,
            hashedToken = token,
            purpose = VERIFY_EMAIL_PURPOSE,
            expiresAt = System.currentTimeMillis() + 5L * 60L * 1000L,
            emailAddress = signUpDetails.emailAddress
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

fun Route.getAuthenticatedUser(
    ecPrivateKeyString: String, // Use for signing value
    signingService: SigningService,
    encryptService: EncryptService,
    uiWalletRepository: UIWalletRepository,
    key:String,
    serverEcPublicKey: String,
    serverRsaPublicKey: String
) {
    authenticate {
        post("authenticated-user") {
            val email = getEmailFromJwt() ?: return@post
            val userResult = uiWalletRepository.getUserById(emailAddress = email)
            val rsaPublicKey = call.receiveText()

            if (userResult is Result.Error) {
                call.respond(
                    HttpStatusCode.NotFound, message = userResult.error.toErrorMessage()
                )
                return@post
            }

            val user = (userResult as Result.Success).data

            val userString = Json.encodeToString(
                user.toLoggedInUser(
                    encryptService = encryptService,
                    key = key
                )
            )


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

        }
    }
}


fun Route.verifyEmail(

    uiWalletRepository: UIWalletRepository,

    ) {
    authenticate("otp-jwt") {
        put("verify-mail") {


            //val token = call.queryParameters["token"]
            val clientRsaPublicKey = call.receiveText()

            val emailAddress = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()


            if (emailAddress == null) {
                call.respond(HttpStatusCode.NotAcceptable, "User Email is required")
                return@put
            }
            val tokenEntityResult = uiWalletRepository.getTokenByEmail(
                emailAddress = emailAddress,
                purpose = VERIFY_EMAIL_PURPOSE
            )

            val tokenValid = handleTokenValidity(VERIFY_EMAIL_PURPOSE, tokenEntityResult)

            if (!tokenValid) {
                return@put
            }


            val userUpdateResult = uiWalletRepository.updateUserEmailVerify(
                isVerified = true,
                emailAddress = emailAddress
            )
            if (userUpdateResult is Result.Error) {
                println("User Update result ${userUpdateResult.error}")
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Issue from our end,please try again"
                )
                return@put
            }

            call.respond(HttpStatusCode.OK, ServerResponse("Email Verified"))

//            val loggedInUser = (userUpdateResult as Result.Success).data.toLoggedInUser(
//                encryptService = encryptService,
//                rsaPrivateKeyString = rsaPrivateKeyString
//            )
//            val encodeToString = Json.encodeToString(loggedInUser)
//
//
//            val encryptedUser = encryptService.encryptData(
//                value = encodeToString,
//                clientRsapublicKey = clientRsaPublicKey
//            ) ?: run {
//                call.respond(
//                    HttpStatusCode.BadRequest,
//                    "Issue from our end,please try again"
//                )
//                return@put
//            }
//            val signature = signingService.signData(
//                data = encryptedUser.aesEncryptedString,
//                privateKeyString = ecPrivateKeyString
//            )
//
//            val encryptedModel = EncryptedModel(
//                signature = signature,
//                encryptedData = encryptedUser.aesEncryptedString,
//                ecKey = serverEcPublicKey,
//                rsaKey = serverRsaPublicKey,
//                aesKey = encryptedUser.rsaEncryptedKey
//            )
//
//            call.respond(HttpStatusCode.OK, message = ServerResponse(data = encryptedModel)) // TO verification only

        }

    }
}

fun Route.verifyOtp(
    tokenService: TokenService,
    uiWalletRepository: UIWalletRepository,
    audience: String,
    secret: String,
    issuer: String
) {
    get("verify-otp") {

        val token = call.queryParameters["token"]

        val emailAddress = call.queryParameters["email"]
        val purpose = call.queryParameters["purpose"]


        if (token == null || emailAddress == null || purpose == null) {
            call.respond(HttpStatusCode.NotAcceptable, "Email, token purpose and token is required")
            return@get
        }


        val tokenEntityResult = uiWalletRepository.getTokenById(
            token = token,
            userId = null,
            emailAddress = emailAddress
        )
        val tokenValid = handleTokenValidity(purpose, tokenEntityResult)

        if (!tokenValid) {
            return@get
        }
        val fiveMinute = 5L * 60L * 1000L
        val tokenConfig = TokenConfig(
            issuer = issuer,
            audience = audience,
            secret = secret,
            expiresAt = fiveMinute
        )
        val usernameClaim = TokenClaim(name = "email", value = emailAddress)
        val jwtToken = tokenService.generateToken(
            claims = arrayOf(usernameClaim),
            config = tokenConfig
        )
        call.respond(HttpStatusCode.OK, ServerResponse(data = jwtToken))
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
            val errorMessage = user.error.toErrorMessage(errorToType = DatabaseErrorToType.ResetPasswordError)
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
        println("Token is $token , purpose is $purpose")
        if (mailSendResult is Result.Error) {
            call.respond(HttpStatusCode.NotAcceptable, "Unable to send mail to your email address")
            return@get
            //
        }

        val userId = (user as Result.Success).data.id.toString()

        val tokenEntity = TokenEntity(
            userId = userId,
            hashedToken = token,
            purpose = purpose,
            expiresAt = System.currentTimeMillis() + 5L * 60L * 1000L, //5min
            emailAddress = emailAddress
        )
        uiWalletRepository.deleteExistingToken(userId = userId)

        uiWalletRepository.addToken(token = tokenEntity)
        call.respond(HttpStatusCode.OK, ServerResponse(data = "Otp Code sent to $emailAddress"))

    }
}


fun Route.resetPassword(
    rsaPrivateKeyString: String, // Use for decrypting value
    signingService: SigningService,
    encryptService: EncryptService,
    uiWalletRepository: UIWalletRepository,
    ecPrivateKeyString: String,
    hashingService: HashingService,
) {
    authenticate("otp-jwt") {
        put("reset-password") {

            val jwtPrincipal = call.principal<JWTPrincipal>()
            val emailAddress = jwtPrincipal?.payload?.getClaim("email")?.asString()

            if (emailAddress == null) {
                call.respond(HttpStatusCode.NotAcceptable, "User Email is required")
                return@put
            }
            val tokenEntityResult = uiWalletRepository.getTokenByEmail(
                emailAddress = emailAddress,
                purpose = RESET_PASSWORD_PURPOSE
            )


            val tokenValid = handleTokenValidity(RESET_PASSWORD_PURPOSE, tokenEntityResult)

            if (!tokenValid) {
                return@put
            }

            val decryptVerifiedMessage = call.decryptVerifiedMessage<ResetPassword>(
                rsaPrivateKeyString = rsaPrivateKeyString,
                signingService = signingService,
                encryptService = encryptService,
                ecPrivateKeyString = ecPrivateKeyString
            )


            if (decryptVerifiedMessage !is Result.Success) {
                return@put
            }

            val userResult = uiWalletRepository.getUserById(
                matricNumber = null,
                emailAddress = emailAddress,
                objectId = null
            )

            if (userResult is Result.Error) {
                call.respond(HttpStatusCode.NotFound, message = userResult.error.toErrorMessage())
                return@put
            }

            val user = (userResult as Result.Success).data

            val existingPasswords = user.lastUsedPasswords.toSet()
            val hashedPassword = hashingService.hashValue(value = decryptVerifiedMessage.data.data.password)
            if (existingPasswords.contains(hashedPassword.hashedValue) || user.hashedPassword == hashedPassword.hashedValue) {
                call.respond(HttpStatusCode.NotModified, "You can't use already existing password")
                return@put
            }
            val newLastPassword = existingPasswords.toMutableList()
            newLastPassword.addLast(hashedPassword.hashedValue)
            val editedUser = user.copy(
                hashedPassword = hashedPassword.hashedValue,
                lastUsedPasswords = newLastPassword
            )

            val result = uiWalletRepository.updateUser(editedUser)
            if (result is Result.Error) {
                call.respond(HttpStatusCode.NotModified, "Error updating user")
                return@put
            }
            call.respond(HttpStatusCode.OK, ServerResponse(data = "Password Changed Successfully"))

        }

    }

}

suspend fun RoutingContext.handleTokenValidity(
    purpose: String,
    tokenEntityResult: Result<TokenEntity, DatabaseError>,
): Boolean {
    if (tokenEntityResult is Result.Error) {
        val errorMessage = tokenEntityResult.error.toErrorMessage(errorToType = DatabaseErrorToType.TokenError)
        call.respond(HttpStatusCode.NotFound, errorMessage)
        return false
    }

    val tokenEntity = (tokenEntityResult as Result.Success).data
    val tokenExpired = tokenEntity.expiresAt < System.currentTimeMillis()
    if (tokenExpired) {
        call.respond(HttpStatusCode.Gone, "Token Expired, Please request a new now")
        return false
    }
    if (purpose != tokenEntity.purpose) {
        call.respond(HttpStatusCode.NotAcceptable, "Token not valid, Please request a new now")
        return false
    }
    return true
}