package com.crezent.finalyearproject.core.data.security.encryption

import android.security.keystore.KeyProperties
import android.util.Base64
import com.crezent.finalyearproject.ANDROID_KEY_STORE
import com.crezent.finalyearproject.EC_ALIAS
import com.crezent.finalyearproject.RSA_ALIAS
import com.crezent.finalyearproject.SIGNATURE_ALGORITHM
import com.crezent.finalyearproject.TRANSFORMATION
import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.KeyStore
import java.security.KeyStore.PrivateKeyEntry
import java.security.PublicKey
import java.security.Security
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher


actual object CryptographicOperation {

    private const val GCM_IV_LENGTH = 12
    private const val GCM_TAG_LENGTH = 16

    init {
        if (Security.getProvider("BC") == null) {
            // Security.addProvider(BouncyCastleProvider())
        }
        //Security.addProvider(BouncyCastleProvider())

    }

    private val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply {
        load(null)
    }

    private val rsaKeyEntry by lazy {
        keyStore.getEntry(RSA_ALIAS, null) as? PrivateKeyEntry

    }

    private val ecKeyEntry by lazy {
        keyStore.getEntry(EC_ALIAS, null) as? PrivateKeyEntry

    }


    actual fun encryptData(serverPublicKey: String, data: String): String {

        val publicKey = decodePublicKey(serverPublicKey)
        println("Public key decoded successfully")

        // Print available providers and their algorithms


        println("Using transformation: $TRANSFORMATION")
        val cipher = Cipher.getInstance(TRANSFORMATION)
        println("Cipher created successfully")

        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        println("Cipher initialized successfully")

        val encrypted = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        println("Encryption completed. Encrypted length: ${encrypted.size}")

        val result = Base64.encodeToString(encrypted, Base64.DEFAULT)
        println("Base64 encoding completed. Final length: ${result.length}")

        return result


    }

    actual fun decryptData(encryptedData: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val privateKey = rsaKeyEntry?.privateKey

        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val decipher = cipher.doFinal(encryptedData.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(
            decipher, Base64.DEFAULT
        )
    }

    actual fun signData(dataToSign: String): String {
        try {

            val signature = Signature.getInstance(SIGNATURE_ALGORITHM)
            val privateKey = ecKeyEntry?.privateKey
            signature.initSign(privateKey)
            signature.update(dataToSign.toByteArray())
            return Base64.encodeToString(signature.sign(), Base64.DEFAULT)
        } catch (error: Exception) {
            error.printStackTrace()
            throw Exception()
        }
    }

    actual fun verifySignature(
        signature: String,
        dataToVerify: String,
        publicKey: String
    ): Boolean {

        try {

            val publicKeyInstance = decodePublicKey(publicKey)
            val signatureInstance = Signature.getInstance(SIGNATURE_ALGORITHM)
            signatureInstance.initVerify(publicKeyInstance)
            signatureInstance.update(dataToVerify.toByteArray())
            return signatureInstance.verify(Base64.decode(dataToVerify, Base64.DEFAULT))
        } catch (error: InvalidKeyException) {
            error.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun decodePublicKey(publicKeyString: String): PublicKey {
        val decodedByte = Base64.decode(publicKeyString, Base64.DEFAULT)
        val keyFactory = KeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
        val keySpec = X509EncodedKeySpec(decodedByte)
        return keyFactory.generatePublic(keySpec)
    }

}