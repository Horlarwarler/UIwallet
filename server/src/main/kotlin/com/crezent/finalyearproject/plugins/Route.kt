package com.crezent.finalyearproject.plugins

import com.crezent.finalyearproject.data.repo.PaymentRepository
import com.crezent.finalyearproject.data.repo.UIWalletRepository
import com.crezent.finalyearproject.routes.*
import com.crezent.finalyearproject.utility.security.encryption.ECBEncryptService
import com.crezent.finalyearproject.utility.security.encryption.SigningService
import com.crezent.finalyearproject.utility.security.hashing.HashingService
import com.crezent.finalyearproject.utility.security.token.JwtTokenService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRoute(

) {
//    val serverPrivateKey = System.getenv("server_private_key")
    //val serverPublicKey = System.getenv("server_public_key")

//    val keyPair = generateKeyPair()
//    val serverPublicKey = exportPublicKeyToBase64(keyPair)
//    val serverPrivateKey = exportPrivateKeyToBase64(keyPair)

    val ecPublicKey = System.getenv("ec_public_key")
    val key = System.getenv("key")
    val ecPrivateKey = System.getenv("ec_private_key")

    val rsaPublicKey = System.getenv("rsa_public_key")
    val rsaPrivateKey = System.getenv("rsa_private_key")

    val secret = System.getenv("jwt_secret")!!
    val audience = environment.config.property("jwt.audience").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    // val audience = environment.config.property("jwt.audience")


    // println("Server Public key : $serverPublicKey")
    val encryptService = ECBEncryptService()

    val uiWalletRepository by inject<UIWalletRepository>()
    val hashingService by inject<HashingService>()
    val paymentRepository by inject<PaymentRepository>()

    val jwtTokenService = JwtTokenService()


    routing {
        singIn(
            ecPrivateKeyString = ecPrivateKey,

            signingService = SigningService,
            encryptService = encryptService,
            uiWalletRepository = uiWalletRepository,
            hashingService = hashingService,
            rsaPrivateKeyString = rsaPrivateKey,
            tokenService = jwtTokenService,
            audience = audience,
            secret = secret,
            issuer = issuer
        )
        signUp(

            signingService = SigningService,
            encryptService = encryptService,
            uiWalletRepository = uiWalletRepository,
            hashingService = hashingService,
            rsaPrivateKeyString = rsaPrivateKey,
            ecPrivateKeyString = ecPrivateKey
        )
        getUser(
            serverPrivateKeyString = "serverPrivateKey",
            signingService = SigningService,
            encryptService = encryptService,
            uiWalletRepository = uiWalletRepository,
            serverPublicKeyString = "serverPublicKey"
        )
        verifyEmail(
            uiWalletRepository = uiWalletRepository,
        )
        getApiKey(
            rsaPublicKey = rsaPublicKey,
            ecPublicKey = ecPublicKey
        )
        sendOtpToken(
            uiWalletRepository = uiWalletRepository
        )

        resetPassword(
            signingService = SigningService,
            encryptService = encryptService,
            uiWalletRepository = uiWalletRepository,
            hashingService = hashingService,
            rsaPrivateKeyString = rsaPrivateKey,
            ecPrivateKeyString = ecPrivateKey

        )
        verifyOtp(
            tokenService = jwtTokenService,
            uiWalletRepository = uiWalletRepository,
            audience = audience,
            secret = secret,
            issuer = issuer
        )

        createCard(
            rsaPrivateKeyString = rsaPrivateKey,
            paymentRepository = paymentRepository,
            signingService = SigningService,
            ecPrivateKeyString = ecPrivateKey,
            encryptService = encryptService,
            key = key
        )
        verifyCvv(
            rsaPrivateKeyString = rsaPrivateKey,
            paymentRepository = paymentRepository,
            signingService = SigningService,
            hashingService = hashingService,
            ecPrivateKeyString = ecPrivateKey,
            encryptService = encryptService

        )

        getAuthenticatedUser(
            ecPrivateKeyString = ecPrivateKey,
            signingService = SigningService,
            encryptService = encryptService,
            uiWalletRepository = uiWalletRepository,
            serverEcPublicKey = ecPublicKey,
            serverRsaPublicKey = rsaPublicKey,
            key = key
        )

    }
}