package com.crezent.finalyearproject.core.data.security.encryption

import com.crezent.security.KeyPairGenerator
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSError


@OptIn(ExperimentalForeignApi::class)

actual object KeyPairGenerator {

    private val keyPairGenerator = KeyPairGenerator()

    @OptIn(BetaInteropApi::class)
    actual fun generateKeyStore() {
        memScoped {

            val errorPointer = nativeHeap.alloc<ObjCObjectVar<NSError?>>()

            val isSuccess = keyPairGenerator.generateKeyPairAndReturnError(
                error = errorPointer.ptr
            )
            if (!isSuccess) {
                val nsError = errorPointer.value
                println("Error occurred: ${nsError?.localizedDescription}")
            }


        }
        //  KeyPairGenerator().getPublicKey()
    }


    actual fun getClientKeyPair(alias: String): String {
        println("Will get key is null")

        val publicKeyString = keyPairGenerator.getPublicKeyString() ?: kotlin.run {
            println("Generating  key is null")
            return ""
        }

        println("Public key is $publicKeyString")

        return publicKeyString


    }


}
