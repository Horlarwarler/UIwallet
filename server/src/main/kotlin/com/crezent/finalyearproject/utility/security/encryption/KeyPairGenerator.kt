package com.crezent.finalyearproject.utility.security.encryption

import com.crezent.finalyearproject.utility.Constant.KEY_PAIR_ALGORITHM
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters
import org.bouncycastle.jcajce.provider.asymmetric.rsa.AlgorithmParametersSpi.OAEP
import org.bouncycastle.jcajce.util.AlgorithmParametersUtils
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.Security
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.spec.ECGenParameterSpec
import java.security.spec.ECParameterSpec
import java.util.Base64

object KeyPairGenerator {

    init {
        Security.addProvider(BouncyCastleProvider())
    }

//    fun generateKeyPair(): KeyPair {
//        val keyGen = KeyPairGenerator.getInstance("EC")
//        keyGen.initialize(256)
//        return keyGen.generateKeyPair()
//    }

    fun generateKeyPair(): KeyPair {
        val keyGen = KeyPairGenerator
            .getInstance("RSA")

     //   val ecSpec = ECGenParameterSpec("secp256r1")
       // keyGen.initialize(ecSpec)
        return keyGen.generateKeyPair()

    }

    fun exportPublicKeyToBase64(keyPair: KeyPair): String {
        return Base64.getEncoder().encodeToString(keyPair.public.encoded)
    }

//    fun exportPublicKeyToBase64(keyPair: KeyPair): String {
//        val publicKey = keyPair.public as ECPublicKey
//        val point = publicKey.w
//
//        // Get X and Y coordinates
//        val x = point.affineX.toByteArray()
//        val y = point.affineY.toByteArray()
//
//        // Combine X and Y with a prefix byte (0x04 indicates uncompressed point)
//        val keyBytes = ByteArray(65)
//        keyBytes[0] = 0x04
//        System.arraycopy(x, 0, keyBytes, 1 + (32 - x.size), x.size)
//        System.arraycopy(y, 0, keyBytes, 33 + (32 - y.size), y.size)
//
//        return Base64.getEncoder().encodeToString(keyBytes)
//    }

    fun exportPrivateKeyToBase64(keyPair: KeyPair): String {
        return Base64.getEncoder().encodeToString(keyPair.private.encoded)
    }


//
//    //fun ex
//    fun exportPublicKey(
//        publicKey: ECPublicKey
//    ): String {
//        val pkBytes = publicKey.encoded
//        println("First few bytes: ${pkBytes.take(10).joinToString(", ")}")
//        return Base64.getEncoder().encodeToString(pkBytes)
//    }
//
//    fun exportPrivateKey(
//        privateKey: ECPrivateKey
//    ): String {
//        return Base64.getEncoder().encodeToString(privateKey.encoded)
//    }
}
