package com.crezent.finalyearproject.utility.security.encryption


import com.crezent.finalyearproject.ELLIPTIC_CURVE_STANDARD_NAME
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.ECPointUtil
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECNamedCurveSpec
import java.security.*
import java.security.spec.*
import java.util.*


object SigningService {

    init {
        Security.addProvider(BouncyCastleProvider())

    }

    fun signData(data: String, privateKeyString: String): String {

        val privateKeyByte = org.bouncycastle.util.encoders.Base64.decode(privateKeyString)
        val keyFactory = KeyFactory.getInstance("EC")
        val privateKey = keyFactory.generatePrivate(PKCS8EncodedKeySpec(privateKeyByte))
        val signature = Signature.getInstance("SHA256withECDSA")
        signature.initSign(privateKey)
        signature.update(data.toByteArray())
        val sign = signature.sign() // THIS THROW THE ERROR
        return Base64.getEncoder().encodeToString(sign)
    }

    fun verifySignature(data: String, signatureString: String, ecPublicKeyString: String): Boolean {
        println("Public key $ecPublicKeyString")
        try {
            val publicKey = importX509PublicKey(ecPublicKeyString)
            val signature = Signature.getInstance("SHA256withECDSA")
            signature.initVerify(publicKey)
            signature.update(data.toByteArray())
            return signature.verify(org.bouncycastle.util.encoders.Base64.decode(signatureString))
        } catch (error: InvalidKeyException) {
            error.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun importX509PublicKey(publicKeyString: String): PublicKey {
        // Decode the Base64-encoded key
        val publicKeyBytes = org.bouncycastle.util.encoders.Base64.decode(publicKeyString)

        val keyFactory = KeyFactory.getInstance("EC")
        val keySpec = X509EncodedKeySpec(publicKeyBytes)
        return keyFactory.generatePublic(keySpec)

    }



}