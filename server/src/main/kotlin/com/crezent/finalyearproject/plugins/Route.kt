package com.crezent.finalyearproject.plugins

import com.crezent.finalyearproject.data.repo.UIWalletRepository
import com.crezent.finalyearproject.routes.*
import com.crezent.finalyearproject.utility.security.encryption.ECBEncryptService
import com.crezent.finalyearproject.utility.security.encryption.KeyPairGenerator.exportPrivateKeyToBase64
import com.crezent.finalyearproject.utility.security.encryption.KeyPairGenerator.exportPublicKeyToBase64
import com.crezent.finalyearproject.utility.security.encryption.KeyPairGenerator.generateKeyPair
import com.crezent.finalyearproject.utility.security.encryption.SigningService
import com.crezent.finalyearproject.utility.security.hashing.HashingService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRoute(

) {
//    val serverPrivateKey = System.getenv("server_private_key")
    //val serverPublicKey = System.getenv("server_public_key")

    val keyPair = generateKeyPair()
    val serverPublicKey = exportPublicKeyToBase64(keyPair)
    val serverPrivateKey = exportPrivateKeyToBase64(keyPair)

    val ecPublicKey = System.getenv("ec_public_key")
    val ecPrivateKey = System.getenv("ec_private_key")

    val rsaPublicKey = System.getenv("rsa_public_key")
    val rsaPrivateKey = System.getenv("rsa_private_key")



    println("Server Public key : $serverPublicKey")
    val encryptService = ECBEncryptService()

    val uiWalletRepository by inject<UIWalletRepository>()
    val hashingService by inject<HashingService>()


    routing {
        singIn(
            ecPrivateKeyString = ecPrivateKey,

            signingService = SigningService,
            encryptService = encryptService,
            uiWalletRepository = uiWalletRepository,
            hashingService = hashingService,
            serverEcPublicKey = ecPublicKey,
            serverRsaPublicKey = rsaPublicKey,
            rsaPrivateKeyString = rsaPrivateKey

        )
        signUp(
            serverPrivateKeyString = serverPrivateKey,
            signingService = SigningService,
            encryptService = encryptService,
            uiWalletRepository = uiWalletRepository,
            hashingService = hashingService
        )
        getUser(
            serverPrivateKeyString = serverPrivateKey,
            signingService = SigningService,
            encryptService = encryptService,
            uiWalletRepository = uiWalletRepository,
            serverPublicKeyString = serverPublicKey
        )
        verifyEmail(
            serverPrivateKeyString = serverPrivateKey,
            signingService = SigningService,
            encryptService = encryptService,
            uiWalletRepository = uiWalletRepository,
            serverPublicKeyString = serverPublicKey
        )
        getApiKey(
            serverPublicKey = serverPublicKey
        )
    }
}