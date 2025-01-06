package com.crezent.finalyearproject.utility.security.encryption

import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

object SigningService {

    fun signData(data: String, privateKeyString: String): String {
        val privateKeyByte = Base64.getDecoder().decode(privateKeyString)
        val keyFactory = KeyFactory.getInstance("EC")
        val privateKey = keyFactory.generatePrivate(PKCS8EncodedKeySpec(privateKeyByte))

        val signature = Signature.getInstance("SHA256withECDSA")
        signature.initSign(privateKey)
        signature.update(data.toByteArray())
        return Base64.getEncoder().encodeToString(signature.sign())
    }

    fun verifySignature(data: String, signatureString: String, ecPublicKeyString: String): Boolean {
        println("Public key $ecPublicKeyString")
        val publicKeyBytes = Base64.getDecoder().decode(ecPublicKeyString)
        val keyFactory = KeyFactory
            .getInstance("EC")
        try {
            val publicKey = keyFactory.generatePublic(X509EncodedKeySpec(publicKeyBytes))

            val signature = Signature.getInstance("SHA256withECDSA")
            signature.initVerify(publicKey)
            signature.update(data.toByteArray())
            return signature.verify(Base64.getDecoder().decode(signatureString))
        }
        catch (error:InvalidKeyException){
            error.printStackTrace()
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        return false

    }
}