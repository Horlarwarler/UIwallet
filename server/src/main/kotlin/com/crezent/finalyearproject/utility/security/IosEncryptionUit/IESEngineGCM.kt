package com.crezent.finalyearproject.utility.security.IosEncryptionUit

import org.bouncycastle.crypto.*
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.generators.KDF2BytesGenerator
import org.bouncycastle.crypto.params.*
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.ECPointUtil
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECNamedCurveSpec
import org.bouncycastle.jce.spec.ECParameterSpec
import org.bouncycastle.util.Arrays
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.ECPoint
import java.security.spec.ECPublicKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyAgreement
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec


class IOSEncryptionECwithAES {

    fun decryptEciesEncryption(
        privateKey: PrivateKey,
        encryptedString: ByteArray
    ): ByteArray {

        //Note the byte array contains the ephemeral client public key, the cipher text, tag
        // 1. Take ephemeral public key

        val ephemeralKeyBytes = Arrays.copyOfRange(encryptedString, 0, 65)
        val ephemeralPublicKey: PublicKey = getPublicKey(ephemeralKeyBytes) // Generate the public key for
        val encryptedData = Arrays.copyOfRange(encryptedString, 65, encryptedString.size)

        // 2. Key agreement using ECDH with Cofactor and integrated X9.63

        val initialSecret = getInitialSecret(ephemeralPublicKey, privateKey)
        val kdfOut = getDerivation(initialSecret, ephemeralKeyBytes) // Get the KDF


        val secretKeyBytes = Arrays.copyOfRange(kdfOut, 0, 16) // The first 16 element is the secret
        val secretKey: SecretKey = SecretKeySpec(secretKeyBytes, "AES")

        // 4. Decrypt with AES key
        val tagLength = 128
        val iv = Arrays.copyOfRange(kdfOut, 16, kdfOut.size)
        val aesGcmParams = GCMParameterSpec(tagLength, iv)
        val aesCipher = Cipher.getInstance("AES/GCM/NoPadding")
        aesCipher.init(Cipher.DECRYPT_MODE, secretKey, aesGcmParams)
        val decryptedData = aesCipher.doFinal(encryptedData)

        return decryptedData
    }


    @Throws(Exception::class)
    private fun getPublicKey(encodedBytes: ByteArray): PublicKey {
        val keyFactory: KeyFactory = KeyFactory.getInstance("EC")
        val ecParameterSpec: ECParameterSpec = ECNamedCurveTable.getParameterSpec("secp256r1")
        val params =
            ECNamedCurveSpec("secp256r1", ecParameterSpec.curve, ecParameterSpec.g, ecParameterSpec.n)
        val publicPoint: ECPoint = ECPointUtil.decodePoint(params.curve, encodedBytes)
        val pubKeySpec = ECPublicKeySpec(publicPoint, params)
        return keyFactory.generatePublic(pubKeySpec)
    }



    @Throws(java.lang.Exception::class)
    private fun getInitialSecret(ephemeralPublicKey: PublicKey, privateKey: PrivateKey): ByteArray {
        val keyAgreementAlgorithm = "ECDH" // Seems to be equivalent to "ECDHC"
        val keyAgreement = KeyAgreement.getInstance(keyAgreementAlgorithm, BouncyCastleProvider())
        keyAgreement.init(privateKey)
        keyAgreement.doPhase(ephemeralPublicKey, true)
        return keyAgreement.generateSecret()
    }

    private fun getDerivation(initialSecret: ByteArray, ephemeralKeyBytes: ByteArray): ByteArray {
        val kdfGenerator = KDF2BytesGenerator(SHA256Digest())
        kdfGenerator.init(KDFParameters(initialSecret, ephemeralKeyBytes))
        val kdfOut = ByteArray(32)
        kdfGenerator.generateBytes(kdfOut, 0, 32)
        return kdfOut
    }
}