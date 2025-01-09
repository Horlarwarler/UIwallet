package com.crezent.finalyearproject.utility.security.encryption


import com.crezent.finalyearproject.ELLIPTIC_CURVE_STANDARD_NAME
import io.netty.handler.codec.base64.Base64Decoder
import org.bouncycastle.crypto.util.SubjectPublicKeyInfoFactory
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.ECPointUtil
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECNamedCurveSpec
import java.math.BigInteger
import java.security.*
import java.security.interfaces.ECPublicKey
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
        return org.bouncycastle.util.encoders.Base64.toBase64String(signature.sign())
    }

    fun verifySignature(data: String, signatureString: String, ecPublicKeyString: String): Boolean {
        println("Public key $ecPublicKeyString")


        try {
            //  val publicKeyBytes = Base64.getDecoder().decode(ecPublicKeyString)
//
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

//    private fun getPublicKey(
//        publicKey: String
//    ): ECPublicKey {
////        SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo()
////        val publicKeyBytes = Base64.getDecoder().decode(publicKey)
////        println(publicKeyBytes)
////

//
//    }

    private fun getEcParameterSpec(): ECParameterSpec {
        val params = AlgorithmParameters.getInstance("EC")
        params.init(ECGenParameterSpec(ELLIPTIC_CURVE_STANDARD_NAME))
        return params.getParameterSpec(ECParameterSpec::class.java)
    }

    fun importX509PublicKey(publicKeyString: String): PublicKey {
        // Decode the Base64-encoded key
        val publicKeyBytes = org.bouncycastle.util.encoders.Base64.decode(publicKeyString)
        // .getDecoder().decode(publicKeyString)

        println("public key server $publicKeyBytes")

        val keyFactory = KeyFactory.getInstance("EC")
        return keyFactory.generatePublic(X509EncodedKeySpec(publicKeyBytes))

        // Use X509EncodedKeySpec to construct the key
    }


}