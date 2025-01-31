package com.crezent.finalyearproject.core.data.security.encryption

import com.crezent.finalyearproject.models.EncryptionKeyValue
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSError

//@OptIn(ExperimentalForeignApi::class)


//actual object CryptographicOperation {
//    //private val cryptographicOperation = CryptographicOperation()
//    actual fun encryptData(serverPublicKey: String, data: String): EncryptionKeyValue {
//        return TODO()

//        val error = nativeHeap.alloc<ObjCObjectVar<NSError?>>()
//        val encryptedData = cryptographicOperation.encryptDataWithData(
//            data = data,
//            publicKeyString = test,
//            error = error.ptr
//        )
//        @OptIn(BetaInteropApi::class)
//
//        if (error.value != null || encryptedData == null) {
//            println("Error encrypting data ${error.value}")
//            throw Exception("Can not encrypted")
//        }
//
//        val rsaEncryptedKey = encryptedData["rsaEncryptedKey"] as String
//        val aesEncryptedString = encryptedData["aesEncryptedString"] as String
//
//
//        return EncryptionKeyValue(
//            aesEncryptedString = aesEncryptedString,
//            rsaEncryptedKey = rsaEncryptedKey
//        )


 //   }

//    @OptIn(BetaInteropApi::class)
//    actual fun decryptData(
//        encryptedData: String,
//        encryptedAesKey: String
//    ): String {
//        return  TODO()
//        val error = nativeHeap.alloc<ObjCObjectVar<NSError?>>()
//        val decryptedData = cryptographicOperation.decryptDataWithEncryptedData(
//            encryptedData = encryptedData,
//            aesEncryptedString = encryptedAesKey,
//            error = error.ptr
//        )
//
//        if (error.value != null || decryptedData == null) {
//            println("Error decrypting data ${error.value}")
//            throw Exception("Can not decrypted")
//        }
//        return decryptedData
  //  }

//    actual fun signData(dataToSign: String): String {
//
//        TODO()
//        val error = nativeHeap.alloc<ObjCObjectVar<NSError?>>()
//        val signature = cryptographicOperation.signDataWithEncryptedData(
//            encryptedData = dataToSign,
//            error = error.ptr
//        )
//        @OptIn(BetaInteropApi::class)
//
//        if (error.value != null || signature == null) {
//            println("Error signing data ${error.value}")
//            throw Exception("Can not be sign")
//        }
//
//        return signature

  //  }
//
//    actual fun verifySignature(
//        signature: String,
//        dataToVerify: String,
//        publicKey: String
//    ): Boolean {
      //  TODO()
//        val error = nativeHeap.alloc<ObjCObjectVar<NSError?>>()
//        val isVerified = cryptographicOperation.verifySignatureWithData(
//            data = dataToVerify,
//            signature = signature,
//            publicKey = publicKey,
//            error = error.ptr
//        )
//        @OptIn(BetaInteropApi::class)
//
//        if (error.value != null || isVerified == null) {
//            println("Error signing data ${error.value}")
//            throw Exception("Can not be sign")
//        }
//
//        return isVerified == "V"
//    }
//
//
//}